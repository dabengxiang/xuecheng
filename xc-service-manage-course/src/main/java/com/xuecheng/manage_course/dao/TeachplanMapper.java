package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * project name : xuecheng
 * Date:2019/1/3
 * Author: yc.guo
 * DESC:
 */
@Mapper
public interface TeachplanMapper  extends MyMapper<Teachplan>{
    
    public TeachplanNode findTeachplanList(String courseId);



}
