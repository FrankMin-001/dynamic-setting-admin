package com.smalldragon.yml.system.service.resource;

import com.smalldragon.yml.system.dal.resource.DTO.MenuCreateDTO;
import com.smalldragon.yml.system.dal.resource.DTO.MenuUpdateDTO;

import java.util.List;

public interface ResourceService {
    /**
     * @Description  添加菜单
     * @Author YML
     * @Date 2025/3/23
     */
    Boolean addMenu(MenuCreateDTO createDTO);

    /**
     * @Description 删除菜单
     * @Author YML
     * @Date 2025/3/24
     */
    Boolean deleteMenu(List<String> ids);

    /**
     * @Description 修改菜单
     * @Author YML
     * @Date 2025/3/25
     */
    Boolean updateMenu(MenuUpdateDTO updateDTO);
}
