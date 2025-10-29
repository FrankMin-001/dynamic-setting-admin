package com.smalldragon.yml.system.service.versionhistory.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;
import com.smalldragon.yml.system.mapper.versionhistory.BlbbVersionHistoryMapper;
import com.smalldragon.yml.system.service.versionhistory.BlbbVersionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author YML
 * @Date 2025/1/15 12:35
 **/
@Service("BlbbVersionHistoryServiceImpl")
@RequiredArgsConstructor
public class BlbbVersionHistoryServiceImpl implements BlbbVersionHistoryService {

    private final BlbbVersionHistoryMapper blbbVersionHistoryMapper;

    @Override
    public IPage<BlbbVersionHistoryDO> pageList(int pageNo, int pageSize, Long configDataId) {
        Page<BlbbVersionHistoryDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<BlbbVersionHistoryDO> wrapper = new LambdaQueryWrapper<>();
        if (configDataId != null) {
            wrapper.eq(BlbbVersionHistoryDO::getConfigDataId, configDataId);
        }
        wrapper.orderByDesc(BlbbVersionHistoryDO::getId);
        return blbbVersionHistoryMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean record(Long configDataId, String oldVersion, String newVersion, String changeType, String changeDescription, String changeData, String operatedBy) {
        BlbbVersionHistoryDO history = new BlbbVersionHistoryDO();
        history.setConfigDataId(configDataId);
        history.setOldVersion(oldVersion);
        history.setNewVersion(newVersion);
        history.setChangeType(changeType);
        history.setChangeDescription(changeDescription);
        history.setChangeData(changeData);
        history.setOperatedBy(operatedBy);
        blbbVersionHistoryMapper.insert(history);
        return true;
    }
}


