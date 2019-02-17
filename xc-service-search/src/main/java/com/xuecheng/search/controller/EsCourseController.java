package com.xuecheng.search.controller;

import com.xuecheng.api.EsCourseControllerApi;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Date:2019/2/13
 * Author:gyc
 * Desc:
 */

@RestController
@RequestMapping("/search/course")
@Slf4j
public class EsCourseController implements EsCourseControllerApi {


    @Autowired
    private EsCourseService esCourseService;


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult list(@PathVariable("page") int page,
                                    @PathVariable("size")int size,
                                    CourseSearchParam courseSearchParam) {

        try {
            return esCourseService.list(page,size,courseSearchParam);
        } catch (IOException e) {
            log.error("获取失败",e);
            return new QueryResponseResult(CommonCode.FAIL,null);
        }
    }
}
