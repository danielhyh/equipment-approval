package cn.iocoder.yudao.module.biz.service.license;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.DuplicateLicenseVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageRequestVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.OriginalLicenseVO;
import cn.iocoder.yudao.module.biz.dal.mysql.license.LicenseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

    @Resource
    private LicenseMapper licenseMapper;


    public PageResult<LicensePageVO> page(LicensePageRequestVO param) {
        IPage<LicensePageVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        licenseMapper.page(page, param);
        return new PageResult<>(page.getRecords(),  page.getTotal());
    }

    public OriginalLicenseVO getOriginalById(Long id) {
        return licenseMapper.getOriginalById(id);
    }

    public DuplicateLicenseVO getDuplicateById(Long id) {
        return licenseMapper.getDuplicateById(id);
    }
}
