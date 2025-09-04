package cn.iocoder.yudao.module.biz.dal.mysql.license;

import cn.iocoder.yudao.module.biz.controller.admin.license.vo.DuplicateLicenseVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageRequestVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.OriginalLicenseVO;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppDuplicateSubmitRequest;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppLicensePageRespVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LicenseMapper {

    IPage<LicensePageVO> page( IPage<LicensePageVO> page, @Param("p") LicensePageRequestVO requestVO);

    IPage<AppLicensePageRespVO> licensePage(IPage<AppLicensePageRespVO> page,@Param("type") String type, @Param("userId") Long id);

    OriginalLicenseVO getOriginalById(@Param("id") Long id);

    DuplicateLicenseVO getDuplicateById(@Param("id") Long id);

    int insertDuplicateLicense(@Param("vo") AppDuplicateSubmitRequest vo, @Param("creator") Long id);
}
