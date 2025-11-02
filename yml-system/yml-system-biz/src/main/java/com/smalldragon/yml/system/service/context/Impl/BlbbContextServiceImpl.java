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
import com.smalldragon.yml.system.mapper.title.BlbbTitleMapper;
import com.smalldragon.yml.system.service.context.BlbbContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final BlbbTitleMapper blbbTitleMapper;

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
        
        // 将 String 类型的 parentId 转换为 Long
        if (StrUtil.isNotBlank(createDTO.getParentId())) {
            try {
                contextDO.setParentId(Long.parseLong(createDTO.getParentId()));
            } catch (NumberFormatException e) {
                log.error("parentId 格式错误: {}", createDTO.getParentId());
                throw new RuntimeException("父级上下文ID格式错误!");
            }
        } else {
            contextDO.setParentId(null);
        }
        
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
        
        // 将 String 类型的 parentId 转换为 Long
        if (StrUtil.isNotBlank(updateDTO.getParentId())) {
            try {
                contextDO.setParentId(Long.parseLong(updateDTO.getParentId()));
            } catch (NumberFormatException e) {
                log.error("parentId 格式错误: {}", updateDTO.getParentId());
                throw new RuntimeException("父级上下文ID格式错误!");
            }
        } else {
            contextDO.setParentId(null);
        }
        
        blbbContextMapper.updateById(contextDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteData(List<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return true;
        }

        // 收集所有要删除的context ID（包括子节点）
        Set<String> allContextIds = new HashSet<>();
        List<String> contextIdsToCheck = new ArrayList<>(ids);
        
        while (!contextIdsToCheck.isEmpty()) {
            String currentId = contextIdsToCheck.remove(0);
            if (allContextIds.contains(currentId)) {
                continue;
            }
            allContextIds.add(currentId);
            
            // 查找子节点
            Long currentIdAsLong = null;
            try {
                currentIdAsLong = Long.parseLong(currentId);
            } catch (NumberFormatException e) {
                log.warn("无法将id转换为Long: {}", currentId);
                continue;
            }
            
            LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BlbbContextDO::getParentId, currentIdAsLong);
            List<BlbbContextDO> children = blbbContextMapper.selectList(queryWrapper);
            
            if (CollUtil.isNotEmpty(children)) {
                for (BlbbContextDO child : children) {
                    if (!allContextIds.contains(child.getId())) {
                        contextIdsToCheck.add(child.getId());
                    }
                }
            }
        }
        
        // 检查是否有配置（hasConfig=true 或 有title数据）
        List<String> contextIdsList = new ArrayList<>(allContextIds);
        List<BlbbContextDO> contextsToCheck = blbbContextMapper.selectBatchIds(contextIdsList);
        
        List<String> contextNamesWithConfig = new ArrayList<>();
        for (BlbbContextDO context : contextsToCheck) {
            boolean hasConfigFlag = context.getHasConfig() != null && context.getHasConfig();
            boolean hasTitleData = false;
            
            // 检查是否有title数据
            LambdaQueryWrapper<com.smalldragon.yml.system.dal.title.BlbbTitleDO> titleWrapper = 
                new LambdaQueryWrapper<>();
            titleWrapper.eq(com.smalldragon.yml.system.dal.title.BlbbTitleDO::getContextId, context.getId());
            long titleCount = blbbTitleMapper.selectCount(titleWrapper);
            hasTitleData = titleCount > 0;
            
            if (hasConfigFlag || hasTitleData) {
                contextNamesWithConfig.add(context.getNodeName() + "(" + context.getContextPath() + ")");
            }
        }
        
        if (!contextNamesWithConfig.isEmpty()) {
            String errorMsg = "以下上下文或其子上下文存在配置，无法删除: " + 
                String.join(", ", contextNamesWithConfig);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        blbbContextMapper.deleteBatchIds(contextIdsList);
        return true;
    }

    @Override
    public BlbbContextVO getInfoById(String id) {
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

        if (StrUtil.isNotBlank(pageDTO.getParentId())) {
            try {
                Long parentId = Long.parseLong(pageDTO.getParentId());
                queryWrapper.eq(BlbbContextDO::getParentId, parentId);
            } catch (NumberFormatException e) {
                log.warn("parentId 格式错误: {}", pageDTO.getParentId());
            }
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
        log.info("开始查询上下文树形结构");
        List<BlbbContextDO> allContexts = blbbContextMapper.selectList(null);
        log.info("查询到上下文数量: {}", allContexts != null ? allContexts.size() : 0);
        if (allContexts != null && !allContexts.isEmpty()) {
            log.info("查询到的上下文数据: {}", allContexts);
        } else {
            log.warn("未查询到任何上下文数据，请检查数据库连接和表数据");
        }

        List<BlbbContextVO> contextVOs = allContexts.stream()
                .map(context -> BeanUtil.copyProperties(context, BlbbContextVO.class))
                .collect(Collectors.toList());

        // 构建树形结构
        List<BlbbContextVO> tree = buildTree(contextVOs);
        log.info("构建树形结构完成，根节点数量: {}", tree != null ? tree.size() : 0);
        return tree;
    }

    @Override
    public BlbbContextDO getContextByPath(String contextPath) {
        LambdaQueryWrapper<BlbbContextDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(BlbbContextDO::getContextPath, contextPath);
        return blbbContextMapper.selectOne(queryWrapper);
    }

    /**
     * 构建树形结构
     * 支持任意深度层级，使用Map优化性能，防止循环引用
     */
    private List<BlbbContextVO> buildTree(List<BlbbContextVO> allNodes) {
        if (CollUtil.isEmpty(allNodes)) {
            return new ArrayList<>();
        }

        // 构建父子关系映射表，key为parentId，value为该parentId下的所有子节点列表
        // 这样可以避免每次递归都遍历整个列表，时间复杂度从O(n²)降低到O(n)
        Map<Long, List<BlbbContextVO>> childrenMap = new HashMap<>();
        
        // 同时构建ID到节点的映射，用于循环引用检测
        Map<Long, BlbbContextVO> nodeMap = new HashMap<>();
        
        // 先初始化所有节点的children为空列表，并建立映射
        for (BlbbContextVO node : allNodes) {
            node.setChildren(new ArrayList<>());
            
            // 将String类型的ID转换为Long类型
            Long nodeIdAsLong = null;
            if (node.getId() != null) {
                try {
                    nodeIdAsLong = Long.parseLong(node.getId());
                    nodeMap.put(nodeIdAsLong, node);
                } catch (NumberFormatException e) {
                    log.warn("无法将id转换为Long: {}", node.getId());
                    continue;
                }
            }
            
            // 处理parentId，统一处理null和0的情况
            Long nodeParentId = node.getParentId();
            if (nodeParentId == null || nodeParentId == 0L) {
                nodeParentId = 0L; // 统一将根节点的parentId视为0
            }
            
            // 将节点添加到对应的父节点下
            childrenMap.computeIfAbsent(nodeParentId, k -> new ArrayList<>()).add(node);
        }
        
        // 检测循环引用
        Set<Long> visited = new HashSet<>();
        for (BlbbContextVO node : allNodes) {
            Long nodeId = null;
            try {
                if (node.getId() != null) {
                    nodeId = Long.parseLong(node.getId());
                    if (nodeId != null && !visited.contains(nodeId)) {
                        detectCycle(nodeId, nodeMap, visited, new HashSet<>());
                    }
                }
            } catch (NumberFormatException e) {
                // 忽略无法转换的ID
            }
        }
        
        // 从根节点开始构建树（parentId为0或null的节点）
        List<BlbbContextVO> rootNodes = childrenMap.getOrDefault(0L, new ArrayList<>());
        
        // 递归构建子树
        for (BlbbContextVO rootNode : rootNodes) {
            Long rootNodeId = null;
            try {
                if (rootNode.getId() != null) {
                    rootNodeId = Long.parseLong(rootNode.getId());
                    buildSubTree(rootNode, rootNodeId, childrenMap);
                }
            } catch (NumberFormatException e) {
                log.warn("根节点ID转换失败: {}", rootNode.getId());
            }
        }
        
        return rootNodes;
    }
    
    /**
     * 递归构建子树
     */
    private void buildSubTree(BlbbContextVO parentNode, Long parentNodeId, Map<Long, List<BlbbContextVO>> childrenMap) {
        // 获取当前节点的所有子节点
        List<BlbbContextVO> children = childrenMap.get(parentNodeId);
        if (CollUtil.isEmpty(children)) {
            parentNode.setChildren(new ArrayList<>());
            return;
        }
        
        // 为当前节点设置子节点列表
        parentNode.setChildren(children);
        
        // 递归处理每个子节点
        for (BlbbContextVO child : children) {
            Long childId = null;
            try {
                if (child.getId() != null) {
                    childId = Long.parseLong(child.getId());
                    if (childId != null) {
                        buildSubTree(child, childId, childrenMap);
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("子节点ID转换失败: {}", child.getId());
            }
        }
    }
    
    /**
     * 检测循环引用
     */
    private void detectCycle(Long nodeId, Map<Long, BlbbContextVO> nodeMap, Set<Long> visited, Set<Long> path) {
        if (path.contains(nodeId)) {
            log.error("检测到循环引用，节点ID: {}", nodeId);
            throw new RuntimeException("数据中存在循环引用，节点ID: " + nodeId);
        }
        
        if (visited.contains(nodeId)) {
            return;
        }
        
        visited.add(nodeId);
        path.add(nodeId);
        
        BlbbContextVO node = nodeMap.get(nodeId);
        if (node != null && node.getParentId() != null && node.getParentId() != 0L) {
            detectCycle(node.getParentId(), nodeMap, visited, path);
        }
        
        path.remove(nodeId);
    }
}
