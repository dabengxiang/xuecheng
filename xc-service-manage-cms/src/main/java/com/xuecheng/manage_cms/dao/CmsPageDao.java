package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
public interface CmsPageDao extends MongoRepository<CmsPage,String>{

    List<CmsPage> findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
