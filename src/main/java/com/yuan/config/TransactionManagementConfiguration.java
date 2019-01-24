package com.yuan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import javax.sql.DataSource;

/**
 * Created by wangy on 2018/11/26.
 * <p>
 * 跟Service层相关的事务管理配置类
 */
@Configuration
@EnableTransactionManagement//该注解表示：开启事务管理。同时必须继承如下接口
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

    @Autowired
    @Qualifier("dataSource")//按名字注入
    private DataSource dataSource;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
