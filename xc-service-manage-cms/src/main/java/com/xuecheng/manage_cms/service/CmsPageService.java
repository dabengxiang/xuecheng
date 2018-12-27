package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageDao;
import com.xuecheng.manage_cms.dao.CmsSiteDao;
import com.xuecheng.manage_cms.dao.CmsTemplateDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;


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

    @Autowired
    private CmsSiteDao cmsSiteDao;
    
    @Autowired
    private CmsTemplateDao cmsTemplateDao;

    public Page<CmsPage> findList(int page, int size, QueryPageRequest queryPageRequest){

        if(page <= 0){
            page = 1;
        }
        page--;

        PageRequest of = PageRequest.of(page, size);

        CmsPage cmsPageTemplage = new CmsPage();

        if(StringUtils.isNotBlank(queryPageRequest.getSiteId())){
            cmsPageTemplage.setSiteId(queryPageRequest.getSiteId());

        }
        if(StringUtils.isNotBlank(queryPageRequest.getPageId())) {
            cmsPageTemplage.setPageId(queryPageRequest.getPageId());
        }

        if(StringUtils.isNotBlank(queryPageRequest.getPageAliase())) {
            cmsPageTemplage.setPageAliase(queryPageRequest.getPageAliase());

        }



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


    public List<Map<String,Object>> siteList() {
        List<CmsSite> all = cmsSiteDao.findAll();
        List<Map<String,Object>>  list = new ArrayList<>(0);
        if(all!=null){
            all.forEach(new Consumer<CmsSite>() {
                @Override
                public void accept(CmsSite cmsSite) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("siteId",cmsSite.getSiteId());
                    map.put("siteName",cmsSite.getSiteName());
                    list.add(map);
                }
            });
        }
        return list;
    }

    public List<Map<String,Object>> templateList(String siteId) {

        List<CmsTemplate> all = null;
        
        if(StringUtils.isNotBlank(siteId)){
            CmsTemplate cmsTemplate = new CmsTemplate();
            cmsTemplate.setSiteId(siteId);
             all = cmsTemplateDao.findAll(Example.of(cmsTemplate));
        }else{
             all = cmsTemplateDao.findAll();
        }
        
        
        List<Map<String,Object>>  list = new ArrayList<>(0);
        if(all!=null){
            all.forEach(new Consumer<CmsTemplate>() {
                @Override
                public void accept(CmsTemplate cmsTemplate) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("templateId",cmsTemplate.getTemplateId());
                    map.put("templateName",cmsTemplate.getTemplateName());
                    list.add(map);
                }
            });
        }
        return list;
        
    }

    public CmsPage findById(String id) {
        return cmsPageDao.findById(id).get();
    }

    public void edit(CmsPage cmsPage) {
        Optional<CmsPage> byId = cmsPageDao.findById(cmsPage.getPageId());
        if(byId.isPresent()){
            cmsPageDao.save(cmsPage);

        }else{
            throw  new RuntimeException("修改失败，不存在此id");
        }
    }

    public void delete(String id) {
        cmsPageDao.deleteById(id);
    }
}

