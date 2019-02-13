package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.CommonResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Date:2019/2/7
 * Author:gyc
 * Desc:
 */
@FeignClient(value = "xc-service-manage-cms")
public interface CmsPageClient {

    @GetMapping("/cms/find/{id}")
    public CommonResponseResult findById(@PathVariable("id") String id) ;


    @GetMapping("/cms/save")
    public CmsPageResult saveAndUpdate(@RequestBody CmsPage cmsPage);

    @GetMapping("/cms/postPageQuick")
    CmsPostPageResult publishPage(@RequestBody CmsPage cmsPage);
}
