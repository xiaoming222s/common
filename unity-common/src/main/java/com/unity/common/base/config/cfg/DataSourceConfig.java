
package com.unity.common.base.config.cfg;


import com.unity.common.base.config.datasource.DynamicDataSource;
import com.unity.common.base.config.datasource.DynamicDataSourceFactory;
import com.unity.common.base.config.interceptor.SelectCountInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;


/**
 * 数据源配置，使用dynamicdatasource。并使用driud数据源<br>
 *
 * <b>注意</b><br/>
 * 此类加上了<code>@RefreshScope</code>注解， 根据<a href=
 * "http://cloud.spring.io/spring-cloud-static/docs/1.0.x/spring-cloud.html#_refresh_scope">
 * 官方文档</a>对其解释，<u>‘@RefreshScope works (technically) on an @Configuration
 * class, but it might lead to surprising behaviour.‘</u>
 * <code>@Configuration</code>与<code>@RefreshScope</code> 同时使用可能会出现‘意想不到的行为’.
 * 测试中并未发现，但此问题仍不可忽略，有待后续深入理解并完善.
 *
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {
    @Autowired
    private Environment env;

    @Bean
    @Primary
//    @RefreshScope
    public DynamicDataSource dataSource() throws Exception {
        log.info("=================ActiveProfiles===============");
        for(String e :env.getActiveProfiles()){
            log.info(e);
        }
        log.info("=================ActiveProfiles===============");

        //System.out.println("=================DefaultProfiles===============");
        /*for(String e :env.getDefaultProfiles()){
            System.out.println(e);
        }*/
        //System.out.println("=================DefaultProfiles===============");

        DynamicDataSource ds = DynamicDataSourceFactory.build("spring.datasource.master", "spring.datasource.slave").createDataSource(env);
        return ds;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
