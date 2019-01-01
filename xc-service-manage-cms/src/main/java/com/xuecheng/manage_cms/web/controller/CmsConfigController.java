package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.exception.CustomerException;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
@RequestMapping("/cms/config")
@RestController
public class CmsConfigController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CmsPageService cmsPageService;



    @GetMapping("/getModel/{id}")
    public CmsConfig findCmsConfigById(@PathVariable("id") String id) {
        return cmsPageService.findCmsConfigById(id);
    }


}