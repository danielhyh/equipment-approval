package cn.iocoder.yudao.module.biz.service.acceptancematerial;

import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.acceptancematerial.AcceptanceMaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 验收资料 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AcceptanceMaterialServiceImpl implements AcceptanceMaterialService {

    @Resource
    private AcceptanceMaterialMapper acceptanceMaterialMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public Boolean createAcceptanceMaterial(List<AppAcceptanceMaterialSaveReqVO> createReqVO) {
        // 插入
        List<AcceptanceMaterialDO> acceptanceMaterial = BeanUtils.toBean(createReqVO, AcceptanceMaterialDO.class);
        acceptanceMaterial.forEach(obj -> {
            obj.setUploadTime(LocalDateTime.now());
            obj.setStatus("待审核");
        });
        boolean hasId = createReqVO.stream().anyMatch(obj -> obj.getId() != null);
        Long applicationId = createReqVO.stream().map(AppAcceptanceMaterialSaveReqVO::getApplicationId).filter(Objects::nonNull).findFirst().orElse(null);
        //如果id不为空 即为重新上传
        if (applicationId != null && hasId) {
            //修改设备验收资料为待审批
            String querySql = """
                    select b.id from biz_license_original a
                    left join biz_license_duplicate b on a.id = b.original_id
                    where a.application_id = ?
                    """;
            String updateSql = """
                    update biz_license_duplicate set extra = null where id = :id
                    """;
            jdbcClient.sql(querySql).param(applicationId).query(Long.class).optional()
                    .map(duplicateId -> jdbcClient.sql(updateSql).param("id", duplicateId).update());
        }
        // 返回
        List<BatchResult> batchResults = acceptanceMaterialMapper.insertOrUpdate(acceptanceMaterial);
        return batchResults.stream().flatMapToInt(rs -> IntStream.of(rs.getUpdateCounts())).allMatch(count -> count > 0);
    }

    @Override
    public void updateAcceptanceMaterial(AppAcceptanceMaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateAcceptanceMaterialExists(updateReqVO.getId());
        // 更新
        AcceptanceMaterialDO updateObj = BeanUtils.toBean(updateReqVO, AcceptanceMaterialDO.class);
        acceptanceMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteAcceptanceMaterial(Long id) {
        // 校验存在
        validateAcceptanceMaterialExists(id);
        // 删除
        acceptanceMaterialMapper.deleteById(id);
    }

    @Override
        public void deleteAcceptanceMaterialListByIds(List<Long> ids) {
        // 删除
        acceptanceMaterialMapper.deleteByIds(ids);
        }


    private void validateAcceptanceMaterialExists(Long id) {
        if (acceptanceMaterialMapper.selectById(id) == null) {
            throw exception(ACCEPTANCE_MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public AcceptanceMaterialDO getAcceptanceMaterial(Long id) {
        return acceptanceMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<AcceptanceMaterialDO> getAcceptanceMaterialPage(AppAcceptanceMaterialPageReqVO pageReqVO) {
        return acceptanceMaterialMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AcceptanceMaterialDO> list(Long  id) {
        //Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        LambdaQueryWrapper<AcceptanceMaterialDO> wrapper = Wrappers.lambdaQuery(AcceptanceMaterialDO.class)
                .eq( AcceptanceMaterialDO::getApplicationId, id);
        return acceptanceMaterialMapper.selectList(wrapper);
    }

}