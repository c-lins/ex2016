package org.greg.resteasy.repository;

import org.greg.resteasy.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * CrudRepository默认有针对实体对象的CRUD方法.
 * PagingAndSortingRepository默认有针对实体对象的CRUD与分页查询函数.
 *
 */
public interface UserDao extends PagingAndSortingRepository<User, Long> {

	User findByEmail(String email);
	User findByLoginName(String loginName);
}
