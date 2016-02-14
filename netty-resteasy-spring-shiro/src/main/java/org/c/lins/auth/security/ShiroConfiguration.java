package org.c.lins.auth.security;

import com.google.common.collect.Lists;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.c.lins.auth.security.jwt.realm.JWTRealm;
import org.c.lins.auth.security.realm.StandardRealm;
import org.c.lins.auth.utils.constants.Securitys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lins on 16-1-11.
 */
@Configuration
public class ShiroConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    private static Map<String, Filter> filtersMap = new LinkedHashMap<>();


    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public AuthorizingRealm realm(EhCacheManager cacheManager) {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(Securitys.HASH_ALGORITHM);
        credentialsMatcher.setHashIterations(Securitys.HASH_INTERATIONS);

        StandardRealm securityRealm = new StandardRealm();
        securityRealm.setCredentialsMatcher(credentialsMatcher);
        securityRealm.setCacheManager(cacheManager);
        return securityRealm;
    }

    @Bean(name = "jwtrealm")
    public AuthorizingRealm jwtrealm(EhCacheManager cacheManager) {
        SimpleCredentialsMatcher tokenMatcher = new SimpleCredentialsMatcher();

        JWTRealm jwtRealm = new JWTRealm();
        jwtRealm.setCredentialsMatcher(tokenMatcher);
        return jwtRealm;
    }

    @Bean(name = "sessionManager")
    public DefaultSessionManager defaultSessionManager() {
        DefaultSessionManager dsm = new DefaultSessionManager();
        dsm.setSessionValidationSchedulerEnabled(false);
        return dsm;
    }

    @Bean(name = "subjectFactory")
    public StatelessDefaultSubjectFactory subjectFactory() {
        StatelessDefaultSubjectFactory sdsf = new StatelessDefaultSubjectFactory();
        return sdsf;
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
    public DefaultWebSecurityManager defaultWebSecurityManager(EhCacheManager cacheManager,AuthorizingRealm realm,AuthorizingRealm jwtrealm,DefaultSessionManager defaultSessionManager,StatelessDefaultSubjectFactory subjectFactory) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        Collection realms = Lists.newArrayList();
        realms.add(jwtrealm);
        realms.add(realm);

        dwsm.setRealms(realms);
        dwsm.setSessionManager(defaultSessionManager);
//        dwsm.setSubjectFactory(subjectFactory);
        dwsm.setCacheManager(cacheManager);
        return dwsm;
    }

//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager dwsm) {
//        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
//        aasa.setSecurityManager(dwsm);
//        return new AuthorizationAttributeSourceAdvisor();
//    }
//
//    @Bean(name = "shiroFilter")
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager dwsm) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean
//                .setSecurityManager(dwsm);
//
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean(name = "statelessAuthcFilter")
//    public JWTOrFormAuthenticationFilter statelessAuthcFilter() {
//        final JWTOrFormAuthenticationFilter statelessAuthc = new JWTOrFormAuthenticationFilter();
//        statelessAuthc.setLoginUrl("/rest/auth/login");
//
//        return statelessAuthc;
//    }
//
//    @Bean(name = "filterChainManager")
//    public CustomDefaultFilterChainManager customDefaultFilterChainManager(JWTOrFormAuthenticationFilter statelessAuthcFilter) {
//        CustomDefaultFilterChainManager cdfcm = new CustomDefaultFilterChainManager();
//
//        filtersMap.put("statelessAuthc",statelessAuthcFilter);
////        filterChainDefinitionMap.put("/rest/auth/login", "anon");
////        filterChainDefinitionMap.put("/v1/account/", "authc");
//
//        cdfcm.setCustomFilters(filtersMap);
//        cdfcm.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return cdfcm;
//    }
//
//    @Bean(name = "filterChainResolver")
//    public CustomPathMatchingFilterChainResolver customPathMatchingFilterChainResolver(CustomDefaultFilterChainManager cdfcm) {
//        CustomPathMatchingFilterChainResolver cpmfcr = new CustomPathMatchingFilterChainResolver();
//
//        cpmfcr.setCustomDefaultFilterChainManager(cdfcm);
//        return cpmfcr;
//    }

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
