package cn.iocoder.yudao.module.biz.dal.mysql.devicelicense;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.devicelicense.DeviceLicenseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface DeviceLicenseMapper extends BaseMapperX<DeviceLicenseDO> {

    @Select("select count(*) from biz_device_license where  category_code = #{c} and step_type = #{s} and status = 'USED'")
    Integer countByProvinceAndCategoryAndStep( @Param("c") String category, @Param("s") String step);

    @Select("SELECT COUNT(*) FROM biz_device_license d WHERE  d.category_code = #{c} AND d.step_type = #{s} and status = 'USED'")
    Integer countByProvinceAndCategoryAndStepAndManufacturer(
            @Param("c") String category,
            @Param("s") String step);


    @Select("select * from biz_device_license where license_number = #{l}")
    DeviceLicenseDO findByLicenseNumber(@Param("l") String licenseNumber);

    @Select("""
        select * from biz_device_license d
        where d.category_code = #{c} AND d.step_type = #{s}
        AND d.status = 'GENERATED'
        """)
    List<DeviceLicenseDO> findReusableByProvinceCategoryStep(
            @Param("c") String categoryCode,
            @Param("s") String stepType);
}
