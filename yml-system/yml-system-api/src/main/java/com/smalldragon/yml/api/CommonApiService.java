package com.smalldragon.yml.api;

import java.util.List;

public interface CommonApiService {

    /**
     * @Description 通过角色获取 resourceVO 对象
     * @Author YML
     * @Date 2025/3/25
     */
    List<String> getFunctionDataByRoles(String roles);

}
