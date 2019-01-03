package com.elens.data.rbces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RbcEsApplication {

    public static void main(String[] args)  {
        SpringApplication.run(RbcEsApplication.class, args);

    }


}

