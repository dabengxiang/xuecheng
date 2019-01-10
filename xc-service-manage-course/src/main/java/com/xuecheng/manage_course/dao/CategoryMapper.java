package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * project name : xuecheng
 * Date:2019/1/8
 * Author: yc.guo
 * DESC:
 */

@Mapper
public interface CategoryMapper {

    public CategoryNode findCategoryList();

}
