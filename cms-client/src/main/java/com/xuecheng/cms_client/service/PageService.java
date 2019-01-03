package com.xuecheng.cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cms_client.dao.CmsPageDao;
import com.xuecheng.cms_client.dao.CmsSiteDao;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * project name : xuecheng
 * Date:2019/1/2
 * Author: yc.guo
 * DESC:
 */
@Service
public class PageService {

    
    
    private static Logger logger =  LoggerFactory.getLogger(PageService.class);
    
    
    @Autowired
    private CmsPageDao cmsPageDao;

    @Autowired
    private CmsSiteDao cmsSiteDao;
    
    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    @Autowired
    private GridFSBucket gridFSBucket;
    
    
    public void savePageToServerPath(String pageId) {

        Optional<CmsPage> cmsPageOpt = cmsPageDao.findById(pageId);
        if(!cmsPageOpt.isPresent()){
            logger.error("this cmsPage is not existing : {}" ,pageId);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTXISTS);
        }
        CmsPage cmsPage = cmsPageOpt.get();

        String siteId = cmsPage.getSiteId();

        Optional<CmsSite> cmsSiteOptional = cmsSiteDao.findById(siteId);
        if(!cmsSiteOptional.isPresent()){
            logger.error("this cmssite is not existing : {}" ,siteId);
            ExceptionCast.cast(CmsCode.CMS_SITE_ADDPAGE_NOTXISTS);
        }
        CmsSite cmsSite = cmsSiteOptional.get();

        String path = cmsSite.getSitePhysicalPath()+ cmsPage.getPagePhysicalPath()+ cmsPage.getPageName();
//         path = "C:/Users/83673/Desktop/test.html";
        logger.info("path is : {}" , path);
        
        String htmlFileId = cmsPage.getHtmlFileId();

        try (
                InputStream inputStream = getFileById(htmlFileId);

                FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
                
                ){

            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    public InputStream getFileById(String fileId) throws IOException {

        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));

        if(gridFSFile==null){
            ExceptionCast.cast(CmsCode.CMS_HTML_FILE_NOTXISTS);
        }
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        return gridFsResource.getInputStream();
        


    }
    
    
    
    
    
    
    
 
    
    
    

}
