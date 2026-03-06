package cn.iocoder.yudao.module.biz.controller.admin.atg;

import com.alibaba.gov.callbackapi.request.CallbackAtgBizAffairReceiveRequest;
import com.alibaba.gov.callbackapi.response.CallbackAtgBizAffairReceiveResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atg/callback")
public class AffairCallbackController {

    /**
     * 办件收件回调接口
     * 对应规范接口：callback.atg.biz.affair.receive
     */
    @PostMapping("/affair/receive")
    @ResponseBody
    public CallbackAtgBizAffairReceiveResponse receiveAffair(@RequestBody CallbackAtgBizAffairReceiveRequest request) {
        CallbackAtgBizAffairReceiveResponse response = new CallbackAtgBizAffairReceiveResponse();

        try {
            // 1. 获取关键数据
            String projId = request.getProjId(); // 统一办件单号
            String applicantName = request.getApplicantVO().getApplyName(); // 申请人

            // 2. 业务处理 (落库、校验等)
            // TODO: 将 request 中的数据存入本地数据库，状态标记为"已收件"
            System.out.println("收到办件：" + projId + ", 申请人：" + applicantName);

            // 3. 设置成功响应
            response.setResultStatus("S"); // S:成功, F:失败, U:未知

            // 可选：返回路由后的事项编码或审批系统链接
            // response.setRoutedMatterCode("...");
            // response.setAuditSysUrl("http://your-system.com/check/" + projId);

        } catch (Exception e) {
            e.printStackTrace();
            response.setResultStatus("F");
            response.setResultMsg("系统内部错误：" + e.getMessage());
            // 注意：返回失败会导致平台重试，请确保幂等性
        }

        return response;
    }

    /**
     * 补齐补正受理回调
     * 对应规范接口：callback.atg.biz.affair.supplementAccept
     */
    @PostMapping("/affair/supplement")
    @ResponseBody
    public CallbackAtgBizAffairReceiveResponse supplementAccept(@RequestBody Object request) {
        // 类似上述逻辑，处理用户补充材料后的回调
        CallbackAtgBizAffairReceiveResponse response = new CallbackAtgBizAffairReceiveResponse();
        response.setResultStatus("S");
        return response;
    }
}
