package com.smalldragon.yml.system.service.dicttype;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.dicttype.BlbbDictTypeDO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypeCreateDTO;
import com.smalldragon.yml.system.dal.dicttype.DTO.BlbbDictTypePageDTO;
import com.smalldragon.yml.system.dal.dicttype.VO.BlbbDictTypeVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbDictTypeService {

    /**
     * @Description 添加字典类型
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean insertData(BlbbDictTypeCreateDTO createDTO);

    /**
     * @Description 修改字典类型
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean updateData(Long id, BlbbDictTypeCreateDTO updateDTO);

    /**
     * @Description 删除字典类型
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean deleteData(List<Long> ids);

    /**
     * @Description 获取字典类型信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbDictTypeVO getInfoById(Long id);

    /**
     * @Description 字典类型分页查询
     * @Author YML
     * @Date 2025/1/15
     */
    IPage<BlbbDictTypeDO> pageList(BlbbDictTypePageDTO pageDTO);

    /**
     * @Description 根据字典类型获取字典类型信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbDictTypeDO getDictTypeByType(String dictType);

    /**
     * @Description 获取所有启用的字典类型列表
     * @Author YML
     * @Date 2025/1/15
     */
    List<BlbbDictTypeVO> getAllEnabledDictTypes();

    /**
     * @Description 启用/禁用字典类型
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean toggleStatus(Long id, Integer status);
}
