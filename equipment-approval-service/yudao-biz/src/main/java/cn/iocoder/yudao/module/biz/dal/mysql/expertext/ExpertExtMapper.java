package cn.iocoder.yudao.module.biz.dal.mysql.expertext;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.expertext.ExpertExtDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.expertext.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 专家扩展信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ExpertExtMapper extends BaseMapperX<ExpertExtDO> {

    IPage<ExpertExtRespVO> page(IPage<ExpertExtRespVO> page,@Param("reqVO") ExpertExtPageReqVO reqVO);

    List<ExpertExtRespVO> list(@Param("keyword")String keywords, @Param("specialty") String specialty);

    @Select("select specialty from biz_expert_ext where deleted = 0")
    List<String> getSpecialty();

    List<ExpertReviewVO> selectReviewRecord(@Param("id") Long id);

}