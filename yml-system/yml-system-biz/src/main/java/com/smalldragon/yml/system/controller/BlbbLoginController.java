package com.smalldragon.yml.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.exception.NotLoginException;
import com.smalldragon.yml.system.dal.useraccount.BlbbUserAccountDO;
import com.smalldragon.yml.system.service.useraccount.BlbbUserAccountService;
import com.smalldragon.yml.system.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Blbb 登录控制器
 * @Author YML
 * @Date 2025/10/29 16:32
 **/
@Slf4j
@RestController
@RequestMapping("/api/blbb/auth")
@RequiredArgsConstructor
@Api(tags = "Blbb 登录管理")
public class BlbbLoginController {

    private final BlbbUserAccountService blbbUserAccountService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param request HttpServletRequest
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public ResponseEntity<?> login(@RequestParam String username,
                                  @RequestParam String password,
                                  HttpServletRequest request) {
        try {
            // 验证用户名密码
            BlbbUserAccountDO userAccount = blbbUserAccountService.getUserAccountByUsername(username);
            if (userAccount == null) {
                return ResponseEntity.badRequest().body("用户名或密码错误");
            }

            // 验证密码
            if (!PasswordUtil.matches(password, userAccount.getPassword())) {
                return ResponseEntity.badRequest().body("用户名或密码错误");
            }

            // 登录成功，保存用户信息到 session
            HttpSession session = request.getSession(true);
            LoginDTO loginDTO = BeanUtil.copyProperties(userAccount, LoginDTO.class);
            session.setAttribute("currentUser", loginDTO);
            session.setAttribute("userId", userAccount.getId());
            session.setAttribute("username", userAccount.getUsername());

            // 设置 session 过期时间（30分钟）
            session.setMaxInactiveInterval( 30 * 60);

            log.info("用户登录成功: {}, Session ID: {}, Session过期时间: {}秒",
                    username, session.getId(), session.getMaxInactiveInterval());
            return ResponseEntity.ok().body("登录成功");
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户登出
     * @param request HttpServletRequest
     * @return 登出结果
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                session.invalidate();
                log.info("用户登出成功: {}", username);
            }
            return ResponseEntity.ok().body("登出成功");
        } catch (Exception e) {
            log.error("登出失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("登出失败: " + e.getMessage());
        }
    }

    /**
     * 检查登录状态
     * @param request HttpServletRequest
     * @return 登录状态信息
     */
    @GetMapping("/status")
    @ApiOperation(value = "检查登录状态")
    public ResponseEntity<?> checkLoginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            LoginDTO user = (LoginDTO) session.getAttribute("currentUser");
            return ResponseEntity.ok().body("已登录，用户: " + user.getUsername());
        }
        return ResponseEntity.ok().body("未登录");
    }

}
