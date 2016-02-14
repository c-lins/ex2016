package org.c.lins.auth.controller;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.c.lins.auth.controller.request.Article;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.pojo.response.Helloworld;
import org.c.lins.auth.repository.UserDao;
import org.c.lins.auth.security.ShiroUser;
import org.c.lins.auth.security.jwt.JWTToken;
import org.c.lins.auth.security.jwt.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/hello")
public class HomeController {

	@Autowired
	private JWTToken tokens;

	@Autowired
	private UserDao userDao;
	@POST
	@Path("/world")
	@Produces("application/json")
	public Helloworld helloworld() throws Exception {
		return new Helloworld("Welcome, HelloWorld sssss");
	}

	@GET
	@Path("/auth")
	@Produces("application/json")
	public Helloworld auth(@Context SecurityContext context) {
		return new Helloworld(context.getUserPrincipal().getName());
	}

    //TODO 测试中，此处报错，可能是参数转换为Article实例出错
	@POST
	@Path("/article")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Article save(Article article) {
		return article;
	}

//   /**
//     * 因为自动转换为实例出错，所有直接获取参数，再创建实例
//     * @param name
//     * @param id
//     * @return
//     */

   /*
    @POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Article save(@QueryParam("name") String name,
                        @QueryParam("id") int id) {
		return new Article(id, name);
	}*/

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Article> save(
			@QueryParam("multi") boolean isMulti,
			List<Article> articles) {
		return articles;
	}
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public TokenResponse login(User user) {
		//当前Subject
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken uptoken = new UsernamePasswordToken(
				user.getLoginName(),
                user.getHashPassword());
//		uptoken.setRememberMe(true);
        try {
			subject.login(uptoken);

        } catch (AuthenticationException e) {//登录失败
            e.printStackTrace();
        } catch (Exception e) {//登录失败
            e.printStackTrace();
        }

		ShiroUser cuuser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		TokenResponse token = tokens.createToken(cuuser);
		return token;
	}
}
