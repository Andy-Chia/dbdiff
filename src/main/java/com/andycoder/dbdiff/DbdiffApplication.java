package com.andycoder.dbdiff;

import com.andycoder.dbdiff.service.DatabaseComparator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = {"com.andycoder.dbdiff.dao"})
public class DbdiffApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DbdiffApplication.class, args);

    }

}
