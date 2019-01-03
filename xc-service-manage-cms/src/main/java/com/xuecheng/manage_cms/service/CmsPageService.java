package com.xuecheng.manage_cms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.message.BrokerMessageContant;
import com.xuecheng.framework.domain.message.BrokerMessageLog;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitMqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigDao;
import com.xuecheng.manage_cms.dao.CmsPageDao;
import com.xuecheng.manage_cms.dao.CmsSiteDao;
import com.xuecheng.manage_cms.dao.CmsTemplateDao;
import com.xuecheng.manage_cms.utils.FastJsonConvertUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
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
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private CmsPageDao cmsPageDao;

    @Autowired
    private CmsSiteDao cmsSiteDao;
    
    @Autowired
    private CmsTemplateDao cmsTemplateDao;

    @Autowired
    private CmsConfigDao cmsConfigDao;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    ObjectMapper objectMapper ;

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
             ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTXISTS);
        }
    }

    public void delete(String id) {
        cmsPageDao.deleteById(id);
    }

    /**
     * 查找config数据通过id
     * @param id
     */
    public CmsConfig findCmsConfigById(String id){
        Optional<CmsConfig> cmsConfig = cmsConfigDao.findById(id);
        if(cmsConfig.isPresent()){
            return cmsConfig.get();

        }else {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        return null;

    }

    /**
     * 通过id获取预览信息
     * @param pageId
     */
    public String getPageHtml(String pageId) throws Exception {
        Optional<CmsPage> cmsPageOpt = cmsPageDao.findById(pageId);
        if(!cmsPageOpt.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTXISTS);
        }
        CmsPage cmsPage = cmsPageOpt.get();

        String dataUrl = cmsPage.getDataUrl();
        Map<String, Object> data = getModelDataByDataUrl(dataUrl);
        String templateId = cmsPage.getTemplateId();

        String templateStr = getTemplateStr(getTemplateFileId(templateId));
        String content = generateHtml(templateStr, data);
        return content;
    }



    public String getTemplateFileId(String templateId){
        Optional<CmsTemplate> byId = cmsTemplateDao.findById(templateId);
        return byId.get().getTemplateFileId();

    }




    public String getTemplateStr(String fsId) throws IOException {
        GridFSFile gridfs = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fsId)));
        GridFSDownloadStream gridStream = gridFSBucket.openDownloadStream(gridfs.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridfs, gridStream);
        InputStream inputStream = gridFsResource.getInputStream();
        return IOUtils.toString(inputStream,"utf-8");


    }

    /**
     * 通过模板和数据生成html
     * @param templateStr
     * @param data
     */
    public String  generateHtml(String templateStr,Map<String,Object> data) throws IOException, TemplateException {

        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateStr);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template", "utf-8");


        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        return content;



    }


    /**
     *  获取模板信息
     * @param templateId
     * @return
     */
    public String getTemplate( String templateId) throws IOException {
        Optional<CmsTemplate> cmsTemplateOpt = cmsTemplateDao.findById(templateId);
        if(!cmsTemplateOpt.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplate cmsTemplate = cmsTemplateOpt.get();
        String templateFileId = cmsTemplate.getTemplateFileId();

        GridFSFile gridFs = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFs.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFs, gridFSDownloadStream);
        InputStream inputStream = gridFsResource.getInputStream();
        String content = IOUtils.toString(inputStream, "utf-8");
        return content;
    }


    /**
     * 通过dataurl得到数据
     * @param dataUrl
     * @return
     */
    public Map<String,Object> getModelDataByDataUrl(String dataUrl){
        Map<String,Object> map = restTemplate.getForObject(dataUrl, Map.class);
        return map;
    }

    /**
     * 保存文件
     * @param pageId
     * @param pageHtml
     * @throws IOException
     */
    public CmsPage saveFile(String pageId,String pageHtml) throws IOException {

        Optional<CmsPage> cmsPageOpt = cmsPageDao.findById(pageId);
        if(!cmsPageOpt.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NOTXISTS);
        }
        CmsPage cmsPage = cmsPageOpt.get();

        InputStream inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
        ObjectId store = gridFsTemplate.store(inputStream, "utf-8");

        cmsPage.setHtmlFileId(store.toString());
        cmsPageDao.save(cmsPage);
        
        return cmsPage;
        
    }

    /**
     * 发布页面
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) throws Exception {
        String pageHtml = getPageHtml(pageId);
        CmsPage cmsPage = saveFile(pageId, pageHtml);
        sendPostPage(cmsPage);
        return ResponseResult.SUCCESS();
    }


    /**
     * 往mq中发信息
    * @param cmsPage
     */
    public void sendPostPage(CmsPage cmsPage) throws JsonProcessingException {
        
        String siteId = cmsPage.getSiteId();
        Map<String, Object> map = new HashMap<>();
        map.put("pageId",cmsPage.getPageId());
//        byte[] bytes = objectMapper.writeValueAsBytes(map);
//
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/json");
//        Message message = new Message(bytes, messageProperties);
//        CorrelationData correlationData = new CorrelationData();
//
//        correlationData.setId();

        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();


        rabbitTemplate.convertAndSend(RabbitMqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,map);
    }


    /**
     * 插入一个默认的brokerMessage
     */
//    public void insertDefaultBorkerMessage(CmsPage cmsPage){
//        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
//        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
//        brokerMessageLog.setCreateTime(new Date());
//        brokerMessageLog.setUpdateTime(new Date());
//        brokerMessageLog.setStatus(BrokerMessageContant.ORDER_SENDING);
////        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, BrokerMessageContant.ORDER_TIMEOUT));
//        brokerMessageLog.setTryCount(0);
//        
//
//    }
}

