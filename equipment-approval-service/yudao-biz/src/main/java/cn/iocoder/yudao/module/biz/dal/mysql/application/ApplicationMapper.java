package cn.iocoder.yudao.module.biz.dal.mysql.application;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.application.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 申请 Mapper
 *
 * @author listen
 */
@Mapper
public interface ApplicationMapper extends BaseMapperX<ApplicationDO> {


    IPage<ApplicationPageRespVO> page(IPage<ApplicationPageRespVO> page, @Param("reqVO") ApplicationPageReqVO reqVO);


    ApplicationBasicInformationVO selectBasicInfo(@Param("id") Long id);

    BusinessInfoVO businessInfo(@Param("id") Long id);

    ApprovalDetailsVO approvalDetails(@Param("id") Long id);
}