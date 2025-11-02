package com.smalldragon.yml.system.service.versionhistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbVersionHistoryService {

    IPage<BlbbVersionHistoryDO> pageList(int pageNo, int pageSize, String configDataId);

    IPage<BlbbVersionHistoryDO> pageListByConfigDataIds(int pageNo, int pageSize, List<String> configDataIds);

    Boolean record(String configDataId, String oldVersion, String newVersion, String changeType, String changeDescription, String changeData, String operatedBy);
}


