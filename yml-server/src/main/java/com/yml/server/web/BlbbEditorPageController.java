package com.yml.server.web;

import com.smalldragon.yml.context.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class BlbbEditorPageController {

    @GetMapping("/blbb/editor")
    public String editor(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String username = null;
        
        if (session != null) {
            // 优先从username属性获取
            username = (String) session.getAttribute("username");
            
            // 如果username不存在，尝试从currentUser中获取
            if (username == null) {
                Object currentUser = session.getAttribute("currentUser");
                if (currentUser != null && currentUser instanceof LoginDTO) {
                    LoginDTO loginDTO = (LoginDTO) currentUser;
                    username = loginDTO.getUsername();
                }
            }
            
            if (username != null) {
                model.addAttribute("username", username);
                log.debug("Editor页面获取到用户名: {}", username);
            } else {
                log.warn("Editor页面未找到用户名信息，session id: {}", session.getId());
            }
        } else {
            log.warn("Editor页面session为空");
        }
        
        return "blbb-editor";
    }

    @GetMapping("/blbb/login")
    public String login() {
        return "login";
    }

    @GetMapping("/blbb/dict")
    public String dict(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String username = null;
        
        if (session != null) {
            // 优先从username属性获取
            username = (String) session.getAttribute("username");
            
            // 如果username不存在，尝试从currentUser中获取
            if (username == null) {
                Object currentUser = session.getAttribute("currentUser");
                if (currentUser != null && currentUser instanceof LoginDTO) {
                    LoginDTO loginDTO = (LoginDTO) currentUser;
                    username = loginDTO.getUsername();
                }
            }
            
            if (username != null) {
                model.addAttribute("username", username);
                log.debug("Dict页面获取到用户名: {}", username);
            } else {
                log.warn("Dict页面未找到用户名信息，session id: {}", session != null ? session.getId() : "null");
            }
        } else {
            log.warn("Dict页面session为空");
        }
        
        return "blbb-dict";
    }

    @GetMapping("/blbb/template")
    public String template(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String username = null;
        
        if (session != null) {
            // 优先从username属性获取
            username = (String) session.getAttribute("username");
            
            // 如果username不存在，尝试从currentUser中获取
            if (username == null) {
                Object currentUser = session.getAttribute("currentUser");
                if (currentUser != null && currentUser instanceof LoginDTO) {
                    LoginDTO loginDTO = (LoginDTO) currentUser;
                    username = loginDTO.getUsername();
                }
            }
            
            if (username != null) {
                model.addAttribute("username", username);
                log.debug("Template页面获取到用户名: {}", username);
            } else {
                log.warn("Template页面未找到用户名信息，session id: {}", session != null ? session.getId() : "null");
            }
        } else {
            log.warn("Template页面session为空");
        }
        
        return "blbb-template";
    }

    @GetMapping("/blbb/configdata")
    public String configData(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String username = null;
        
        if (session != null) {
            // 优先从username属性获取
            username = (String) session.getAttribute("username");
            
            // 如果username不存在，尝试从currentUser中获取
            if (username == null) {
                Object currentUser = session.getAttribute("currentUser");
                if (currentUser != null && currentUser instanceof LoginDTO) {
                    LoginDTO loginDTO = (LoginDTO) currentUser;
                    username = loginDTO.getUsername();
                }
            }
            
            if (username != null) {
                model.addAttribute("username", username);
                log.debug("ConfigData页面获取到用户名: {}", username);
            } else {
                log.warn("ConfigData页面未找到用户名信息，session id: {}", session != null ? session.getId() : "null");
            }
        } else {
            log.warn("ConfigData页面session为空");
        }
        
        return "blbb-configdata";
    }

    @GetMapping("/")
    public String root(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            return "redirect:/blbb/editor";
        }
        return "redirect:/blbb/login";
    }
    
    /**
     * 测试端点：检查session信息（仅用于调试）
     */
    @GetMapping("/blbb/debug/session")
    @org.springframework.web.bind.annotation.ResponseBody
    public String debugSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "Session is null";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Session ID: ").append(session.getId()).append("\n");
        sb.append("Username attribute: ").append(session.getAttribute("username")).append("\n");
        sb.append("CurrentUser attribute: ").append(session.getAttribute("currentUser")).append("\n");
        
        Object currentUser = session.getAttribute("currentUser");
        if (currentUser != null) {
            sb.append("CurrentUser type: ").append(currentUser.getClass().getName()).append("\n");
            if (currentUser instanceof LoginDTO) {
                LoginDTO loginDTO = (LoginDTO) currentUser;
                sb.append("CurrentUser username: ").append(loginDTO.getUsername()).append("\n");
            }
        }
        
        return sb.toString();
    }
}
