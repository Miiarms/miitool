package com.miiarms.miitool.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Bean
    public MybatisParameterPlugin parameterPlugin(){
        return new MybatisParameterPlugin();
    }

}
