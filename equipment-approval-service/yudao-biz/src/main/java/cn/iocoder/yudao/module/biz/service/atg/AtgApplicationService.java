package cn.iocoder.yudao.module.biz.service.atg;

import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationCreateReqVO;
import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationUpdateReqVO;

/**
 * 高效通办系统 - 申请 Service
 */
public interface AtgApplicationService {

    /**
     * 创建高办系统申请（回调时调用）
     * source=1, appStatus=5（默认已通过审核）
     */
    Long createAtgApplication(AtgApplicationCreateReqVO reqVO);

    /**
     * 补充设备信息并生成许可证（一步完成）
     * @return 许可证编号
     */
    String completeAndGenerateLicense(AtgApplicationUpdateReqVO reqVO);

}
