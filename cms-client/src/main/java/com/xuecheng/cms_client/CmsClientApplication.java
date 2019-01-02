package com.xuecheng.cms_client;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * project name : xuecheng
 * Date:2019/1/2
 * Author: yc.guo
 * DESC:
 */
@SpringBootApplication
@EntityScan(basePackages = "com.xuecheng.framework.domain")
@ComponentScan(basePackages = "com.xuecheng.api")
@ComponentScan(basePackages = "com.xuecheng.framework.exception")
public class CmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsClientApplication.class);
    }

    

}
