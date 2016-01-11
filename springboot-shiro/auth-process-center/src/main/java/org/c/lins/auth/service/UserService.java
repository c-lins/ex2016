package org.c.lins.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.c.lins.auth.entity.User;
import org.c.lins.auth.repository.UserDao;
import org.c.lins.auth.service.exception.ErrorCode;
import org.c.lins.auth.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	// codehale metrics
	@Autowired
	private CounterService counterService;

	@Transactional
	public void saveUser(User user) {

		if (user==null || StringUtils.isBlank(user.aliasName) || StringUtils.isBlank(user.loginName) || StringUtils.isBlank(user.hashPassword) || StringUtils.isBlank(user.salt)) {
			throw new ServiceException("Invalid parameter", ErrorCode.BAD_REQUEST);
		}
		userDao.save(user);
	}

	@Transactional(readOnly = true)
	public User findByLoginName(String username) {
		return userDao.findByLoginName(username);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}
}
