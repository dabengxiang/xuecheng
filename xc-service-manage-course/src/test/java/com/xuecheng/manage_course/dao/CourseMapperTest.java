package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * project name : xuecheng
 * Date:2019/1/7
 * Author: yc.guo
 * DESC:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseMapperTest {

    
    @Autowired
    private CourseMapper courseMapper;
    
    @Test
    public void findCourseListPage() {
        CourseListRequest courseListRequest = new CourseListRequest();
        courseListRequest.setCompanyId("1");
        PageHelper.startPage(1,10);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        System.out.println(courseListPage);
    }
}