### 快速登录认证 quick-login
使用Sa-Token实现的登录页面，在配置文件中配置用户名密码等信息

#### 集成步骤
+ 在项目中添加依赖
```xml
<dependency>
    <groupId>top.lhy</groupId>
    <artifactId>lhy-plugin-quick-login</artifactId>
    <version>1.0.0</version>
</dependency>
```
+ 配置文件
```yaml
lhy:
  quick:
    name: admin     # 用户名，默认admin
    pwd: 123456     # 密码，默认123456
    title: 快速登录       # 页面title，默认：快速登录
    auto: false     # 自动随机密码，默认false，为true时name和pwd无效
    auth: true      # 是否开启认证，默认true
    copr: true      # 是否显示版权， 默认true
    include: /**        # 拦截路径
    exclude: /assets/**,/buss/file/**       # 放行路径
```
