package com.smalldragon.yml.system.service.versionhistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.versionhistory.BlbbVersionHistoryDO;

/**
 * @author YML
 */
public interface BlbbVersionHistoryService {

    IPage<BlbbVersionHistoryDO> pageList(int pageNo, int pageSize, Long configDataId);

    Boolean record(Long configDataId, String oldVersion, String newVersion, String changeType, String changeDescription, String changeData, String operatedBy);
}


