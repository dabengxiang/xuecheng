package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CmsPageService {

    @Autowired
    private CmsPageDao cmsPageDao;

    public Page<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest){

        if(page <= 0){
            page = 1;
        }
        page--;

        PageRequest of = PageRequest.of(page, size);

        CmsPage cmsPageTemplage = new CmsPage();

        cmsPageTemplage.setSiteId(queryPageRequest.getSiteId());
        cmsPageTemplage.setPageId(queryPageRequest.getPageId());
        cmsPageTemplage.setPageAliase(queryPageRequest.getPageAliase());


        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",
                new ExampleMatcher.GenericPropertyMatcher().contains());


        Example<CmsPage> cmsPageExample =  Example.of(cmsPageTemplage,exampleMatcher);

        return  cmsPageDao.findAll(cmsPageExample,of);
    }

    public void add(CmsPage cmsPage) {
        cmsPage.setPageId(null);
        List<CmsPage> list =  cmsPageDao.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(),cmsPage.getPageWebPath());
        if(list== null || list.isEmpty()){
            cmsPageDao.save(cmsPage);
        }else{
            throw new RuntimeException("数据库已经存在了相同的数据了");
        }
    }
    
    
}

