package com.smalldragon.yml.system.service.configdata.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.system.dal.configdata.BlbbConfigDataDO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataCreateDTO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataPageDTO;
import com.smalldragon.yml.system.dal.configdata.VO.BlbbConfigDataVO;
import com.smalldragon.yml.system.mapper.configdata.BlbbConfigDataMapper;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import com.smalldragon.yml.system.service.configdata.BlbbConfigDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 11:05
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbConfigDataServiceImpl")
public class BlbbConfigDataServiceImpl implements BlbbConfigDataService {

    private final BlbbConfigDataMapper blbbConfigDataMapper;
    private final BlbbVersionHistoryService blbbVersionHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbConfigDataCreateDTO createDTO) {
        BlbbConfigDataDO configDataDO = BeanUtil.copyProperties(createDTO, BlbbConfigDataDO.class);
        configDataDO.setCreatedBy(UserContext.getLoginUsername());
        if (configDataDO.getVersion() == null) {
            configDataDO.setVersion(1);
        }
        if (configDataDO.getIsActive() == null) {
            configDataDO.setIsActive(true);
        }
        blbbConfigDataMapper.insert(configDataDO);

        // 记录版本历史 - CREATE
        // 保存完整的数据快照（保持与其他操作的一致性）
        String dataSnapshot = JSONUtil.toJsonStr(configDataDO);
        blbbVersionHistoryService.record(
                configDataDO.getId(),
                null,
                String.valueOf(configDataDO.getVersion()),
                "CREATE",
                "创建配置数据",
                dataSnapshot,
                configDataDO.getCreatedBy()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(String id, String rowData, String changeDescription) {
        BlbbConfigDataDO configDataDO = blbbConfigDataMapper.selectById(id);
        if (configDataDO == null) {
            throw new RuntimeException("要修改的配置数据不存在!");
        }

        // 保存变更前的完整数据快照（用于版本历史记录）
        String oldDataSnapshot = JSONUtil.toJsonStr(configDataDO);
        
        // 记录旧版本数据到历史表
        String operatedBy = UserContext.getLoginUsername();
        blbbVersionHistoryService.record(
                configDataDO.getId(),
                String.valueOf(configDataDO.getVersion()),
                String.valueOf(configDataDO.getVersion() + 1),
                "UPDATE",
                StrUtil.isNotBlank(changeDescription) ? changeDescription : "更新配置数据",
                oldDataSnapshot,
                operatedBy
        );

        // 更新数据
        configDataDO.setRowData(rowData);
        configDataDO.setVersion(configDataDO.getVersion() + 1);
        blbbConfigDataMapper.updateById(configDataDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<String> ids) {
        for (String id : ids) {
            BlbbConfigDataDO before = blbbConfigDataMapper.selectById(id);
            if (before != null) {
                // 删除也需要记录 newVersion，避免数据库 NOT NULL 约束报错
                // 语义上：每次变更（含删除）版本 +1，用于历史记录追踪
                String operatedBy = UserContext.getLoginUsername();
                blbbVersionHistoryService.record(
                        before.getId(),
                        String.valueOf(before.getVersion()),
                        String.valueOf(before.getVersion() + 1),
                        "DELETE",
                        "删除配置数据",
                        cn.hutool.json.JSONUtil.toJsonStr(before),
                        operatedBy
                );
            }
        }
        blbbConfigDataMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbConfigDataVO getInfoById(String id) {
        BlbbConfigDataDO configDataDO = blbbConfigDataMapper.selectById(id);
        if (configDataDO == null) {
            throw new RuntimeException("配置数据不存在!");
        }
        return BeanUtil.copyProperties(configDataDO, BlbbConfigDataVO.class);
    }

    @Override
    public IPage<BlbbConfigDataDO> pageList(BlbbConfigDataPageDTO pageDTO) {
        Page<BlbbConfigDataDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbConfigDataDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            queryWrapper.like(BlbbConfigDataDO::getRowData, pageDTO.getKeywords());
        }

        if (StrUtil.isNotBlank(pageDTO.getTitleId())) {
            queryWrapper.eq(BlbbConfigDataDO::getTitleId, pageDTO.getTitleId());
        }

        if (StrUtil.isNotBlank(pageDTO.getTemplateType())) {
            queryWrapper.eq(BlbbConfigDataDO::getTemplateType, pageDTO.getTemplateType());
        }

        if (pageDTO.getIsActive() != null) {
            queryWrapper.eq(BlbbConfigDataDO::getIsActive, pageDTO.getIsActive());
        }

        queryWrapper.orderByAsc(BlbbConfigDataDO::getDisplayOrder)
                .orderByDesc(BlbbConfigDataDO::getId);
        return blbbConfigDataMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<BlbbConfigDataVO> getConfigDataByTemplateType(String templateType) {
        LambdaQueryWrapper<BlbbConfigDataDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbConfigDataDO::getTemplateType, templateType);
        queryWrapper.eq(BlbbConfigDataDO::getIsActive, true);
        queryWrapper.orderByAsc(BlbbConfigDataDO::getDisplayOrder);
        
        List<BlbbConfigDataDO> configDataList = blbbConfigDataMapper.selectList(queryWrapper);
        return configDataList.stream()
                .map(configData -> BeanUtil.copyProperties(configData, BlbbConfigDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BlbbConfigDataVO> getConfigDataByTitleId(String titleId) {
        LambdaQueryWrapper<BlbbConfigDataDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbConfigDataDO::getTitleId, titleId);
        queryWrapper.eq(BlbbConfigDataDO::getIsActive, true);
        queryWrapper.orderByAsc(BlbbConfigDataDO::getDisplayOrder);
        
        List<BlbbConfigDataDO> configDataList = blbbConfigDataMapper.selectList(queryWrapper);
        return configDataList.stream()
                .map(configData -> BeanUtil.copyProperties(configData, BlbbConfigDataVO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleActive(String id, Boolean isActive) {
        BlbbConfigDataDO configDataDO = blbbConfigDataMapper.selectById(id);
        if (configDataDO == null) {
            throw new RuntimeException("配置数据不存在!");
        }
        
        // 保存变更前的完整数据快照（用于版本历史记录）
        String oldDataSnapshot = JSONUtil.toJsonStr(configDataDO);
        
        // 记录版本历史 - UPDATE (激活状态变更)
        String operatedBy = UserContext.getLoginUsername();
        String changeDesc = isActive ? "激活配置数据" : "禁用配置数据";
        blbbVersionHistoryService.record(
                configDataDO.getId(),
                String.valueOf(configDataDO.getVersion()),
                String.valueOf(configDataDO.getVersion() + 1),
                "UPDATE",
                changeDesc,
                oldDataSnapshot,
                operatedBy
        );
        
        // 更新数据（包括版本号，因为这也算一次变更）
        configDataDO.setIsActive(isActive);
        configDataDO.setVersion(configDataDO.getVersion() + 1);
        blbbConfigDataMapper.updateById(configDataDO);
        return true;
    }
}
