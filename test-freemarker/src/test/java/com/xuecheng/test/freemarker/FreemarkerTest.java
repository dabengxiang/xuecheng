package com.xuecheng.test.freemarker;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/29
 * Author:gyc
 * Desc:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {


    @Test
    public void  testGenerateHtml() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String path = this.getClass().getResource("/").getPath();
        System.out.println(path);
        configuration.setDirectoryForTemplateLoading(new File( path + "/templates/"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("test1.ftl");

        Map<String, Object> data = getData();

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream("d:/test1.html");
        IOUtils.copy(inputStream,outputStream);
    }


    @Test
    public void  testStringGenerateHtml() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String str = "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hello ${name}!\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",str);
        configuration.setTemplateLoader(stringTemplateLoader);

        Template template = configuration.getTemplate("template", "utf-8");


        Map<String, Object> data = getData();

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        System.out.println(content);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream("d:/test2.html");
        IOUtils.copy(inputStream,outputStream);



    }

    public Map<String,Object> getData(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","张三");

        return map;
    }






}
