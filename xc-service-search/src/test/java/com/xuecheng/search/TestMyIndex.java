package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date:2019/2/11
 * Author:gyc
 * Desc:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMyIndex {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     *
     * "properties": {
     "description": {
     "type": "text",
     "analyzer": "ik_max_word",
     "search_analyzer": "ik_smart"
     },
     "name": {
     "type": "text",
     "analyzer": "ik_max_word",
     "search_analyzer": "ik_smart"
     },
     "pic": {
     "type": "text",
     "index": false
     },
     "price": {
     "type": "scaled_float",
     "scaling_factor": 100
     },
     "studymodel": {
     "type": "text"
     },
     "timestamp": {
     "type": "date",
     "format": "yyyy‐MM‐dd HH:mm:ss||yyyy‐MM‐dd"
     }
     }
     */

    /**
     *
     * @return
     * @throws IOException
     */

    private XContentBuilder getMapping() throws IOException {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject()
                .startObject("properties")
                    .startObject("name").field("type","text").field("analyzer","ik_max_word").field("search_analyzer","ik_smart").endObject()
                    .startObject("description").field("type","text").field("analyzer","ik_max_word").field("search_analyzer","ik_smart").endObject()
//                    .startObject("price").field("type","scaled_float").field("scaling_factor","100").endObject()
                    .startObject("price").field("type","float").endObject()
                    .startObject("studymodel").field("type","keyword").endObject()
                    .startObject("timestamp").field("type","date").field("format","yyyy-MM-dd HH:mm:ss||yyyy‐MM‐dd").endObject()
                    .startObject("pic").field("type","text").field("index","false").endObject()
                .endObject()
                .endObject();

        return xContentBuilder;

    }

    @Test
    public void createMapping() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        createIndexRequest.settings(Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 0));
        createIndexRequest.mapping("doc",getMapping());
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    @Test
    public void deleteMapping() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }


    /**
     * 添加文档
     * @throws IOException
     */
    @Test
    public void testAddDoc() throws IOException {

        Map<String, Object> source = new HashMap<>();

        source.put("name","天龙八部2");
        source.put("description","金庸作品2");
        source.put("pic","http://4444.jpg");
        source.put("studymodel","102");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = dateFormat.format(new Date());
        System.err.println(timeStr);
        source.put("timestamp", timeStr);
        source.put("price",3.34);


        IndexRequest indexRequest = new IndexRequest("xc_course","doc");

        indexRequest.source(source);


        IndexResponse indexResponse = restHighLevelClient.index(indexRequest); //主要这里插入
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }


    /**
     * 一键创建，删除后在创建
     * @throws Exception
     */
    @Test
    public void quickCreate() throws  Exception{
        deleteMapping();
        createMapping();
    }


    /**
     * 通过id查询文档
     * @throws IOException
     */
    @Test
    public void getDocById() throws IOException {

        GetRequest getRequest = new GetRequest("xc_course", "doc", "lNm74GgBGBFlJhrzdjwc");
        GetResponse documentFields = restHighLevelClient.get(getRequest);
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        System.out.println(sourceAsMap);
    }







}
