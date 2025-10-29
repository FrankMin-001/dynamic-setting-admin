package com.smalldragon.yml.system.service.context;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.context.BlbbContextDO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextCreateDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextPageDTO;
import com.smalldragon.yml.system.dal.context.DTO.BlbbContextUpdateDTO;
import com.smalldragon.yml.system.dal.context.VO.BlbbContextVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbContextService {

    /**
     * @Description 添加上下文
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean insertData(BlbbContextCreateDTO createDTO);

    /**
     * @Description 修改上下文
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean updateData(BlbbContextUpdateDTO updateDTO);

    /**
     * @Description 删除上下文
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean deleteData(List<Long> ids);

    /**
     * @Description 获取上下文信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbContextVO getInfoById(Long id);

    /**
     * @Description 上下文分页查询
     * @Author YML
     * @Date 2025/1/15
     */
    IPage<BlbbContextDO> pageList(BlbbContextPageDTO pageDTO);

    /**
     * @Description 获取上下文树形结构
     * @Author YML
     * @Date 2025/1/15
     */
    List<BlbbContextVO> getContextTree();

    /**
     * @Description 根据路径获取上下文
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbContextDO getContextByPath(String contextPath);
}
