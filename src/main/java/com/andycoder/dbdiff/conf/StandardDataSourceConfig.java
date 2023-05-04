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
@MapperScan(basePackages = "com.andycoder.dbdiff.dao.standard",
        sqlSessionTemplateRef = "standardSqlSessionTemplate")
public class StandardDataSourceConfig {
    @Bean(name = "standardDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.standard")
    public DataSource standardDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "standardSqlSessionFactory")
    public SqlSessionFactory standardSqlSessionFactory
            (@Qualifier("standardDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "standardTransactionManager")
    @Primary
    public DataSourceTransactionManager standardTransactionManager
            (@Qualifier("standardDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "standardSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate standardSqlSessionTemplate
            (@Qualifier("standardSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
