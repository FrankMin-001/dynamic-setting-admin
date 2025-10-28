package com.smalldragon.yml.system.apiService;

import cn.hutool.core.util.StrUtil;
import com.smalldragon.yml.api.CommonApiService;
import com.smalldragon.yml.system.dal.resource.VO.ResourceSetVO;
import com.smalldragon.yml.system.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author YML
 * @Date 2025/3/26 0:11
 **/
@Slf4j
@RequiredArgsConstructor
@Service("CommonApiServiceImpl")
public class CommonApiServiceImpl implements CommonApiService {

    private final CommonService commonService;

    @Override
    public List<String> getFunctionDataByRoles(String roles) {

        if (StrUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }

        ResourceSetVO resourceVoByRoles = commonService.getResourceVoByRoles(roles);
        if (resourceVoByRoles == null || resourceVoByRoles.getFunctionSet() == null) {
            return Collections.emptyList();
        }

        // 确保返回的是不可变列表（避免外部修改影响内部数据）
        return Collections.unmodifiableList(
                new ArrayList<>(resourceVoByRoles.getFunctionSet())
        );

    }

}
