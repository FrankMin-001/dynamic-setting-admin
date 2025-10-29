package com.smalldragon.yml.system.service.dicthistory.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.system.dal.dicthistory.BlbbDictHistoryDO;
import com.smalldragon.yml.system.mapper.dicthistory.BlbbDictHistoryMapper;
import com.smalldragon.yml.system.service.dicthistory.BlbbDictHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author YML
 * @Date 2025/1/15 12:25
 **/
@Service("BlbbDictHistoryServiceImpl")
@RequiredArgsConstructor
public class BlbbDictHistoryServiceImpl implements BlbbDictHistoryService {

    private final BlbbDictHistoryMapper blbbDictHistoryMapper;

    @Override
    public IPage<BlbbDictHistoryDO> pageList(int pageNo, int pageSize, String dictType) {
        Page<BlbbDictHistoryDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<BlbbDictHistoryDO> wrapper = new LambdaQueryWrapper<>();
        if (dictType != null && !dictType.isEmpty()) {
            wrapper.eq(BlbbDictHistoryDO::getDictType, dictType);
        }
        wrapper.orderByDesc(BlbbDictHistoryDO::getId);
        return blbbDictHistoryMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean record(String dictType, String operationType, String oldData, String newData, String operatedBy) {
        BlbbDictHistoryDO history = new BlbbDictHistoryDO();
        history.setDictType(dictType);
        history.setOperationType(operationType);
        history.setOldData(oldData);
        history.setNewData(newData);
        history.setOperatedBy(operatedBy);
        blbbDictHistoryMapper.insert(history);
        return true;
    }
}


