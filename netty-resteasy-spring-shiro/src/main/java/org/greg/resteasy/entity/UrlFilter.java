package org.greg.resteasy.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by lins on 16-1-12.
 */
//@Entity
public class UrlFilter {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name; //url名称/描述
    public String url; //地址
    public String basics; //地址
    public String roles; //所需要的角色，可省略
    public String permissions; //所需要的权限，可省略

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
