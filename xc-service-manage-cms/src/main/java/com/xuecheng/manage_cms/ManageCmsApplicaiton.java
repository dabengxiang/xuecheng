package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
@SpringBootApplication
@EntityScan(basePackages = "com.xuecheng.framework.domain")
@ComponentScan(basePackages = "com.xuecheng.manage_cms")
@ComponentScan(basePackages = "com.xuecheng.api")
public class ManageCmsApplicaiton {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplicaiton.class);

    }
}