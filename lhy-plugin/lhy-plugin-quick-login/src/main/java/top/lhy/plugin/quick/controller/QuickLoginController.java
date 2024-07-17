package top.lhy.plugin.quick.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.lhy.plugin.quick.config.QuickLoginConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class QuickLoginController {

    @Resource
    private QuickLoginConfig loginConfig;

    @GetMapping("/toLogin")
    public String toLogin(HttpServletRequest request) {
        request.setAttribute("cfg", loginConfig);
        return "quick-login.html";
    }


    @PostMapping("/doLogin")
    @ResponseBody
    public SaResult doLogin(String name, String pwd) {

        // 参数完整性校验
        if(SaFoxUtil.isEmpty(name) || SaFoxUtil.isEmpty(pwd)) {
            return SaResult.get(500, "请输入账号和密码", null);
        }

        // 密码校验
        if(name.equals(loginConfig.getName()) && pwd.equals(loginConfig.getPwd())) {
            StpUtil.login(loginConfig.getName());
            return SaResult.get(200, "ok", StpUtil.getTokenInfo());
        } else {
            // 校验失败
            return SaResult.get(500, "账号或密码输入错误", null);
        }
    }

    @GetMapping("/doLogoff")
    public void doLogoff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StpUtil.logout();
        response.sendRedirect(request.getContextPath());
    }
}
