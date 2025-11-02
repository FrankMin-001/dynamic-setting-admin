package com.smalldragon.yml.system.service.title;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.title.BlbbTitleDO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleCreateDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitlePageDTO;
import com.smalldragon.yml.system.dal.title.DTO.BlbbTitleUpdateDTO;
import com.smalldragon.yml.system.dal.title.VO.BlbbTitleVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbTitleService {

    Boolean insertData(BlbbTitleCreateDTO createDTO);

    Boolean updateData(BlbbTitleUpdateDTO updateDTO);

    Boolean deleteData(List<String> ids);

    BlbbTitleVO getInfoById(String id);

    IPage<BlbbTitleDO> pageList(BlbbTitlePageDTO pageDTO);

    List<BlbbTitleVO> listByContextId(String contextId);

    BlbbTitleDO getByTitleKey(String titleKey);
}


