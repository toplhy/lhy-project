package top.lhy.plugin.quick.config;

import cn.dev33.satoken.util.SaFoxUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "lhy.quick")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuickLoginConfig {

    /** 是否开启全局认证 */
    private Boolean auth = true;

    /** 用户名 */
    private String name = "admin";

    /** 密码 */
    private String pwd = "123456";

    /** 是否自动生成一个账号和密码 */
    private Boolean auto = false;

    /** 登录页面的标题 */
    private String title = "快速登录";

    /** 是否显示底部版权信息 */
    private Boolean copr = true;

    /** 配置拦截的路径，逗号分隔 */
    private String include = "/**";

    /** 配置拦截的路径，逗号分隔 */
    private String exclude = "";

    public void setAuto(Boolean auto) {
        this.auto = auto;
        if(auto) {
            this.name = SaFoxUtil.getRandomString(8);
            this.pwd = SaFoxUtil.getRandomString(8);
        }
    }
}
