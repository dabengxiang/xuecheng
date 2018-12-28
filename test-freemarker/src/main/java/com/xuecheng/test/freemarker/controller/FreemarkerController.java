package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * project name : xuecheng
 * Date:2018/12/28
 * Author: yc.guo
 * DESC:
 */
@Controller
public class FreemarkerController {

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String freeMarker(Map<String,Object> map){
        map.put("name","张三");
        Student stu1 = new Student();
        stu1.setAge(18);
        stu1.setName("小明");
        stu1.setMondy(100f);

        Student stu2 = new Student();

        stu2.setAge(19);
        stu2.setName("小强");
        stu2.setMondy(102f);

        List<Student>  stus= new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);


        map.put("stus",stus);

        Map<String, Object> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        
        map.put("stuMap",stuMap);

        return "test1";
        
    }
    

}
