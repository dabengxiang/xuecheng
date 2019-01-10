package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    
    @Autowired
    private TeachplanRepository teachplanRepository;
    
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    
    @Autowired
    private CourseMarketRepository courseMarketRepository;


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
            parentTeachPlan = teachplanRepository.getOne(parentid);
        }


        String grade = parentTeachPlan.getGrade();
        if("1".equals(parentTeachPlan.getGrade())){
            teachplan.setGrade("2");
        }else{
            teachplan.setGrade("3");
        }
        teachplan.setStatus("0");//未发布

        teachplanRepository.save(teachplan);

    }


    public Teachplan getTeachplanRoot(String courseId){

        List<Teachplan> teachplans = teachplanRepository.findByCourseidAndParentid(courseId,"0");
        if(teachplans == null || teachplans.isEmpty()){
            CourseBase courseBase = courseBaseRepository.getOne(courseId);
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setCourseid(courseId);
            teachplan.setDescription(courseBase.getDescription());
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan;

        }else{
            return teachplans.get(0);
        }





    }

    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
//        courseListRequest.setCompanyId("1");
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        return new QueryResponseResult(CommonCode.SUCCESS,
                new QueryResult(courseListPage.getResult(),courseListPage.getTotal()));
    }


    /**
     * 添加课程
     * @param courseBase
     */
    public void addCourseBase(CourseBase courseBase) {
        //todo: 不能写死的这里
        courseBase.setCompanyId("1");
        courseBaseRepository.save(courseBase); 
     

        
    }

    /**
     * 查询课程
     * @param courseId
     */
    public CourseBase getCourseBaseById(String courseId) {
        
       return  courseBaseRepository.findById(courseId).get();
    }

    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        return optional.isPresent() ? optional.get() : null;
        
    }

    public void updateCourseMarket(CourseMarket courseMarket) {
        courseMarketRepository.save(courseMarket);
    }
}
