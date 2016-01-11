package org.c.lins.auth.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.c.lins.auth.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lins on 16-1-11.
 */
public class AccountDto {

    /**
     * User
     */
    public Long id;
    public String aliasName;
    public String loginName;
    public String hashPassword;
    public String salt;
    public String email;

    /**
     * User Role 关系
     */
    public List<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
