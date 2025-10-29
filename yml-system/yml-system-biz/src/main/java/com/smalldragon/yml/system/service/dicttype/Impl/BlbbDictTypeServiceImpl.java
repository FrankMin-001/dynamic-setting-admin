package com.smalldragon.yml.system.service.dicttype.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.system.dal.dicttype.BlbbDictTypeDO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypeCreateDTO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypePageDTO;
import com.smalldragon.yml.system.dal.dicttype.VO.BlbbDictTypeVO;
import com.smalldragon.yml.system.mapper.dicttype.BlbbDictTypeMapper;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import com.smalldragon.yml.system.service.dicttype.BlbbDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 11:25
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbDictTypeServiceImpl")
public class BlbbDictTypeServiceImpl implements BlbbDictTypeService {

    private final BlbbDictTypeMapper blbbDictTypeMapper;
    private final BlbbDictHistoryService blbbDictHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbDictTypeCreateDTO createDTO) {
        // 校验字典类型是否已存在
        LambdaQueryWrapper<BlbbDictTypeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbDictTypeDO::getDictType, createDTO.getDictType());
        BlbbDictTypeDO existDictType = blbbDictTypeMapper.selectOne(queryWrapper);
        if (existDictType != null) {
            log.error("字典类型已存在: {}", createDTO.getDictType());
            throw new RuntimeException("字典类型已存在!");
        }

        BlbbDictTypeDO dictTypeDO = BeanUtil.copyProperties(createDTO, BlbbDictTypeDO.class);
        dictTypeDO.setCreatedBy(UserContext.getLoginUsername());
        blbbDictTypeMapper.insert(dictTypeDO);
        // 历史-ADD（记录新类型）
        blbbDictHistoryService.record(
                dictTypeDO.getDictType(),
                "ADD",
                null,
                toJsonSafe(dictTypeDO),
                dictTypeDO.getCreatedBy()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(Long id, BlbbDictTypeCreateDTO updateDTO) {
        BlbbDictTypeDO dictTypeDO = blbbDictTypeMapper.selectById(id);
        if (dictTypeDO == null) {
            throw new RuntimeException("要修改的字典类型信息不存在!");
        }

        // 如果字典类型发生变化，需要校验新类型是否已存在
        if (!dictTypeDO.getDictType().equals(updateDTO.getDictType())) {
            LambdaQueryWrapper<BlbbDictTypeDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlbbDictTypeDO::getDictType, updateDTO.getDictType());
            queryWrapper.ne(BlbbDictTypeDO::getId, id);
            BlbbDictTypeDO existDictType = blbbDictTypeMapper.selectOne(queryWrapper);
            if (existDictType != null) {
                log.error("字典类型已存在: {}", updateDTO.getDictType());
                throw new RuntimeException("字典类型已存在!");
            }
        }

        String oldJson = toJsonSafe(dictTypeDO);
        BeanUtil.copyProperties(updateDTO, dictTypeDO);
        blbbDictTypeMapper.updateById(dictTypeDO);
        // 历史-UPDATE
        blbbDictHistoryService.record(
                dictTypeDO.getDictType(),
                "UPDATE",
                oldJson,
                toJsonSafe(dictTypeDO),
                UserContext.getLoginUsername()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<Long> ids) {
        for (Long id : ids) {
            BlbbDictTypeDO before = blbbDictTypeMapper.selectById(id);
            if (before != null) {
                blbbDictHistoryService.record(
                        before.getDictType(),
                        "DELETE",
                        toJsonSafe(before),
                        null,
                        UserContext.getLoginUsername()
                );
            }
        }
        blbbDictTypeMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbDictTypeVO getInfoById(Long id) {
        BlbbDictTypeDO dictTypeDO = blbbDictTypeMapper.selectById(id);
        if (dictTypeDO == null) {
            throw new RuntimeException("字典类型信息不存在!");
        }
        return BeanUtil.copyProperties(dictTypeDO, BlbbDictTypeVO.class);
    }

    private String toJsonSafe(BlbbDictTypeDO data) {
        try {
            return "{\"dictType\":\"" + data.getDictType() + "\","
                    + "\"dictName\":\"" + data.getDictName() + "\","
                    + "\"description\":\"" + (data.getDescription() == null ? "" : data.getDescription().replace("\"", "\\\"")) + "\","
                    + "\"status\":" + (data.getStatus() == null ? "null" : data.getStatus())
                    + "}";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IPage<BlbbDictTypeDO> pageList(BlbbDictTypePageDTO pageDTO) {
        Page<BlbbDictTypeDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbDictTypeDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            queryWrapper.like(BlbbDictTypeDO::getDictType, pageDTO.getKeywords())
                    .or()
                    .like(BlbbDictTypeDO::getDictName, pageDTO.getKeywords())
                    .or()
                    .like(BlbbDictTypeDO::getDescription, pageDTO.getKeywords());
        }

        if (pageDTO.getStatus() != null) {
            queryWrapper.eq(BlbbDictTypeDO::getStatus, pageDTO.getStatus());
        }

        queryWrapper.orderByDesc(BlbbDictTypeDO::getId);
        return blbbDictTypeMapper.selectPage(page, queryWrapper);
    }

    @Override
    public BlbbDictTypeDO getDictTypeByType(String dictType) {
        LambdaQueryWrapper<BlbbDictTypeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbDictTypeDO::getDictType, dictType);
        return blbbDictTypeMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BlbbDictTypeVO> getAllEnabledDictTypes() {
        LambdaQueryWrapper<BlbbDictTypeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbDictTypeDO::getStatus, 1);
        queryWrapper.orderByAsc(BlbbDictTypeDO::getDictType);
        
        List<BlbbDictTypeDO> dictTypes = blbbDictTypeMapper.selectList(queryWrapper);
        return dictTypes.stream()
                .map(dictType -> BeanUtil.copyProperties(dictType, BlbbDictTypeVO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleStatus(Long id, Integer status) {
        BlbbDictTypeDO dictTypeDO = blbbDictTypeMapper.selectById(id);
        if (dictTypeDO == null) {
            throw new RuntimeException("字典类型不存在!");
        }
        
        dictTypeDO.setStatus(status);
        blbbDictTypeMapper.updateById(dictTypeDO);
        return true;
    }
}
