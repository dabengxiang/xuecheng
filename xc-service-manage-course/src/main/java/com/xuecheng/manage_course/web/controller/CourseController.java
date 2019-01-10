package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.cms.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Date:2019/1/6
 * Author:gyc
 * Desc:
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;
    
    

    @GetMapping("/teachplan/list/{courseId}")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        TeachplanNode teachplanList = courseService.findTeachplanList(courseId);
        return teachplanList;
    }


    @PostMapping("/teachplan/add")
    @Override
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan){

        courseService.addTeachplan(teachplan);
        return ResponseResult.SUCCESS();
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page, 
                                              @PathVariable("size") int size,
                                              @ModelAttribute  CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
        
    }


    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
         courseService.addCourseBase(courseBase);
         return ResponseResult.SUCCESS();
    }

    @Override
    @GetMapping("/get/base/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) {
       return  courseService.getCourseBaseById(courseId);
    }

    @GetMapping("/get/market/{courseId}")
    @Override
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    @PostMapping("/market/add")
    public ResponseResult updateCourseMarket(@RequestBody  CourseMarket courseMarket) {
         courseService.updateCourseMarket(courseMarket);
         return ResponseResult.SUCCESS();
    }


}
