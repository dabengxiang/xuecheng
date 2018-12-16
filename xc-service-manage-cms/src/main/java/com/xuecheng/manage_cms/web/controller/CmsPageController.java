package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
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
@RequestMapping("cms")
@RestController
public class CmsPageController extends BaseController implements CmsPageControllerApi {

    
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size , QueryPageRequest queryPageRequest){

        Page<CmsPage> list = cmsPageService.findList(page, size, queryPageRequest);

        return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult<CmsPage>(list.getContent(),list.getTotalElements()));

    }

    @Override
    @PostMapping("/add")
    public ResponseResult add(@RequestBody  CmsPage cmsPage) {

        try {
            cmsPageService.add(cmsPage);
            return ResponseResult.SUCCESS();
        }catch (Exception e){
            logger.error(this.getClass().toString(),e);
            return ResponseResult.FAIL();
        }
    }

    @Override
    @GetMapping("/siteList")
    public List<Map<String, Object>> siteList() {
        return cmsPageService.siteList();
    }


}
