package org.c.lins.auth.conf.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by linchao@111.com.cn on 2015/10/12.
 */
@Configuration
@EnableTransactionManagement
public class DataBaseConfiguration implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    private static Logger log = LoggerFactory.getLogger(DataBaseConfiguration.class);

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "jdbc.");
    }

    @Bean(name="writeDataSource", destroyMethod = "close", initMethod="init")
    @Primary
    public DataSource writeDataSource() {
        log.debug("Configruing Write DataSource");

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setTestWhileIdle(Boolean.valueOf(propertyResolver.getProperty("testWhileIdle")));
        datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        return datasource;
    }

}
