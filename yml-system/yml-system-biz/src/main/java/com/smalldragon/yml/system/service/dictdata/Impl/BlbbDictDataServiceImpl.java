package com.smalldragon.yml.system.service.dictdata.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.system.dal.dictdata.BlbbDictDataDO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataCreateDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataPageDTO;
import com.smalldragon.yml.system.dal.dictdata.VO.BlbbDictDataVO;
import com.smalldragon.yml.system.mapper.dictdata.BlbbDictDataMapper;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import com.smalldragon.yml.system.service.dictdata.BlbbDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 12:10
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbDictDataServiceImpl")
public class BlbbDictDataServiceImpl implements BlbbDictDataService {

    private final BlbbDictDataMapper blbbDictDataMapper;
    private final BlbbDictHistoryService blbbDictHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbDictDataCreateDTO createDTO) {
        // 同一 dict_type + dict_code 不可重复
        LambdaQueryWrapper<BlbbDictDataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbDictDataDO::getDictType, createDTO.getDictType())
                .eq(BlbbDictDataDO::getDictCode, createDTO.getDictCode());
        BlbbDictDataDO exist = blbbDictDataMapper.selectOne(wrapper);
        if (exist != null) {
            throw new RuntimeException("字典编码已存在!");
        }

        BlbbDictDataDO dataDO = BeanUtil.copyProperties(createDTO, BlbbDictDataDO.class);
        dataDO.setCreatedBy(UserContext.getLoginUsername());
        if (dataDO.getStatus() == null) {
            dataDO.setStatus(1);
        }
        blbbDictDataMapper.insert(dataDO);
        // 历史-ADD
        blbbDictHistoryService.record(
                dataDO.getDictType(),
                "ADD",
                null,
                toJsonSafe(dataDO),
                dataDO.getCreatedBy()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(Long id, BlbbDictDataCreateDTO updateDTO) {
        BlbbDictDataDO db = blbbDictDataMapper.selectById(id);
        if (db == null) {
            throw new RuntimeException("字典数据不存在!");
        }
        // 校验唯一
        LambdaQueryWrapper<BlbbDictDataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbDictDataDO::getDictType, updateDTO.getDictType())
                .eq(BlbbDictDataDO::getDictCode, updateDTO.getDictCode())
                .ne(BlbbDictDataDO::getId, id);
        if (blbbDictDataMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("字典编码已存在!");
        }
        String oldJson = toJsonSafe(db);
        BeanUtil.copyProperties(updateDTO, db);
        blbbDictDataMapper.updateById(db);
        // 历史-UPDATE
        blbbDictHistoryService.record(
                db.getDictType(),
                "UPDATE",
                oldJson,
                toJsonSafe(db),
                UserContext.getLoginUsername()
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<Long> ids) {
        for (Long id : ids) {
            BlbbDictDataDO before = blbbDictDataMapper.selectById(id);
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
        blbbDictDataMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbDictDataVO getInfoById(Long id) {
        BlbbDictDataDO db = blbbDictDataMapper.selectById(id);
        if (db == null) {
            throw new RuntimeException("字典数据不存在!");
        }
        return BeanUtil.copyProperties(db, BlbbDictDataVO.class);
    }

    private String toJsonSafe(BlbbDictDataDO data) {
        try {
            // 简单拼接，避免引入额外依赖；生产建议统一JSON工具
            return "{\"dictType\":\"" + data.getDictType() + "\","
                    + "\"dictCode\":\"" + data.getDictCode() + "\","
                    + "\"dictValue\":\"" + data.getDictValue() + "\","
                    + "\"displayOrder\":" + (data.getDisplayOrder() == null ? "null" : data.getDisplayOrder()) + ","
                    + "\"status\":" + (data.getStatus() == null ? "null" : data.getStatus()) + ","
                    + "\"extendProps\":" + (data.getExtendProps() == null ? "null" : ("\"" + data.getExtendProps().replace("\"", "\\\"") + "\""))
                    + "}";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IPage<BlbbDictDataDO> pageList(BlbbDictDataPageDTO pageDTO) {
        Page<BlbbDictDataDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbDictDataDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            wrapper.like(BlbbDictDataDO::getDictCode, pageDTO.getKeywords())
                    .or()
                    .like(BlbbDictDataDO::getDictValue, pageDTO.getKeywords());
        }
        if (StrUtil.isNotBlank(pageDTO.getDictType())) {
            wrapper.eq(BlbbDictDataDO::getDictType, pageDTO.getDictType());
        }
        if (pageDTO.getStatus() != null) {
            wrapper.eq(BlbbDictDataDO::getStatus, pageDTO.getStatus());
        }
        wrapper.orderByAsc(BlbbDictDataDO::getDisplayOrder).orderByDesc(BlbbDictDataDO::getId);
        return blbbDictDataMapper.selectPage(page, wrapper);
    }

    @Override
    public List<BlbbDictDataVO> listByDictType(String dictType) {
        LambdaQueryWrapper<BlbbDictDataDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbDictDataDO::getDictType, dictType)
                .eq(BlbbDictDataDO::getStatus, 1)
                .orderByAsc(BlbbDictDataDO::getDisplayOrder);
        List<BlbbDictDataDO> list = blbbDictDataMapper.selectList(wrapper);
        return list.stream().map(it -> BeanUtil.copyProperties(it, BlbbDictDataVO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleStatus(Long id, Integer status) {
        BlbbDictDataDO db = blbbDictDataMapper.selectById(id);
        if (db == null) {
            throw new RuntimeException("字典数据不存在!");
        }
        db.setStatus(status);
        blbbDictDataMapper.updateById(db);
        return true;
    }
}


