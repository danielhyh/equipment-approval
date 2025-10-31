package cn.iocoder.yudao.module.system.controller.test;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.sso.SSOController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class TestController {

    @Resource
    private SSOController ssoController;

    @GetMapping("/sxwjwylqc")
    public void redirect(HttpServletResponse response) throws IOException {
        CommonResult<String> loginUrl = ssoController.getLoginUrl();
        String data = loginUrl.getData();
        response.sendRedirect(data);
    }
}
