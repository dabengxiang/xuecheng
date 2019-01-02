package com.xuecheng.cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * project name : xuecheng
 * Date:2019/1/2
 * Author: yc.guo
 * DESC:
 */
public interface CmsPageDao extends MongoRepository<CmsPage,String> {
}
