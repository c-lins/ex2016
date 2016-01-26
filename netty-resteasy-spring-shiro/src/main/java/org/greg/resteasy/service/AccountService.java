package org.greg.resteasy.service;

import org.apache.commons.lang3.StringUtils;
import org.greg.resteasy.entity.User;
import org.greg.resteasy.repository.RoleDao;
import org.greg.resteasy.repository.UserDao;
import org.greg.resteasy.service.exception.ErrorCode;
import org.greg.resteasy.service.exception.ServiceException;
import org.greg.resteasy.utils.Digests;
import org.greg.resteasy.utils.Encodes;
import org.greg.resteasy.utils.constants.Securitys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lins on 16-1-11.
 */
//@Service
public class AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

//    @Autowired
    private UserDao userDao;

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

//    @Transactional
//    public List<User> findAll(Pageable pageable) {
//        Iterable<User> users = userDao.findAll(pageable);
//        return BeanMapper.mapList(users, User.class);
//    }

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
    public User findById(Long id) {
        return userDao.findOne(id);
    }

    /**
     * 获取当前用户数量.
     */
    public Long getUserCount() {
        return userDao.count();
    }

}
