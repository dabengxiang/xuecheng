package com.xuecheng.search.controller;

import com.xuecheng.api.EsCourseControllerApi;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date:2019/2/13
 * Author:gyc
 * Desc:
 */

@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {


    @Autowired
    private EsCourseService esCourseService;


    @Override
    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {

        esCourseService.list(page,size,courseSearchParam);

        return null;
    }
}
