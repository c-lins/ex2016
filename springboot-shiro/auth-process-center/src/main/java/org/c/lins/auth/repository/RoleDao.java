package org.c.lins.auth.repository;

import org.c.lins.auth.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by lins on 16-1-11.
 */
public interface RoleDao extends PagingAndSortingRepository<Role,Long> {
}
