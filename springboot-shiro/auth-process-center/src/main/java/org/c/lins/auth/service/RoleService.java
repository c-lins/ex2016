package org.c.lins.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.c.lins.auth.entity.Role;
import org.c.lins.auth.repository.RoleDao;
import org.c.lins.auth.service.exception.ErrorCode;
import org.c.lins.auth.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lins on 16-1-11.
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Transactional
    public void saveRole(Role role) {

        if (role==null || StringUtils.isBlank(role.roleAliasName) || StringUtils.isBlank(role.roleName)) {
            throw new ServiceException("Invalid parameter", ErrorCode.BAD_REQUEST);
        }
        roleDao.save(role);
    }

    @Transactional(readOnly = true)
    public Iterable<Role> findAll(Pageable pageable) {
        return roleDao.findAll(pageable);
    }
}
