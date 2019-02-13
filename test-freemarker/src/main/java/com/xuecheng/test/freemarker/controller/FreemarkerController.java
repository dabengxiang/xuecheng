package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;


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


    @RequestMapping("/course")
    public String Course(Map<String,Object> map){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/297e7c7c62b888f00162b8a7dec20000", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "course";
    }
    

}
