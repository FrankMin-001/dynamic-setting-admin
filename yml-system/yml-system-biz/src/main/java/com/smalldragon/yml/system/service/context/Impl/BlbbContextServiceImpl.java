package com.smalldragon.yml.system.service.context.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.context.UserContext;
import com.smalldragon.yml.system.dal.context.BlbbContextDO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextCreateDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextPageDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextUpdateDTO;
import com.smalldragon.yml.system.dal.context.VO.BlbbContextVO;
import com.smalldragon.yml.system.mapper.context.BlbbContextMapper;
import com.smalldragon.yml.system.service.context.BlbbContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author YML
 * @Date 2025/1/15 10:20
 **/
@Slf4j
@RequiredArgsConstructor
@Service("BlbbContextServiceImpl")
public class BlbbContextServiceImpl implements BlbbContextService {

    private final BlbbContextMapper blbbContextMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertData(BlbbContextCreateDTO createDTO) {
        // 校验路径是否已存在
        LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbContextDO::getContextPath, createDTO.getContextPath());
        BlbbContextDO existContext = blbbContextMapper.selectOne(queryWrapper);
        if (existContext != null) {
            log.error("上下文路径已存在: {}", createDTO.getContextPath());
            throw new RuntimeException("上下文路径已存在!");
        }

        BlbbContextDO contextDO = BeanUtil.copyProperties(createDTO, BlbbContextDO.class);
        contextDO.setCreatedBy(UserContext.getLoginUsername());
        blbbContextMapper.insert(contextDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateData(BlbbContextUpdateDTO updateDTO) {
        BlbbContextDO contextDO = blbbContextMapper.selectById(updateDTO.getId());
        if (contextDO == null) {
            throw new RuntimeException("要修改的上下文信息不存在!");
        }

        // 如果路径发生变化，需要校验新路径是否已存在
        if (!contextDO.getContextPath().equals(updateDTO.getContextPath())) {
            LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlbbContextDO::getContextPath, updateDTO.getContextPath());
            queryWrapper.ne(BlbbContextDO::getId, updateDTO.getId());
            BlbbContextDO existContext = blbbContextMapper.selectOne(queryWrapper);
            if (existContext != null) {
                log.error("上下文路径已存在: {}", updateDTO.getContextPath());
                throw new RuntimeException("上下文路径已存在!");
            }
        }

        BeanUtil.copyProperties(updateDTO, contextDO);
        blbbContextMapper.updateById(contextDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<Long> ids) {
        // 检查是否有子节点
        LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BlbbContextDO::getParentId, ids);
        List<BlbbContextDO> children = blbbContextMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(children)) {
            throw new RuntimeException("存在子节点，无法删除!");
        }

        blbbContextMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public BlbbContextVO getInfoById(Long id) {
        BlbbContextDO contextDO = blbbContextMapper.selectById(id);
        if (contextDO == null) {
            throw new RuntimeException("上下文信息不存在!");
        }
        return BeanUtil.copyProperties(contextDO, BlbbContextVO.class);
    }

    @Override
    public IPage<BlbbContextDO> pageList(BlbbContextPageDTO pageDTO) {
        Page<BlbbContextDO> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(pageDTO.getKeywords())) {
            queryWrapper.like(BlbbContextDO::getContextPath, pageDTO.getKeywords())
                    .or()
                    .like(BlbbContextDO::getNodeName, pageDTO.getKeywords())
                    .or()
                    .like(BlbbContextDO::getDescription, pageDTO.getKeywords());
        }

        if (pageDTO.getParentId() != null) {
            queryWrapper.eq(BlbbContextDO::getParentId, pageDTO.getParentId());
        }

        if (pageDTO.getNodeLevel() != null) {
            queryWrapper.eq(BlbbContextDO::getNodeLevel, pageDTO.getNodeLevel());
        }

        if (pageDTO.getHasConfig() != null) {
            queryWrapper.eq(BlbbContextDO::getHasConfig, pageDTO.getHasConfig());
        }

        queryWrapper.orderByAsc(BlbbContextDO::getNodeLevel)
                .orderByAsc(BlbbContextDO::getContextPath);
        return blbbContextMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<BlbbContextVO> getContextTree() {
        // 查询所有上下文
        List<BlbbContextDO> allContexts = blbbContextMapper.selectList(null);
        List<BlbbContextVO> contextVOs = allContexts.stream()
                .map(context -> BeanUtil.copyProperties(context, BlbbContextVO.class))
                .collect(Collectors.toList());

        // 构建树形结构
        return buildTree(contextVOs, null);
    }

    @Override
    public BlbbContextDO getContextByPath(String contextPath) {
        LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlbbContextDO::getContextPath, contextPath);
        return blbbContextMapper.selectOne(queryWrapper);
    }

    /**
     * 构建树形结构
     */
    private List<BlbbContextVO> buildTree(List<BlbbContextVO> allNodes, Long parentId) {
        return allNodes.stream()
                .filter(node -> (parentId == null && node.getParentId() == null) || 
                               (parentId != null && parentId.equals(node.getParentId())))
                .peek(node -> {
                    List<BlbbContextVO> children = buildTree(allNodes, node.getId());
                    node.setChildren(children);
                })
                .collect(Collectors.toList());
    }
}
