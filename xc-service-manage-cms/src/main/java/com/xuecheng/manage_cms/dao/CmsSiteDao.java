package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Date:2018/12/16
 * Author:gyc
 * Desc:
 */
public interface CmsSiteDao extends MongoRepository<CmsSite,String> {


}
