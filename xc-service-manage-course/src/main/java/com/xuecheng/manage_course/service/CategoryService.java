package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * project name : xuecheng
 * Date:2019/1/8
 * Author: yc.guo
 * DESC:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;


    public CategoryNode findList() {
        return categoryMapper.findCategoryList();
    }
}
