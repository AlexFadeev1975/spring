package org.example.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@ConfigurationProperties(prefix = "spring.datasource")
public class ContactProperties {


    @Bean("dataSource")

    public DataSource dataSource() {

        return DataSourceBuilder
                .create()
                .build();
    }

}
