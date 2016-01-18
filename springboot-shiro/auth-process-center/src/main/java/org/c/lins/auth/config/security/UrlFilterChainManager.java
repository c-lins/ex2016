package org.c.lins.auth.config.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.c.lins.auth.entity.UrlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lins on 16-1-12.
 */
@Service
public class UrlFilterChainManager {

    private static final Logger logger = LoggerFactory.getLogger(UrlFilterChainManager.class);

    @Autowired
    private DefaultFilterChainManager filterChainManager;
    private Map<String, NamedFilterList> defaultFilterChains;

    @PostConstruct
    public void init() {
        defaultFilterChains =
                new HashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
    }

    public void initFilterChains(List<UrlFilter> urlFilters) {
        //1、首先删除以前老的filter chain并注册默认的
        filterChainManager.getFilterChains().clear();
        if(defaultFilterChains != null) {
            filterChainManager.getFilterChains().putAll(defaultFilterChains);
        }
        //2、循环URL Filter 注册filter chain
        for (UrlFilter urlFilter : urlFilters) {
            String url = urlFilter.url;
            ////注册basics filter
            if (!StringUtils.isEmpty(urlFilter.basics)) {
                filterChainManager.addToChain(url, urlFilter.basics);
            }
            //注册roles filter
            if (!StringUtils.isEmpty(urlFilter.roles)) {
                filterChainManager.addToChain(url, "roles", urlFilter.roles);
            }
            //注册perms filter
            if (!StringUtils.isEmpty(urlFilter.permissions)) {
                filterChainManager.addToChain(url, "perms", urlFilter.permissions);
            }

        }
        filterChainManager.addToChain("/**", "user");
    }
}
