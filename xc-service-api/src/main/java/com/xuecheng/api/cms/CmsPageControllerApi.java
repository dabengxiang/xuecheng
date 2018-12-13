package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
