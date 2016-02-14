package org.c.lins.auth.repository;

import org.c.lins.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * CrudRepository默认有针对实体对象的CRUD方法.
 * PagingAndSortingRepository默认有针对实体对象的CRUD与分页查询函数.
 *
 */
//public interface UserDao extends PagingAndSortingRepository<User, Long> {
public interface UserDao extends JpaRepository<User, Long> {

	User findByEmail(String email);
	User findByLoginName(String loginName);
}
