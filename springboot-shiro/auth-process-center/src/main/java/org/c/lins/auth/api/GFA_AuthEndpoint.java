package org.c.lins.auth.api;

import org.apache.shiro.SecurityUtils;
import org.c.lins.auth.config.security.jwt.JWTToken;
import org.c.lins.auth.config.security.jwt.TokenResponse;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lins on 16-1-11.
 */
@RestController
@RequestMapping(value = "/rest/auth")
public class GFA_AuthEndpoint {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTToken tokens;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public TokenResponse login(){

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        TokenResponse token = tokens.createToken(user);
        return token;

        //当前Subject
//        Subject currentUser = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(
//                request.getParameter("username"),
//                request.getParameter("password"));
//        token.setRememberMe(true);
//        try {
//            currentUser.login(token);
//
//            return accountService.findUserByLoginName(((ShiroUser)currentUser.getPrincipal()).loginName);
//        } catch (AuthenticationException e) {//登录失败
//            e.printStackTrace();
//        } catch (Exception e) {//登录失败
//            e.printStackTrace();
//        }
//        return null;
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

    @RequestMapping(value = "/tet")
    public String loginx() {
        try {
            //SecurityUtils.getSubject().logout();
        }catch (Exception e){
            return "用户退出失败";
        }
        return "用户退出成功";
    }
}
