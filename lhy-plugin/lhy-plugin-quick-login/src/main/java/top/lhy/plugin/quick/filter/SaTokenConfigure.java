package top.lhy.plugin.quick.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lhy.plugin.quick.config.QuickLoginConfig;

import javax.annotation.Resource;

@Configuration
public class SaTokenConfigure {

    @Resource
    private QuickLoginConfig loginConfig;

    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                // 拦截路由 & 放行路由
                .addInclude(loginConfig.getInclude().split(","))
                .addExclude(loginConfig.getExclude().split(","))
                .addExclude("/favicon.ico", "/toLogin", "/doLogin", "/quick-res/**").
                // 认证函数: 每次请求执行
                setAuth(r -> {
                    // 未登录时直接转发到login.html页面
                    if (loginConfig.getAuth() && !StpUtil.isLogin()) {
                        SaHolder.getRequest().forward("/toLogin");
                        SaRouter.back();
                    }
                }).

                // 异常处理函数：每次认证函数发生异常时执行此函数
                setError(e -> {
                    return e.getMessage();
                });
    }

}
