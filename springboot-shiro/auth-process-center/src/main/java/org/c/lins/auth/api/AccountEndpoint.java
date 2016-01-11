package org.c.lins.auth.api;

import org.c.lins.auth.dto.AccountDto;
import org.c.lins.auth.service.AccountService;
import org.c.lins.auth.utils.BeanMapper;
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
@RequestMapping(value = "/api")
public class AccountEndpoint {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/regist",method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
    public void register(@RequestBody AccountDto dto){
        try {
            accountService.register(dto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/accouts", produces = MediaTypes.JSON_UTF_8)
    public List<AccountDto> liatAllAccount(Pageable pageable){
        return accountService.findAll(pageable);
    }
}
