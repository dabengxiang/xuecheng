package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Date:2018/12/30
 * Author:gyc
 * Desc:
 */

@Controller
public class CmsPagePreviewController {

    @Autowired
    private CmsPageService cmsPageService;


    @RequestMapping(value="/cms/preview/{pageId}",method = RequestMethod.GET)
    public void previewPage(@PathVariable("pageId") String pageId, HttpServletResponse response) throws Exception {
            String content = cmsPageService.getPageHtml(pageId);
            ServletOutputStream outputStream = response.getOutputStream();
            //outputStream.write("outputStream".getBytes());
            outputStream.write(content.getBytes("utf-8"));
    }

}
