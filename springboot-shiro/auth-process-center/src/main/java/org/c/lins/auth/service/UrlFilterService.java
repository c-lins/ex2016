package org.c.lins.auth.service;

import org.c.lins.auth.config.security.UrlFilterChainManager;
import org.c.lins.auth.entity.UrlFilter;
import org.c.lins.auth.repository.UrlFilterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by lins on 16-1-12.
 */
@Service
public class UrlFilterService {

    @Autowired
    private UrlFilterDao urlFilterDao;
    @Autowired
    private UrlFilterChainManager urlFilterChainManager;

    @Transactional(readOnly = true)
    public List<UrlFilter> findAll(){
        return (List<UrlFilter>) urlFilterDao.findAll();
    }

    @PostConstruct
    public void initFilterChain() {
        urlFilterChainManager.initFilterChains(findAll());
    }
}
