package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * project name : xuecheng
 * Date:2018/12/13
 * Author: yc.guo
 * DESC:
 */
public interface CmsPageControllerApi {

    public QueryResponseResult findList(@PathVariable("page") int page, 
                                        @PathVariable("size") int size ,
                                        QueryPageRequest queryPageRequest);


    public ResponseResult add(CmsPage cmsPage);





    public List<Map<String,Object>> siteList();


    public List<Map<String, Object>> templateList(@RequestParam("siteId") String siteID) ;


    public ResponseResult postPage(String pageId) throws Exception;

    public ResponseResult saveAndUpdate( CmsPage cmsPage) ;


    @ApiOperation("一键发布")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
