package com.xuecheng.manage_cms.mongo;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms.ManageCmsApplicaitonTest;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date:2018/12/30
 * Author:gyc
 * Desc:
 */
public class MongoFileTest  extends ManageCmsApplicaitonTest{


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;



    @Test
    public void saveFile() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("E:\\黑马在线视频资料\\学成在线\\9. 课程预览 Eureka Feign\\资料\\课程详情页面模板\\course.ftl");
        ObjectId id = gridFsTemplate.store(inputStream, "课程详情模板文件");
        System.out.println(id.toString());
    }


    @Test
    public void getFile() throws IOException {
        String fileId = "5c2829d8e5f0a41f6041122b";
        GridFSFile gridfs = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridfs.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridfs, gridFSDownloadStream);
        InputStream inputStream = gridFsResource.getInputStream();
        String content = IOUtils.toString(inputStream,"utf-8");
        System.out.println(content);


    }

}
