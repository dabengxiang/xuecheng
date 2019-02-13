package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

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


    @ApiOperation("为课程添加图片")
    public ResponseResult coursepicAdd(String courseId,String pic) ;


    @ApiOperation("查询课程图片")
    public CoursePic coursepicList( String courseId) ;

    @ApiOperation("删除课程图片")
    public ResponseResult coursepicDelete( String courseId);


    @ApiOperation("课程试图")
    public CourseView courseView(String id);

    @ApiOperation("页面预览")
    public CoursePublishResult preview(String id);

   @ApiOperation("页面发布")
   public CoursePublishResult publish(String id);



 }
