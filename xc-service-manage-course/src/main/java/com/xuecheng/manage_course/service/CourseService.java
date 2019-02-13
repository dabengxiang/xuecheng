package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CoursePubRepository coursePubRepository;


    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;


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

    /**
     * 为课程添加图片
     * @param courseId
     * @param pic
     */
    public void coursepicAdd(String courseId, String pic) {
        CoursePic coursePic = null ;
        Optional<CoursePic> dbCoursePic = coursePicRepository.findById(courseId);
        if(dbCoursePic.isPresent()){
            coursePic = dbCoursePic.get();
        }else{
            coursePic = new CoursePic();
           coursePic.setCourseid(courseId);

        }
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
    }


    /**
     * 查询课程图片
     * @param courseId
     * @return
     */
    public CoursePic coursepicList(String courseId) {
         return coursePicRepository.findById(courseId).get();
    }

    /**
     * 删除课程图片
     * @param courseId
     * @return
     */
    public ResponseResult coursepicDelete(String courseId) {
        Long i = coursePicRepository.deleteByCourseid(courseId);
        if(i > 0){
            return ResponseResult.SUCCESS();
        }else{
            return ResponseResult.FAIL();
        }
    }


    /**
     * 获取课程试图
     * @param id
     * @return
     */
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();

        Optional<CourseBase> baseOpt = courseBaseRepository.findById(id);
        if(baseOpt.isPresent()){
            courseView.setCourseBase(baseOpt.get());
        }else {
            ExceptionCast.cast(CourseCode.COURSE_ERROR_COURSEID);
        }

        Optional<CoursePic> picOpt = coursePicRepository.findById(id);
        if(picOpt.isPresent()){
            courseView.setCoursePic(picOpt.get());
        }




        Optional<CourseMarket> marketOpt = courseMarketRepository.findById(id);
        if(marketOpt.isPresent()){
            courseView.setCourseMarket(marketOpt.get());
        }

        courseView.setTeachplanNode(teachplanMapper.findTeachplanList(id));

        return courseView;
    }


    /**
     * 封装一个通用cmsPage
     * @param courseId
     * @return
     */
    private CmsPage getDefaultCmsPage(String courseId){
        Optional<CourseBase> opt = courseBaseRepository.findById(courseId);
        if(!opt.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_ERROR_COURSEID);
        }

        CourseBase courseBase = opt.get();

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageName(courseId+".html");
        return cmsPage;

    }

    /**
     * 预览
     * @param id
     * @return
     */
    public CoursePublishResult preview(String courseId) {

        CmsPage cmsPage = getDefaultCmsPage(courseId);

        CmsPageResult cmsPageResult = cmsPageClient.saveAndUpdate(cmsPage);
        if(cmsPageResult.isSuccess()){

            CmsPage returnCmsPage = cmsPageResult.getCmsPage();

            String pageId = returnCmsPage.getPageId();
            String pageUrl = previewUrl+pageId;

            return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);

        }else{
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

    }


    /**
     * 发布
     * @param id
     * @return
     */
    public CoursePublishResult postPageQuick(String courseId) {

        CmsPage cmsPage = getDefaultCmsPage(courseId);

        CmsPostPageResult cmsPostPageResult = cmsPageClient.publishPage(cmsPage);

        if(cmsPostPageResult.isSuccess()){

            CoursePub coursePub = createCoursePub(courseId);
            saveCoursepub(courseId,coursePub);

            setCoursePubState(courseId);
            String pageUrl = cmsPostPageResult.getPageUrl();
            return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
        }

            return new CoursePublishResult(CommonCode.FAIL,null);

    }


    /**
     * 创建coursepub
     * @return
     */
    public CoursePub createCoursePub(String courseId){

        CourseView courseView = getCourseView(courseId);
        CourseBase courseBase = courseView.getCourseBase();
        CoursePic coursePic = courseView.getCoursePic();
        CourseMarket courseMarket = courseView.getCourseMarket();
        TeachplanNode teachplanNode = courseView.getTeachplanNode();
        CoursePub coursePub = new CoursePub();

        BeanUtils.copyProperties(courseBase,coursePub);
        BeanUtils.copyProperties(coursePic,coursePub);
        BeanUtils.copyProperties(courseMarket,coursePub);

        if(teachplanNode!=null){
            coursePub.setTeachplan(JSONObject.toJSONString(teachplanNode));
        }

        return coursePub;

    }


    /**
     * 设置发布时间
     * @param coursePub
     */
    public void setCoursePubCurTime(CoursePub coursePub){
        Date currentDate = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate);
        coursePub.setTimestamp(currentDate);
        coursePub.setPubTime(currentStr);
    }


    /**
     * 保存coursePub
     */
    public void saveCoursepub(String courseId,CoursePub coursePub){
        Date currentDate = new Date();
        String currentStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate);
        coursePub.setTimestamp(currentDate);
        coursePub.setPubTime(currentStr);
        coursePub.setId(courseId);
        coursePubRepository.save(coursePub);
    }


    /**
     * 设置课程为已发布状态
     * @param courseId
     */
    private void setCoursePubState(String courseId){
        CourseBase courseBaseById = this.getCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);

    }
}
