package com.smalldragon.yml.function.apiService;

import com.smalldragon.yml.function.api.ApiSimpleChatService;
import com.smalldragon.yml.function.service.SimpleChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author YML
 * @Date 2025/4/9 23:12
 **/
@Slf4j
@AllArgsConstructor
@Service("ApiSimpleChatServiceImpl")
public class ApiSimpleChatServiceImpl implements ApiSimpleChatService {

    private final SimpleChatService simpleChatService;


    @Override
    public Boolean hasRoom(String roomId) {
        return simpleChatService.isAlive(roomId);
    }

}
