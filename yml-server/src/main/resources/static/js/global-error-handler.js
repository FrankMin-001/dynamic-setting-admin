/**
 * 全局AJAX错误处理和弹框组件
 * 功能：
 * 1. 统一处理所有AJAX请求错误
 * 2. 401未登录时自动跳转登录页
 * 3. 其他错误用美观弹框提示
 */

(function($) {
    'use strict';

    // 登录页面路径
    const LOGIN_URL = '/blbb/login';

    /**
     * 弹框组件
     */
    const MessageBox = {
        // 创建弹框HTML
        createModal: function(type, title, message) {
            const icons = {
                error: '❌',
                warning: '⚠️',
                info: 'ℹ️',
                success: '✅'
            };

            const colors = {
                error: '#f56565',
                warning: '#ed8936',
                info: '#4299e1',
                success: '#48bb78'
            };

            const icon = icons[type] || icons.info;
            const color = colors[type] || colors.info;

            return `
                <div class="message-box-overlay">
                    <div class="message-box" style="border-top-color: ${color}">
                        <div class="message-box-icon" style="color: ${color}">${icon}</div>
                        <div class="message-box-content">
                            <div class="message-box-title">${title}</div>
                            <div class="message-box-message">${message}</div>
                        </div>
                        <button class="message-box-close">确定</button>
                    </div>
                </div>
            `;
        },

        // 显示弹框
        show: function(type, title, message, callback) {
            // 移除已存在的弹框
            $('.message-box-overlay').remove();

            const html = this.createModal(type, title, message);
            $('body').append(html);

            const $overlay = $('.message-box-overlay');
            const $box = $('.message-box');

            // 动画效果
            $overlay.fadeIn(200);
            $box.css({
                'opacity': '0',
                'transform': 'translate(-50%, -50%) scale(0.8)'
            }).animate({
                'opacity': '1'
            }, 200).css('transform', 'translate(-50%, -50%) scale(1)');

            // 点击关闭按钮或遮罩层关闭
            $('.message-box-close, .message-box-overlay').on('click', function(e) {
                if (e.target === this) {
                    $box.animate({
                        'opacity': '0'
                    }, 150, function() {
                        $overlay.fadeOut(150, function() {
                            $(this).remove();
                            if (typeof callback === 'function') {
                                callback();
                            }
                        });
                    });
                }
            });

            // ESC键关闭
            $(document).on('keydown.messagebox', function(e) {
                if (e.key === 'Escape' || e.keyCode === 27) {
                    $('.message-box-close').trigger('click');
                    $(document).off('keydown.messagebox');
                }
            });
        },

        // 快捷方法
        error: function(message, title, callback) {
            this.show('error', title || '错误', message, callback);
        },

        warning: function(message, title, callback) {
            this.show('warning', title || '警告', message, callback);
        },

        info: function(message, title, callback) {
            this.show('info', title || '提示', message, callback);
        },

        success: function(message, title, callback) {
            this.show('success', title || '成功', message, callback);
        }
    };

    /**
     * 解析错误消息
     */
    function parseErrorMessage(xhr) {
        let message = null;

        try {
            // 尝试解析JSON响应
            if (xhr.responseJSON) {
                message = xhr.responseJSON.message || xhr.responseJSON.msg || null;
            } 
            // 尝试解析文本响应
            else if (xhr.responseText) {
                const text = xhr.responseText.trim();
                // 如果是JSON字符串，尝试解析
                if (text.startsWith('{')) {
                    try {
                        const json = JSON.parse(text);
                        message = json.message || json.msg || null;
                    } catch (e) {
                        // JSON解析失败，忽略
                    }
                } else if (text.length < 500) {
                    // 如果文本较短，直接使用（排除HTML页面）
                    if (!text.startsWith('<') && !text.startsWith('<!')) {
                        message = text || null;
                    }
                }
            }
        } catch (e) {
            // 解析失败，忽略，使用默认消息
            console.warn('解析错误消息失败:', e);
        }

        // 根据HTTP状态码设置默认消息
        if (xhr.status === 0) {
            message = message || '网络连接失败，请检查网络';
        } else if (xhr.status === 400) {
            message = message || '请求参数错误';
        } else if (xhr.status === 401) {
            message = message || '未登录或登录已过期';
        } else if (xhr.status === 403) {
            message = message || '没有权限访问';
        } else if (xhr.status === 404) {
            message = message || '请求的资源不存在';
        } else if (xhr.status === 500) {
            message = message || '服务器内部错误，请稍后重试';
        } else if (xhr.status === 503) {
            message = message || '服务暂时不可用，请稍后重试';
        } else {
            message = message || '操作失败，请稍后重试';
        }

        return message;
    }

    /**
     * 处理AJAX成功响应（检查响应中的code）
     */
    function handleAjaxSuccess(event, xhr, settings) {
        // 如果请求已经被其他错误处理函数处理过，跳过
        if (settings.skipGlobalErrorHandler) {
            return;
        }

        // 登录相关接口不拦截（避免循环跳转）
        if (settings.url && (
            settings.url.includes('/api/blbb/auth/login') ||
            settings.url.includes('/api/blbb/auth/status') ||
            settings.url.includes('/blbb/login')
        )) {
            return;
        }

        // 解析响应数据
        const { code: responseCode, data: responseData } = parseResponseData(xhr);
        
        if (responseCode !== null) {
            console.log('[GlobalErrorHandler] AJAX Success - 检测到 code:', responseCode, 'URL:', settings.url);
        }

        // 如果code是4003（未登录），跳转到登录页
        // 注意：responseCode 可能是字符串或数字，所以使用 == 而不是 ===
        if (responseCode == 4003 || responseCode === '4003') {
            const errorMessage = (responseData && (responseData.msg || responseData.message)) || '用户未登录，请登录!';
            handleNotLogin(errorMessage, responseData);
        }
    }

    /**
     * 解析响应数据（统一方法）
     * 支持从 responseJSON 或 responseText 解析，特别是处理 HTTP 400 等错误状态码
     */
    function parseResponseData(xhr) {
        let responseCode = null;
        let responseData = null;
        
        try {
            // 方法1：优先使用 responseJSON（jQuery 自动解析的）
            // 注意：对于 HTTP 4xx/5xx，jQuery 可能不会自动解析，所以还要尝试方法2
            if (xhr.responseJSON && typeof xhr.responseJSON === 'object') {
                responseData = xhr.responseJSON;
                if ('code' in xhr.responseJSON) {
                    responseCode = xhr.responseJSON.code;
                    console.log('[GlobalErrorHandler] 从 responseJSON 解析到 code:', responseCode);
                    return { code: responseCode, data: responseData };
                } else {
                    console.log('[GlobalErrorHandler] responseJSON 存在但没有 code 字段:', Object.keys(xhr.responseJSON));
                }
            }
            
            // 方法2：尝试手动解析 responseText（对于 HTTP 400 等状态码，jQuery 可能不会自动解析 JSON）
            if (xhr.responseText) {
                const text = xhr.responseText.trim();
                if (text && (text.startsWith('{') || text.startsWith('['))) {
                    try {
                        responseData = JSON.parse(text);
                        if (responseData && typeof responseData === 'object') {
                            if ('code' in responseData) {
                                responseCode = responseData.code;
                                console.log('[GlobalErrorHandler] 从 responseText 解析到 code:', responseCode, '完整响应:', text.substring(0, 300));
                                return { code: responseCode, data: responseData };
                            } else {
                                console.log('[GlobalErrorHandler] 解析到 JSON 但没有 code 字段:', Object.keys(responseData));
                            }
                        }
                    } catch (parseError) {
                        console.warn('[GlobalErrorHandler] 解析 responseText 失败:', parseError, 'Text:', text.substring(0, 200));
                    }
                } else if (text) {
                    console.log('[GlobalErrorHandler] responseText 不是 JSON 格式:', text.substring(0, 100));
                }
            }
        } catch (e) {
            console.warn('[GlobalErrorHandler] 解析响应数据失败:', e);
        }
        
        return { code: responseCode, data: responseData };
    }

    /**
     * 跳转到登录页面（统一方法）
     */
    function redirectToLoginPage() {
        const currentUrl = window.location.pathname + window.location.search;
        console.log('[GlobalErrorHandler] 执行跳转，当前URL:', currentUrl);
        if (currentUrl !== LOGIN_URL && !currentUrl.includes('/blbb/login')) {
            window.location.href = LOGIN_URL + '?redirect=' + encodeURIComponent(currentUrl);
        } else {
            window.location.href = LOGIN_URL;
        }
    }

    /**
     * 处理未登录情况（统一方法）
     */
    function handleNotLogin(errorMessage, responseData) {
        console.warn('[GlobalErrorHandler] ===== 检测到未登录(code=4003) =====');
        console.warn('[GlobalErrorHandler] 错误消息:', errorMessage);
        console.warn('[GlobalErrorHandler] 响应数据:', responseData);
        console.warn('[GlobalErrorHandler] 准备跳转到登录页...');
        
        const msg = errorMessage || '用户未登录，请登录!';
        
        // 显示提示并跳转
        try {
            if (MessageBox && typeof MessageBox.warning === 'function') {
                MessageBox.warning(msg, '未授权访问', redirectToLoginPage);
                // 2秒后自动跳转（防止用户不点击确定）
                setTimeout(function() {
                    console.log('[GlobalErrorHandler] 自动跳转触发');
                    redirectToLoginPage();
                }, 2000);
            } else {
                // MessageBox 不可用，直接跳转
                console.warn('[GlobalErrorHandler] MessageBox 不可用，直接跳转');
                alert(msg + '\n\n即将跳转到登录页面...');
                redirectToLoginPage();
            }
        } catch (e) {
            console.error('[GlobalErrorHandler] 显示 MessageBox 失败:', e);
            // 出错时直接跳转
            alert(msg + '\n\n即将跳转到登录页面...');
            redirectToLoginPage();
        }
    }

    /**
     * 处理AJAX错误
     */
    function handleAjaxError(event, xhr, settings, thrownError) {
        // 如果请求已经被其他错误处理函数处理过，跳过
        if (settings.skipGlobalErrorHandler) {
            return;
        }

        // 登录相关接口不拦截（避免循环跳转）
        if (settings.url && (
            settings.url.includes('/api/blbb/auth/login') ||
            settings.url.includes('/api/blbb/auth/status') ||
            settings.url.includes('/blbb/login')
        )) {
            return;
        }

        const status = xhr.status;
        const message = parseErrorMessage(xhr);
        
        // 解析响应数据（特别注意：HTTP 400 状态码时，响应体 JSON 中的 code 应该是 4003）
        const { code: responseCode, data: responseData } = parseResponseData(xhr);

        console.error('[GlobalErrorHandler] ===== AJAX请求失败 =====', {
            url: settings.url,
            method: settings.type || settings.method,
            status: status,
            code: responseCode,
            message: message,
            error: thrownError,
            responseJSON: xhr.responseJSON,
            responseText: xhr.responseText ? xhr.responseText.substring(0, 500) : null,
            contentType: xhr.getResponseHeader('Content-Type'),
            hasResponseJSON: !!xhr.responseJSON,
            responseJSONKeys: xhr.responseJSON ? Object.keys(xhr.responseJSON) : null
        });

        // 优先检查：响应中的code是否为4003（未登录）
        // HTTP 状态码 400 + 响应体 code=4003 表示未登录
        // 注意：responseCode 可能是字符串或数字，所以使用 == 而不是 ===
        if (responseCode == 4003 || responseCode === '4003') {
            console.log('[GlobalErrorHandler] ✓ 检测到 code=4003 (未登录)，HTTP状态码:', status);
            const errorMessage = (responseData && (responseData.msg || responseData.message)) || message || '用户未登录，请登录!';
            handleNotLogin(errorMessage, responseData);
            // 阻止后续的错误处理
            return false;
        }
        
        // 额外检查：如果 HTTP 状态码是 400 但无法解析到 code，尝试从 responseText 手动解析
        // 这种情况可能是 jQuery 没有自动解析 JSON
        if (status === 400 && !responseCode && xhr.responseText) {
            console.log('[GlobalErrorHandler] HTTP 400 但未解析到 code，尝试手动解析 responseText');
            try {
                const text = xhr.responseText.trim();
                if (text && text.startsWith('{')) {
                    const parsed = JSON.parse(text);
                    if (parsed && parsed.code == 4003) {
                        console.log('[GlobalErrorHandler] ✓ 手动解析检测到 code=4003');
                        const errorMessage = (parsed.msg || parsed.message) || '用户未登录，请登录!';
                        handleNotLogin(errorMessage, parsed);
                        return false;
                    }
                }
            } catch (e) {
                console.warn('[GlobalErrorHandler] 手动解析 responseText 失败:', e);
            }
        }

        // 401未登录，跳转到登录页
        if (status === 401) {
            MessageBox.warning(message || '未登录或登录已过期，即将跳转到登录页面', '未授权访问', function() {
                // 跳转到登录页，并保存当前页面URL以便登录后返回
                const currentUrl = window.location.pathname + window.location.search;
                if (currentUrl !== LOGIN_URL && !currentUrl.includes('/blbb/login')) {
                    window.location.href = LOGIN_URL + '?redirect=' + encodeURIComponent(currentUrl);
                } else {
                    window.location.href = LOGIN_URL;
                }
            });
            return;
        }

        // 如果是成功响应但code不是2000的情况，也显示错误
        // 这种情况通常已经在success回调中处理，但作为兜底
        if (responseCode && responseCode !== 2000 && responseCode !== 4003) {
            const errorMsg = xhr.responseJSON && (xhr.responseJSON.msg || xhr.responseJSON.message) || message;
            MessageBox.error(errorMsg, '操作失败');
            return;
        }

        // 其他错误，显示弹框提示
        MessageBox.error(message, '请求失败');
    }

    /**
     * 添加弹框样式
     */
    function injectStyles() {
        if ($('#global-error-handler-styles').length > 0) {
            return; // 样式已存在
        }

        const styles = `
            <style id="global-error-handler-styles">
                .message-box-overlay {
                    position: fixed;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    background: rgba(0, 0, 0, 0.5);
                    z-index: 999999;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    backdrop-filter: blur(4px);
                    animation: fadeIn 0.2s ease;
                }

                @keyframes fadeIn {
                    from { opacity: 0; }
                    to { opacity: 1; }
                }

                .message-box {
                    background: white;
                    border-radius: 16px;
                    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                    padding: 32px;
                    max-width: 480px;
                    width: 90%;
                    position: relative;
                    border-top: 4px solid #4299e1;
                    animation: slideUp 0.3s ease;
                    transform: translate(-50%, -50%);
                    position: absolute;
                    top: 50%;
                    left: 50%;
                }

                @keyframes slideUp {
                    from {
                        opacity: 0;
                        transform: translate(-50%, -40%);
                    }
                    to {
                        opacity: 1;
                        transform: translate(-50%, -50%);
                    }
                }

                .message-box-icon {
                    font-size: 48px;
                    text-align: center;
                    margin-bottom: 16px;
                }

                .message-box-content {
                    text-align: center;
                    margin-bottom: 24px;
                }

                .message-box-title {
                    font-size: 20px;
                    font-weight: 600;
                    color: #1a202c;
                    margin-bottom: 12px;
                    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                }

                .message-box-message {
                    font-size: 15px;
                    color: #4a5568;
                    line-height: 1.6;
                    word-wrap: break-word;
                    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                }

                .message-box-close {
                    width: 100%;
                    padding: 12px 24px;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                    border: none;
                    border-radius: 8px;
                    font-size: 15px;
                    font-weight: 600;
                    cursor: pointer;
                    transition: all 0.3s ease;
                    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                }

                .message-box-close:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
                }

                .message-box-close:active {
                    transform: translateY(0);
                }
            </style>
        `;

        $('head').append(styles);
    }

    /**
     * 初始化
     */
    function init() {
        // 注入样式
        injectStyles();

        // 配置全局AJAX默认设置，确保JSON自动解析
        // 注意：只设置 dataType 和 accepts，不设置 contentType（避免影响 GET 请求）
        $.ajaxSetup({
            dataType: 'json',
            accepts: {
                json: 'application/json'
            },
            // 确保即使 HTTP 400/500 也能尝试解析 JSON
            converters: {
                'text json': function(text) {
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        return text;
                    }
                }
            },
            // 全局错误处理：即使设置了，也不会覆盖页面级的错误处理
            error: function(xhr, status, error) {
                // 这里不做处理，让全局的 ajaxError 事件处理
            }
        });

        // 注册全局AJAX成功处理（检查响应中的code）
        $(document).ajaxSuccess(handleAjaxSuccess);

        // 注册全局AJAX错误处理
        $(document).ajaxError(handleAjaxError);

        // 将MessageBox暴露到全局
        window.MessageBox = MessageBox;
        
        // 设置初始化标志
        window.__GlobalErrorHandlerInitialized = true;

        console.log('[GlobalErrorHandler] 全局错误处理器已初始化');
        console.log('[GlobalErrorHandler] jQuery版本:', $.fn.jquery);
        
        // 监听所有AJAX请求，用于调试
        $(document).ajaxSend(function(event, xhr, settings) {
            console.log('[GlobalErrorHandler] AJAX请求发送:', settings.url, settings.type);
        });
    }

    // DOM加载完成后初始化
    if (document.readyState === 'loading') {
        $(document).ready(init);
    } else {
        init();
    }

})(jQuery);

