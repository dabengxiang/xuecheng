package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date:2019/1/6
 * Author:gyc
 * Desc:
 */
public interface CourseControllerApi {

    public TeachplanNode findTeachplanList(String courseId);

    public ResponseResult addTeachplan( Teachplan teachplan);


    @ApiOperation("查询我的课程列表")
   QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程")
    public ResponseResult addCourseBase( CourseBase courseBase);
    
    @ApiOperation("根据课程id来查询基础课程页面")
    public CourseBase getCourseBaseById(String courseId);

    @ApiOperation("查询课程营销")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("更新课程营销")
    public ResponseResult updateCourseMarket(CourseMarket courseMarket );

 }
