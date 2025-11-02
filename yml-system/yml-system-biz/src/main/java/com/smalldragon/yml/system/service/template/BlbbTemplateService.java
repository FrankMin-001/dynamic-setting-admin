package com.smalldragon.yml.system.service.template;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smalldragon.yml.system.dal.template.BlbbTemplateDO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateCreateDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplatePageDTO;
import com.smalldragon.yml.system.dal.template.DTO.BlbbTemplateUpdateDTO;
import com.smalldragon.yml.system.dal.template.VO.BlbbTemplateVO;

import java.util.List;

/**
 * @author YML
 */
public interface BlbbTemplateService {

    /**
     * @Description 添加模板
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean insertData(BlbbTemplateCreateDTO createDTO);

    /**
     * @Description 修改模板
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean updateData(BlbbTemplateUpdateDTO updateDTO);

    /**
     * @Description 删除模板
     * @Author YML
     * @Date 2025/1/15
     */
    Boolean deleteData(List<String> ids);

    /**
     * @Description 获取模板信息
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbTemplateVO getInfoById(String id);

    /**
     * @Description 模板分页查询
     * @Author YML
     * @Date 2025/1/15
     */
    IPage<BlbbTemplateDO> pageList(BlbbTemplatePageDTO pageDTO);

    /**
     * @Description 根据模板类型获取模板
     * @Author YML
     * @Date 2025/1/15
     */
    BlbbTemplateDO getTemplateByType(String templateType);

    /**
     * @Description 获取所有模板列表
     * @Author YML
     * @Date 2025/1/15
     */
    List<BlbbTemplateVO> getAllTemplates();
}
