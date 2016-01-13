package org.c.lins.auth.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.c.lins.auth.config.security.ShiroUser;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lins on 16-1-11.
 */
@RestController
@RequestMapping(value = "/v1/auth")
public class GFA_AuthEndpoint {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public User login(HttpServletRequest request){
        //当前Subject
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(
                request.getParameter("username"),
                request.getParameter("password"));
        token.setRememberMe(true);
        try {
            currentUser.login(token);

            return accountService.findUserByLoginName(((ShiroUser)currentUser.getPrincipal()).loginName);
        } catch (AuthenticationException e) {//登录失败
            e.printStackTrace();
        } catch (Exception e) {//登录失败
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        try {
            SecurityUtils.getSubject().logout();
        }catch (Exception e){
            return "用户退出失败";
        }
        return "用户退出成功";
    }
}
