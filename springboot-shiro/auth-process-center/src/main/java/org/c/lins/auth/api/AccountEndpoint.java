package org.c.lins.auth.api;

import org.c.lins.auth.entity.User;
import org.c.lins.auth.service.AccountService;
import org.c.lins.auth.utils.constants.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lins on 16-1-11.
 */
@RestController
@RequestMapping(value = "/v1/account")
public class AccountEndpoint {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/register",method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
    public void register(@RequestBody User user){
        try {
            accountService.register(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = {"/","/list"}, produces = MediaTypes.JSON_UTF_8)
    public List<User> listAllAccount(Pageable pageable){
        return accountService.findAll(pageable);
    }


    @RequestMapping(value = {"/text","/test"}, produces = MediaTypes.JSON_UTF_8)
    public String listAllAccount(){
        return "碾；";
    }
}
