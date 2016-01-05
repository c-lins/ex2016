package org.c.lins.auth.conf.shiro;

import org.c.lins.auth.conf.shiro.realm.ShiroDbRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by linchao on 15-12-21.
 */
@Configuration
public class ShiroConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfiguration.class);

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();


    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public AuthorizingRealm realm() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(ShiroDbRealm.HASH_ALGORITHM);
        credentialsMatcher.setHashIterations(ShiroDbRealm.HASH_INTERATIONS);

        ShiroDbRealm securityRealm = new ShiroDbRealm();
        securityRealm.setCredentialsMatcher(credentialsMatcher);
        securityRealm.setCacheManager(getEhCacheManager());
        return securityRealm;
    }

    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(realm());
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean
                .setSecurityManager(getDefaultWebSecurityManager());
//        shiroFilterFactoryBean.setLoginUrl("/auth/login");
//        shiroFilterFactoryBean.setSuccessUrl("/auth/login");
//        shiroFilterFactoryBean.setSuccessUrl("/auth/login/ok");
        filterChainDefinitionMap.put("/auth/**", "anon");
//        filterChainDefinitionMap.put("/auth/logout", "logout");
        filterChainDefinitionMap.put("/user/info", "authc");
        filterChainDefinitionMap.put("/user/all", "authc,roles[admin]");
        filterChainDefinitionMap.put("/visit/my/save", "authc,roles[other]");
        filterChainDefinitionMap.put("/visit/my/edit", "authc,roles[other]");
        filterChainDefinitionMap.put("/visit/**", "authc");
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}