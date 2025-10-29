package com.smalldragon.yml.system.service.configdata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.configdata.BlbbConfigDataDO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataCreateDTO;
import com.smalldragon.yml.system.dal.configdata.DTO.BlbbConfigDataPageDTO;
import com.smalldragon.yml.system.dal.configdata.VO.BlbbConfigDataVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbConfigDataService {

    /**
     * @Description 添加配置数据
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean insertData(BlbbConfigDataCreateDTO createDTO);

    /**
     * @Description 修改配置数据
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean updateData(Long id, String rowData, String changeDescription);

    /**
     * @Description 删除配置数据
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean deleteData(List<Long> ids);

    /**
     * @Description 获取配置数据信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbConfigDataVO getInfoById(Long id);

    /**
     * @Description 配置数据分页查询
     * @Author YML
     * @Date 2025/1/15
     */
    IPage<BlbbConfigDataDO> pageList(BlbbConfigDataPageDTO pageDTO);

    /**
     * @Description 根据模板类型获取配置数据列表
     * @Author YML
     * @Date 2025/1/15
     */
    List<BlbbConfigDataVO> getConfigDataByTemplateType(String templateType);

    /**
     * @Description 激活/禁用配置数据
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean toggleActive(Long id, Boolean isActive);
}
