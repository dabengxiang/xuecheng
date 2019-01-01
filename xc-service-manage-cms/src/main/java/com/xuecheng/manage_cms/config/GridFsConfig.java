package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date:2018/12/30
 * Author:gyc
 * Desc:
 */
@Configuration
public class GridFsConfig {

    @Value("${spring.data.mongodb.database}")
    private String db;


    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(db);
        return GridFSBuckets.create(database);
    }

}
