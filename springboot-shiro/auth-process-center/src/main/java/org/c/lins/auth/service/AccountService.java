package org.c.lins.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.repository.RoleDao;
import org.c.lins.auth.repository.UserDao;
import org.c.lins.auth.service.exception.ErrorCode;
import org.c.lins.auth.service.exception.ServiceException;
import org.c.lins.auth.utils.BeanMapper;
import org.c.lins.auth.utils.Digests;
import org.c.lins.auth.utils.Encodes;
import org.c.lins.auth.utils.constants.Securitys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    private static void hashPassword(User user, String password) {
        byte[] salt = Digests.generateSalt(Securitys.SALT_SIZE);
        user.salt= Encodes.encodeHex(salt);

        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, Securitys.HASH_INTERATIONS);
        user.hashPassword=Encodes.encodeHex(hashPassword);
    }

    @Transactional
    public void register(User user) {
        if (user==null || StringUtils.isBlank(user.aliasName) || StringUtils.isBlank(user.loginName) || StringUtils.isBlank(user.hashPassword) || StringUtils.isBlank(user.salt)) {
            throw new ServiceException("Invalid parameter", ErrorCode.BAD_REQUEST);
        }
        userDao.save(user);
    }

    @Transactional
    public List<User> findAll(Pageable pageable) {
        Iterable<User> users = userDao.findAll(pageable);
        return BeanMapper.mapList(users, User.class);
    }

    /**
     * 按登录名查询用户.
     */
    @Transactional
    public User findUserByLoginName(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    /**
     * 按Id获得用户.
     */
    public User getUser(Long id) {
        return userDao.findOne(id);
    }

    /**
     * 获取当前用户数量.
     */
    public Long getUserCount() {
        return userDao.count();
    }

}
