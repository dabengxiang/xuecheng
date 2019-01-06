package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Date:2019/1/6
 * Author:gyc
 * Desc:
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CourseService {


    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;


    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.findTeachplanList(courseId);
    }


    /**
     * 添加课程计划
     * @param teachplan
     */
    public void addTeachplan(Teachplan teachplan) {

        if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String parentid = teachplan.getParentid();
        String courseid = teachplan.getCourseid();
        Teachplan parentTeachPlan ;

        if(parentid==null){
            parentTeachPlan = getTeachplanRoot(courseid);
            teachplan.setParentid(parentTeachPlan.getId());
        }else{
            parentTeachPlan = teachplanMapper.selectByPrimaryKey(courseid);
        }


        String grade = parentTeachPlan.getGrade();
        if("1".equals(parentTeachPlan.getGrade())){
            teachplan.setGrade("2");
        }else{
            teachplan.setGrade("3");
        }
        teachplan.setStatus("0");//未发布

        teachplanMapper.insert(teachplan);

    }


    public Teachplan getTeachplanRoot(String courseId){

        Teachplan teachplanExample = new Teachplan();
        teachplanExample.setCourseid(courseId);
        teachplanExample.setParentid("0");
        List<Teachplan> teachplans = teachplanMapper.selectByExample(teachplanExample);
        if(teachplans == null || teachplans.isEmpty()){
            CourseBase courseBase = courseMapper.selectByPrimaryKey(courseId);
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseId);
            teachplan.setDescription(courseBase.getDescription());
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setStatus("0");
            teachplanMapper.insert(teachplan);
            return teachplan;

        }else{
            return teachplans.get(0);
        }





    }
}
