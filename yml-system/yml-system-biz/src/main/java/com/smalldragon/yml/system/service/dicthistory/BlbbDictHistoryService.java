package com.smalldragon.yml.system.service.dicthistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.dicthistory.BlbbDictHistoryDO;

/**
 * @author YML
 */
public interface BlbbDictHistoryService {

    IPage<BlbbDictHistoryDO> pageList(int pageNo, int pageSize, String dictType);

    Boolean record(String dictType, String operationType, String oldData, String newData, String operatedBy);
}


