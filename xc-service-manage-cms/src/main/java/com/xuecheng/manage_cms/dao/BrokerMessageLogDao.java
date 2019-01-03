package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.message.BrokerMessageLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * project name : xuecheng
 * Date:2019/1/3
 * Author: yc.guo
 * DESC:
 */
public interface BrokerMessageLogDao extends MongoRepository<BrokerMessageLog,String> {
}
