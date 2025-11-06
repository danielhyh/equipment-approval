package cn.iocoder.yudao.module.system.controller.sso;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.yudao.module.system.convert.auth.AuthConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.yudao.module.system.service.oauth2.OAuth2TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.chrono.JapaneseDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RequestMapping({"/app-api/sso", "/admin-api/sso", "/sso"})
@RestController
@Tag(name = "SSO 登录")

@Slf4j
public class SSOController {

    @Value("${sso.client-id}")
    private String clientId;

    @Value("${sso.client-secret}")
    private String clientSecret;

    @Value("${sso.base-url}")
    private String ssoBaseUrl;

    @Value("${sso.intranet-base-url}")
    private String intranetBaseUrl;

    @Value("${sso.callback-url}")
    private String callbackUri;

    @Value("${sso.front-end-url}")
    private String frontendUrl;

    @Value("${sso.admin-postfix}")
    private String adminPostfix;

    @Value("${sso.client-postfix}")
    private String clientPostfix;

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private StringRedisTemplate template;

    @GetMapping("/login-url")
    @PermitAll
    @Operation(summary = "获取登录单点登录 URL")
    public CommonResult<String> getLoginUrl() {
        String url = ssoBaseUrl + "/sso/authorize.do" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + URLEncoder.encode(callbackUri, StandardCharsets.UTF_8);
        log.info("生成SSO登录URL: {}", url);
        return success(url);
    }


    @GetMapping("/callback")
    @PermitAll
    public void handlerCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        log.info("收到SSO回调，授权码: {}", code);

        try {
            // 1. 用授权码换取Token
            String tokenUrl = intranetBaseUrl + "/sso/accessToken.do";
            Map<String, Object> tokenParams = new HashMap<>();
            tokenParams.put("code", code);
            tokenParams.put("client_id", clientId);
            tokenParams.put("client_secret", clientSecret);

            String tokenResponse = HttpUtil.post(tokenUrl, tokenParams);
            log.info("Token响应: {}", tokenResponse);

            JSONObject tokenJson = JSONUtil.parseObj(tokenResponse);
            if (tokenJson.getInt("code", -1) != 0) {
                throw new RuntimeException("获取Token失败: " + tokenJson.getStr("message"));
            }

            String accessToken = tokenJson.getStr("access_token");

            // 2. 获取用户信息
            String userUrl = intranetBaseUrl + "/sso/userInfo.do";
            Map<String, Object> userParams = new HashMap<>();
            userParams.put("access_token", accessToken);

            String userResponse = HttpUtil.post(userUrl, userParams);
            log.info("用户信息响应: {}", userResponse);

            JSONObject userJson = JSONUtil.parseObj(userResponse);
            if (userJson.getInt("code", -1) != 0) {
                throw new RuntimeException("获取用户信息失败: " + userJson.getStr("message"));
            }

            String ssoUserId = userJson.getStr("userId");
            String userCaption = userJson.getStr("userCaption");
            String cacheKey = userJson.getStr("cacheKey");
            // 3. 查找或创建本地用户
            AdminUserDO user = getUserBySsoId(ssoUserId);
            if (user == null) {
//                Long userId = createSsoUser(ssoUserId, userCaption);
//                user = userService.getUser(userId);
//                log.info("创建SSO用户: userId={}, nickname={}", ssoUserId, userCaption);
                throw new ServiceException(1, "用户不存在，请先同步用户");
            }

            UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(user.getType());
            // 4. 生成本地Token
            OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                    user.getId(),
                    userTypeEnum.getValue(),
                    OAuth2ClientConstants.CLIENT_ID_DEFAULT,
                    null
            );
            AuthLoginRespVO loginResp = AuthConvert.INSTANCE.convert(accessTokenDO);
            template.opsForValue().set(cacheKey, loginResp.getAccessToken());
            // 5. 重定向到前端
            String postfix = "";
            if (userTypeEnum == UserTypeEnum.ADMIN) {
                postfix = adminPostfix;
            } else {
                postfix = clientPostfix;
            }
            String redirectUrl = frontendUrl + postfix + loginResp.getAccessToken();
            log.info("SSO登录成功，重定向: {}", redirectUrl);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("SSO回调处理失败", e);
            String errorUrl = frontendUrl + "/#/login?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
    }

    private AdminUserDO getUserBySsoId(String ssoUserId) {
        String orgId = jdbcClient.sql("select org_id from system_external_user where id = ?")
                .param(ssoUserId)
                .query(String.class)
                .optional()
                .orElse(null);
        if (orgId == null) {
            return null;
        }
        return userMapper.selectByExternalDeptId(orgId);
    }

    @PostMapping("/logout")
    public void logout(Map<String, String> params) {
        if (params.containsKey("cacheKey")) {
            String accessToken = template.opsForValue().get(params.get("cacheKey"));
            oauth2TokenService.removeAccessToken(accessToken);
        }
    }
}
