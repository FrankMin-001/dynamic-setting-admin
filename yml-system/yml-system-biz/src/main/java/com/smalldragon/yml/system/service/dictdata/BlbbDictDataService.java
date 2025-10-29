package com.smalldragon.yml.system.service.dictdata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.dictdata.BlbbDictDataDO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataCreateDTO;
import com.smalldragon.yml.system.dal.dictdata.DTO.BlbbDictDataPageDTO;
import com.smalldragon.yml.system.dal.dictdata.VO.BlbbDictDataVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbDictDataService {

    Boolean insertData(BlbbDictDataCreateDTO createDTO);

    Boolean updateData(Long id, BlbbDictDataCreateDTO updateDTO);

    Boolean deleteData(List<Long> ids);

    BlbbDictDataVO getInfoById(Long id);

    IPage<BlbbDictDataDO> pageList(BlbbDictDataPageDTO pageDTO);

    List<BlbbDictDataVO> listByDictType(String dictType);

    Boolean toggleStatus(Long id, Integer status);
}


