package com.smalldragon.yml.system.service.template.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.system.dal.template.BlbbTemplateDO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateCreateDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplatePageDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateUpdateDTO;
import com.smalldragon.yml.system.dal.template.VO.BlbbTemplateVO;
import com.smalldragon.yml.system.mapper.template.BlbbTemplateMapper;
import com.smalldragon.yml.system.service.template.BlbbTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 10:45
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbTemplateServiceImpl")
public class BlbbTemplateServiceImpl implements BlbbTemplateService {

    private final BlbbTemplateMapper blbbTemplateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbTemplateCreateDTO createDTO) {
        // 校验模板类型是否已存在
        LambdaQueryWrapper<BlbbTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbTemplateDO::getTemplateType, createDTO.getTemplateType());
        BlbbTemplateDO existTemplate = blbbTemplateMapper.selectOne(queryWrapper);
        if (existTemplate != null) {
            log.error("模板类型已存在: {}", createDTO.getTemplateType());
            throw new RuntimeException("模板类型已存在!");
        }

        // 校验模板名称是否已存在
        queryWrapper.clear();
        queryWrapper.eq(BlbbTemplateDO::getTemplateName, createDTO.getTemplateName());
        existTemplate = blbbTemplateMapper.selectOne(queryWrapper);
        if (existTemplate != null) {
            log.error("模板名称已存在: {}", createDTO.getTemplateName());
            throw new RuntimeException("模板名称已存在!");
        }

        BlbbTemplateDO templateDO = BeanUtil.copyProperties(createDTO, BlbbTemplateDO.class);
        blbbTemplateMapper.insert(templateDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(BlbbTemplateUpdateDTO updateDTO) {
        BlbbTemplateDO templateDO = blbbTemplateMapper.selectById(updateDTO.getId());
        if (templateDO == null) {
            throw new RuntimeException("要修改的模板信息不存在!");
        }

        // 如果模板类型发生变化，需要校验新类型是否已存在
        if (!templateDO.getTemplateType().equals(updateDTO.getTemplateType())) {
            LambdaQueryWrapper<BlbbTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlbbTemplateDO::getTemplateType, updateDTO.getTemplateType());
            queryWrapper.ne(BlbbTemplateDO::getId, updateDTO.getId());
            BlbbTemplateDO existTemplate = blbbTemplateMapper.selectOne(queryWrapper);
            if (existTemplate != null) {
                log.error("模板类型已存在: {}", updateDTO.getTemplateType());
                throw new RuntimeException("模板类型已存在!");
            }
        }

        // 如果模板名称发生变化，需要校验新名称是否已存在
        if (!templateDO.getTemplateName().equals(updateDTO.getTemplateName())) {
            LambdaQueryWrapper<BlbbTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlbbTemplateDO::getTemplateName, updateDTO.getTemplateName());
            queryWrapper.ne(BlbbTemplateDO::getId, updateDTO.getId());
            BlbbTemplateDO existTemplate = blbbTemplateMapper.selectOne(queryWrapper);
            if (existTemplate != null) {
                log.error("模板名称已存在: {}", updateDTO.getTemplateName());
                throw new RuntimeException("模板名称已存在!");
            }
        }

        BeanUtil.copyProperties(updateDTO, templateDO);
        blbbTemplateMapper.updateById(templateDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<String> ids) {
        blbbTemplateMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbTemplateVO getInfoById(String id) {
        BlbbTemplateDO templateDO = blbbTemplateMapper.selectById(id);
        if (templateDO == null) {
            throw new RuntimeException("模板信息不存在!");
        }
        return BeanUtil.copyProperties(templateDO, BlbbTemplateVO.class);
    }

    @Override
    public IPage<BlbbTemplateDO> pageList(BlbbTemplatePageDTO pageDTO) {
        Page<BlbbTemplateDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbTemplateDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            // 忽略大小写搜索模板类型和模板名称
            queryWrapper.and(wrapper -> wrapper
                    .apply("UPPER(template_type) LIKE UPPER({0})", "%" + pageDTO.getKeywords() + "%")
                    .or()
                    .apply("UPPER(template_name) LIKE UPPER({0})", "%" + pageDTO.getKeywords() + "%"));
        }

        queryWrapper.orderByDesc(BlbbTemplateDO::getId);
	    Page<BlbbTemplateDO> blbbTemplateDOPage = blbbTemplateMapper.selectPage(page, queryWrapper);
	    for (BlbbTemplateDO record : blbbTemplateDOPage.getRecords()) {
		    log.info(JSONUtil.toJsonStr(record));
	    }
	    return blbbTemplateDOPage;
    }

    @Override
    public BlbbTemplateDO getTemplateByType(String templateType) {
        LambdaQueryWrapper<BlbbTemplateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbTemplateDO::getTemplateType, templateType);
        return blbbTemplateMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BlbbTemplateVO> getAllTemplates() {
        List<BlbbTemplateDO> templates = blbbTemplateMapper.selectList(null);
        return templates.stream()
                .map(template -> BeanUtil.copyProperties(template, BlbbTemplateVO.class))
                .collect(Collectors.toList());
    }
}
