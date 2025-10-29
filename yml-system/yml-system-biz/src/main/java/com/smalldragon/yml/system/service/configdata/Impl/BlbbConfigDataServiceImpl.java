package com.smalldragon.yml.system.service.configdata.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
        blbbVersionHistoryService.record(
                configDataDO.getId(),
                null,
                String.valueOf(configDataDO.getVersion()),
                "CREATE",
                "创建配置数据",
                configDataDO.getRowData(),
                configDataDO.getCreatedBy()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(Long id, String rowData, String changeDescription) {
        BlbbConfigDataDO configDataDO = blbbConfigDataMapper.selectById(id);
        if (configDataDO == null) {
            throw new RuntimeException("要修改的配置数据不存在!");
        }

        // 记录旧版本数据到历史表
        String operatedBy = UserContext.getLoginUsername();
        blbbVersionHistoryService.record(
                configDataDO.getId(),
                String.valueOf(configDataDO.getVersion()),
                String.valueOf(configDataDO.getVersion() + 1),
                "UPDATE",
                changeDescription,
                rowData,
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
    public Boolean deleteData(List<Long> ids) {
        for (Long id : ids) {
            BlbbConfigDataDO before = blbbConfigDataMapper.selectById(id);
            if (before != null) {
                blbbVersionHistoryService.record(
                        before.getId(),
                        String.valueOf(before.getVersion()),
                        null,
                        "DELETE",
                        "删除配置数据",
                        before.getRowData(),
                        UserContext.getLoginUsername()
                );
            }
        }
        blbbConfigDataMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbConfigDataVO getInfoById(Long id) {
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleActive(Long id, Boolean isActive) {
        BlbbConfigDataDO configDataDO = blbbConfigDataMapper.selectById(id);
        if (configDataDO == null) {
            throw new RuntimeException("配置数据不存在!");
        }
        
        configDataDO.setIsActive(isActive);
        blbbConfigDataMapper.updateById(configDataDO);
        return true;
    }
}
