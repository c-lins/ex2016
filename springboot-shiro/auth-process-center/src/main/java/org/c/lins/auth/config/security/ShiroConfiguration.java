package org.c.lins.auth.config.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.c.lins.auth.utils.SpringContextHolder;
import org.c.lins.auth.utils.constants.Securitys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lins on 16-1-11.
 */
@Configuration
public class ShiroConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();


    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public AuthorizingRealm realm(EhCacheManager cacheManager) {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(Securitys.HASH_ALGORITHM);
        credentialsMatcher.setHashIterations(Securitys.HASH_INTERATIONS);

        ShiroDbRealm securityRealm = new ShiroDbRealm();
        securityRealm.setCredentialsMatcher(credentialsMatcher);
        securityRealm.setCacheManager(cacheManager);
        return securityRealm;
    }

    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(EhCacheManager cacheManager,AuthorizingRealm realm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(realm);
        dwsm.setCacheManager(cacheManager);
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager dwsm) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(dwsm);
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager dwsm) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean
                .setSecurityManager(dwsm);

        return shiroFilterFactoryBean;
    }

    @Bean(name = "filterChainManager")
    public CustomDefaultFilterChainManager customDefaultFilterChainManager() {
        CustomDefaultFilterChainManager cdfcm = new CustomDefaultFilterChainManager();

        filterChainDefinitionMap.put("/v1/auth/**", "anon");
        filterChainDefinitionMap.put("/v1/account/", "authc");
        cdfcm.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return cdfcm;
    }

    @Bean(name = "filterChainResolver")
    public CustomPathMatchingFilterChainResolver customPathMatchingFilterChainResolver(CustomDefaultFilterChainManager cdfcm) {
        CustomPathMatchingFilterChainResolver cpmfcr = new CustomPathMatchingFilterChainResolver();

        cpmfcr.setCustomDefaultFilterChainManager(cdfcm);
        return cpmfcr;
    }

//    @Bean
//    public MethodInvokingFactoryBean methodInvokingFactoryBean(ShiroFilterFactoryBean shiroFilter,CustomPathMatchingFilterChainResolver filterChainResolver) {
//        MethodInvokingFactoryBean mifb = new MethodInvokingFactoryBean();
//
//        mifb.setTargetObject(shiroFilter);
//        mifb.setTargetMethod("setFilterChainResolver");
//        mifb.setArguments(new Object[]{filterChainResolver});
//        return mifb;
//    }

}
