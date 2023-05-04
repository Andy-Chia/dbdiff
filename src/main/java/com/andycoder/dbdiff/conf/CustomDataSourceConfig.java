package com.andycoder.dbdiff.conf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration//注册到springboot 容器中
@MapperScan(basePackages = "com.andycoder.dbdiff.dao.custom",
        sqlSessionTemplateRef = "customSqlSessionTemplate")
public class CustomDataSourceConfig {
    @Bean(name = "customDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.custom")
    public DataSource customDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "customSqlSessionFactory")
    public SqlSessionFactory customSqlSessionFactory
            (@Qualifier("customDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        return bean.getObject();
    }

    @Bean(name = "customTransactionManager")
    public DataSourceTransactionManager customTransactionManager
            (@Qualifier("customDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "customSqlSessionTemplate")
    public SqlSessionTemplate customSqlSessionTemplate
            (@Qualifier("customSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
