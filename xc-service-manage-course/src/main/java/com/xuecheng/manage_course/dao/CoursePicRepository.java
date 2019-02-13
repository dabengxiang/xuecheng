package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date:2019/2/4
 * Author:gyc
 * Desc:
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    Long deleteByCourseid(String courseId);
}
