package com.smalldragon.yml.system.service.title.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.system.dal.title.BlbbTitleDO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleCreateDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitlePageDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleUpdateDTO;
import com.smalldragon.yml.system.dal.title.VO.BlbbTitleVO;
import com.smalldragon.yml.system.mapper.title.BlbbTitleMapper;
import com.smalldragon.yml.system.service.title.BlbbTitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 12:58
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbTitleServiceImpl")
public class BlbbTitleServiceImpl implements BlbbTitleService {

    private final BlbbTitleMapper blbbTitleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbTitleCreateDTO createDTO) {
        // 同一 context 下 title_key 唯一
        LambdaQueryWrapper<BlbbTitleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbTitleDO::getContextId, createDTO.getContextId())
                .eq(BlbbTitleDO::getTitleKey, createDTO.getTitleKey());
        if (blbbTitleMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("同一上下文下标题键名已存在!");
        }
        BlbbTitleDO titleDO = BeanUtil.copyProperties(createDTO, BlbbTitleDO.class);
        blbbTitleMapper.insert(titleDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(BlbbTitleUpdateDTO updateDTO) {
        BlbbTitleDO db = blbbTitleMapper.selectById(updateDTO.getId());
        if (db == null) {
            throw new RuntimeException("标题不存在!");
        }
        // 唯一性校验
        LambdaQueryWrapper<BlbbTitleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbTitleDO::getContextId, updateDTO.getContextId())
                .eq(BlbbTitleDO::getTitleKey, updateDTO.getTitleKey())
                .ne(BlbbTitleDO::getId, updateDTO.getId());
        if (blbbTitleMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("同一上下文下标题键名已存在!");
        }
        BeanUtil.copyProperties(updateDTO, db);
        blbbTitleMapper.updateById(db);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<Long> ids) {
        blbbTitleMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbTitleVO getInfoById(Long id) {
        BlbbTitleDO db = blbbTitleMapper.selectById(id);
        if (db == null) {
            throw new RuntimeException("标题不存在!");
        }
        return BeanUtil.copyProperties(db, BlbbTitleVO.class);
    }

    @Override
    public IPage<BlbbTitleDO> pageList(BlbbTitlePageDTO pageDTO) {
        Page<BlbbTitleDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbTitleDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            wrapper.like(BlbbTitleDO::getTitleName, pageDTO.getKeywords())
                    .or()
                    .like(BlbbTitleDO::getTitleKey, pageDTO.getKeywords());
        }
        if (pageDTO.getContextId() != null) {
            wrapper.eq(BlbbTitleDO::getContextId, pageDTO.getContextId());
        }
        if (pageDTO.getTemplateId() != null) {
            wrapper.eq(BlbbTitleDO::getTemplateId, pageDTO.getTemplateId());
        }
        wrapper.orderByAsc(BlbbTitleDO::getDisplayOrder).orderByDesc(BlbbTitleDO::getId);
        return blbbTitleMapper.selectPage(page, wrapper);
    }

    @Override
    public List<BlbbTitleVO> listByContextId(Long contextId) {
        LambdaQueryWrapper<BlbbTitleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbTitleDO::getContextId, contextId)
                .orderByAsc(BlbbTitleDO::getDisplayOrder);
        List<BlbbTitleDO> list = blbbTitleMapper.selectList(wrapper);
        return list.stream().map(it -> BeanUtil.copyProperties(it, BlbbTitleVO.class)).collect(Collectors.toList());
    }

    @Override
    public BlbbTitleDO getByTitleKey(String titleKey) {
        LambdaQueryWrapper<BlbbTitleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlbbTitleDO::getTitleKey, titleKey);
        return blbbTitleMapper.selectOne(wrapper);
    }
}


