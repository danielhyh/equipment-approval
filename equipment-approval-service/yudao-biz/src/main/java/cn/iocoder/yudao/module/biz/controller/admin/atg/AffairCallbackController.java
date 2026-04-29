package cn.iocoder.yudao.module.biz.controller.admin.atg;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.admin.atg.dto.CallbackApplication;
import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationCreateReqVO;
import cn.iocoder.yudao.module.biz.controller.app.application.vo.AppApplicationSaveReqVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.affairrecord.AffairRecordDO;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.dal.mysql.affairrecord.AffairRecordMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;
import cn.iocoder.yudao.module.biz.service.application.ApplicationService;
import cn.iocoder.yudao.module.biz.service.atg.AtgApplicationService;
import cn.iocoder.yudao.module.biz.service.notification.CreateNotificationRequest;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.operation.OperationLogService;
import com.alibaba.gov.api.client.AtgBusClient;
import com.alibaba.gov.api.domain.request.AtgBusRequest;
import com.alibaba.gov.api.domain.response.AtgBusResponse;
import com.alibaba.gov.api.internal.util.HttpMethodEnum;
import com.alibaba.gov.api.request.AtgBizAffairAcceptRequest;
import com.alibaba.gov.api.response.AtgBizAffairAcceptResponse;
import com.alibaba.gov.callbackapi.request.CallbackAtgBizAffairReceiveRequest;
import com.alibaba.gov.callbackapi.response.CallbackAtgBizAffairReceiveResponse;
import com.alibaba.gov.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/atg")
@Slf4j
public class AffairCallbackController {

    @Resource
    private ApplicationMapper applicationMapper;

    @Resource
    private AffairRecordMapper affairRecordMapper;

    @Resource
    private OperationLogService operationService;

    @Resource
    private NotificationService notificationService;

    @Resource
    private AtgBusClient client;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private AtgApplicationService atgApplicationService;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final Map<Integer, String> actionDescMap = Map.of(1, "许可证申请", 2, "许可证补办", 3, "许可证变更", 4, "基本信息变更");

    /**
     * 办件收件回调接口
     * 对应规范接口：callback.atg.biz.affair.receive
     */
    @PostMapping("/affair/receive")
    @ResponseBody
    public CallbackAtgBizAffairReceiveResponse receiveAffair(@RequestBody CallbackAtgBizAffairReceiveRequest request) {
        CallbackAtgBizAffairReceiveResponse response = new CallbackAtgBizAffairReceiveResponse();
        log.info("receive request:{}", JSON.toJSONString(request));
        log.info("aff Form Info:{}", JSON.toJSONString(request.getAffFormInfo()));

        String projId = null;
        try {
            // 1. 获取关键数据
            projId = request.getProjId(); // 统一办件单号
            //String applicantName = request.getApplicantVO().getApplyName(); // 申请人

            // 2. 幂等校验（事务外查询，避免长事务）
            AffairRecordDO existRecord = affairRecordMapper.selectByProjId(projId);
            if (existRecord != null && existRecord.getStatus() == 1) {
                log.info("办件单号 {} 已成功处理过，跳过重复回调", projId);
                response.setResultStatus("S");
                return response;
            }
            if (StrUtil.isBlank(request.getAffFormInfo())) {
                throw new IllegalArgumentException("参数为 null");
            }
            String recvDeptCode = request.getRecvDeptCode();
            String recvDeptName = request.getRecvDeptName();
            String areaCode = request.getAreaCode();
            // 3. 事务内：创建申请 + 远程受理 + 插入幂等记录，任一失败全部回滚
            final String finalProjId = projId;
            transactionTemplate.executeWithoutResult(status -> {
                // 3.1 调用远程受理接口
                AtgBizAffairAcceptResponse bizAffairAcceptResponse;
                try {
                    AtgBizAffairAcceptRequest atgReq = new AtgBizAffairAcceptRequest();
                    atgReq.setAppId("286301");
                    atgReq.setAreaCode(areaCode);
                    atgReq.setDeptCode(recvDeptCode);
                    atgReq.setDeptName(recvDeptName);
                    atgReq.setProjId(finalProjId);
                    atgReq.setOperatorUid("1");
                    atgReq.setOperatorName("陕西省大型设备管理员");
                    atgReq.setGmtAccept(new Date());
                    atgReq.setPromiseTime(DateUtils.addTime(Duration.ofDays(30)));
                    bizAffairAcceptResponse = client.execute(atgReq);
                } catch (Exception e) {
                    throw new RuntimeException("远程受理接口调用异常", e);
                }
                // 远程调用没抛异常，但业务状态非成功
                boolean result = bizAffairAcceptResponse != null && "S".equals(bizAffairAcceptResponse.getResultStatus());
                if (!result) {
                    throw new RuntimeException("远程受理接口返回失败: " +
                            (bizAffairAcceptResponse != null ? bizAffairAcceptResponse.getResultMsg() : "响应为空"));
                }

                // 3.2 创建高办系统申请（appType=7, appStatus=5）
                AtgApplicationCreateReqVO createReqVO = new AtgApplicationCreateReqVO();
                createReqVO.setProjId(finalProjId);
                createReqVO.setFormInfo(request.getAffFormInfo());
                // 如果有机构信息可以设置
                // createReqVO.setInstitutionId(...);
                // createReqVO.setInstitutionName(...);
                Long applicationId = atgApplicationService.createAtgApplication(createReqVO);

                // 3.3 插入幂等记录
                AffairRecordDO record = AffairRecordDO.builder()
                        .projId(finalProjId)
                        .applicationId(applicationId)
                        .status(1)
                        .build();
                affairRecordMapper.insert(record);
            });

            response.setResultStatus("S");

        } catch (Exception e) {
            log.error("高效通办系统回调报错, projId={}", projId, e);
            response.setResultStatus("F");
            response.setResultMsg("系统内部错误：" + e.getMessage());
        }
        response.setResultStatus("S");
        return response;
    }



    @SuppressWarnings("all")
    private Long createApplication(AppApplicationSaveReqVO createReqVO) {
        // 插入
        ApplicationDO application = BeanUtils.toBean(createReqVO, ApplicationDO.class);
        application.setAppNo("SQ-"+timeFormatter.format(LocalDateTime.now()));
        application.setAppStatus(1);//待初审
        application.setDeadline(LocalDate.now().plusDays(45));
        application.setCreator(String.valueOf(4082L));
        application.setUpdater(String.valueOf(4082L));

        applicationMapper.insert(application);
        //记录操作日志
        Long loginUserId = 4082L;
        String loginUserNickname = "高效通办系统";

        operationService.log(application.getId(), loginUserId, loginUserNickname, "发起的" + actionDescMap.get(createReqVO.getAppType()));
        //发通知
        var request = new CreateNotificationRequest();
        String institutionName = "高效通办系统";
        request.setAppId(application.getId());
        request.setTitle(institutionName);
        String format = String.format("%s(%s)", createReqVO.getLicenseDeviceName(), createReqVO.getLadderConfigModel());
        request.setContent(format);
        request.setPublishNow(false);
        request.setCreator("0");
        notificationService.createNotification(request);
        // 返回
        return application.getId();
    }

}
