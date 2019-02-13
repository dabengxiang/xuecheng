package com.xuecheng.api;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * Date:2019/2/13
 * Author:gyc
 * Desc:
 */
public interface EsCourseControllerApi {

    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);
}