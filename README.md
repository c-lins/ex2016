# 2016 Examples Demo

--------------------------------------------------------------------------------
配置实体类注解时，出现以下错误：
Caused by: org.hibernate.MappingException: Could not determine type for: java.util.List, at table: ss_user, for columns: [org.hibernate.mapping.Column(role_list)]
解决方案：注解要么写在字段上，要么写在getXX上，千万千万不能混合使用，否则会报这个错误！
--------------------------------------------------------------------------------
shiro中默认的过滤器
anon 	            org.apache.shiro.web.filter.authc.AnonymousFilter 	                匿名过滤器
authc 	            org.apache.shiro.web.filter.authc.FormAuthenticationFilter 	        如果继续操作，需要做对应的表单验证否则不能通过
authcBasic 	        org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter 	基本http验证过滤，如果不通过，跳转屋登录页面
logout 	            org.apache.shiro.web.filter.authc.LogoutFilter 	                    登录退出过滤器
noSessionCreation 	org.apache.shiro.web.filter.session.NoSessionCreationFilter 	    没有session创建过滤器
perms 	            org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter 	权限过滤器
port 	            org.apache.shiro.web.filter.authz.PortFilter 	                    端口过滤器，可以设置是否是指定端口如果不是跳转到登录页面
rest 	            org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter 	    http方法过滤器，可以指定如post不能进行访问等
roles 	            org.apache.shiro.web.filter.authz.RolesAuthorizationFilter 	        角色过滤器，判断当前用户是否指定角色
ssl 	            org.apache.shiro.web.filter.authz.SslFilter 	                    请求需要通过ssl，如果不是跳转回登录页
user 	            org.apache.shiro.web.filter.authc.UserFilter 	                    如果访问一个已知用户，比如记住我功能，走这个过滤器
--------------------------------------------------------------------------------
anon:  例子/admins/**=anon 没有参数，表示可以匿名使用。
authc: 例如/admins/user/**=authc表示需要认证(登录)才能使用，没有参数
authcBasic：例如/admins/user/**=authcBasic没有参数表示httpBasic认证
user:例如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查
roles：例子/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。
perms：例子/admins/user/**=perms[user:add:*],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/**=perms["user:add:*,user:modify:*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
rest：例子/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user:method] ,其中method为post，get，delete等。
ssl:例子/admins/user/**=ssl没有参数，表示安全的url请求，协议为https
port：  例子/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString
是你访问的url里的？后面的参数。
注：anon，authcBasic，auchc，user是认证过滤器，perms，roles，ssl，rest，port是授权过滤器
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------

--------------------------------------------------------------------------------