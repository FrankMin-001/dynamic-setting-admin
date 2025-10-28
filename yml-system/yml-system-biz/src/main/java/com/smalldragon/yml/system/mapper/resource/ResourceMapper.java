package com.smalldragon.yml.system.mapper.resource;

import com.github.yulichang.base.MPJBaseMapper;
import com.smalldragon.yml.system.dal.resource.SysResourceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author YML
 */
@Mapper
public interface ResourceMapper extends MPJBaseMapper<SysResourceDO> {

    @Select("SELECT id FROM sys_resource")
    List<String> getAllId();

}
