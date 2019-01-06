package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date:2019/1/6
 * Author:gyc
 * Desc:
 */
public interface CourseControllerApi {

    public TeachplanNode findTeachplanList(String courseId);

    public ResponseResult addTeachplan( Teachplan teachplan);

}
