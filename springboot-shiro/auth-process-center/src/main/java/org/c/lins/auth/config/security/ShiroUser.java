package org.c.lins.auth.config.security;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by lins on 16-1-11.
 * @description :自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public String loginName;
    public String aliasName;

    public String getLoginName() {
        return loginName;
    }

    public Long getPrincipal() {
        return 1L;
    }
    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return aliasName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(loginName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ShiroUser other = (ShiroUser) obj;
        if (loginName == null) {
            if (other.loginName != null) {
                return false;
            }
        } else if (!loginName.equals(other.loginName)) {
            return false;
        }
        return true;
    }
}

