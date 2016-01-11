package org.c.lins.auth.service;

import org.c.lins.auth.dto.AccountDto;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.utils.BeanMapper;
import org.c.lins.auth.utils.Digests;
import org.c.lins.auth.utils.Encodes;
import org.c.lins.auth.utils.constants.Securitys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lins on 16-1-11.
 */
@Service
public class AccountService {

    @Autowired
    private UserService userService;

    private static void hashPassword(User user, String password) {
        byte[] salt = Digests.generateSalt(Securitys.SALT_SIZE);
        user.salt= Encodes.encodeHex(salt);

        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, Securitys.HASH_INTERATIONS);
        user.hashPassword=Encodes.encodeHex(hashPassword);
    }

    @Transactional
    public void register(AccountDto accountDto) {
        User user = BeanMapper.map(accountDto,User.class);
        userService.saveUser(user);
    }

    @Transactional
    public List<AccountDto> findAll(Pageable pageable) {
        Iterable<User> users = userService.findAll(pageable);
        return BeanMapper.mapList(users, AccountDto.class);
    }

    public static void main(String[] args) {
        byte[] salt = Digests.generateSalt(Securitys.SALT_SIZE);
        System.out.println(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1("123456".getBytes(), salt, Securitys.HASH_INTERATIONS);
        System.out.println(Encodes.encodeHex(hashPassword));
    }
}
