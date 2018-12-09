package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date:2018/12/8
 * Author:gyc
 * Desc:
 */
@RequestMapping("cms-page")
@RestController
public class CmsPageController {



    @Autowired
    private CmsPageService cmsPageService;

    @RequestMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size , QueryPageRequest queryPageRequest){

        Page<CmsPage> list = cmsPageService.findList(page, size, queryPageRequest);

        return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult<CmsPage>(list.getContent(),list.getTotalElements()));

    }


}
