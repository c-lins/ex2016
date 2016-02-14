package org.c.lins.auth.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by lins on 16-1-11.
 */
//@Entity
public class Role {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String roleAliasName;
    public String roleName;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
