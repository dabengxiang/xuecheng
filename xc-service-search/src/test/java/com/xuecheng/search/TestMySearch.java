package com.xuecheng.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.xml.QueryBuilderFactory;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Date:2019/2/12
 * Author:gyc
 * Desc:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMySearch {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 查询所有和排序
     * @throws IOException
     */
    @Test
    public void searchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        /////////////////////////////////// 排序  ////////////////////////////////////////////////////////
        sourceBuilder.sort("studymodel" , SortOrder.DESC );
        sourceBuilder.sort("price", SortOrder.ASC);

        ///////////////////////////////////////////////////////////////////////////////////////////


        outPutResult(searchResponse);

    }




    /**
     * 分页查询
     * @throws IOException
     */
    @Test
    public void searchPage() throws IOException{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.from(0);    // 这个两个是关键
        sourceBuilder.size(2);   //
        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);
    }


    /**
     * 条件查询
     * @throws IOException
     */
    @Test
    public void searchTerm() throws IOException{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        sourceBuilder.query(QueryBuilders.termQuery("name","基础"));  //这个是关键
//        sourceBuilder.from(0);
//        sourceBuilder.size(2);
        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);

    }


    /**
     * 通过多个id来找
     * @throws IOException
     */
    @Test
    public void searchByIds() throws IOException {
        String ids = "1,2,4";
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();

        sourceBuilder.query(QueryBuilders.termsQuery("_id",idList));  //注意这里是termsQuery 多了一个s的
//        sourceBuilder.from(0);
//        sourceBuilder.size(2);
        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);
    }

    /**
     * 分词查询
     */
    @Test
    public void searchByMatch() throws IOException {

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        //或者匹配
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.OR));
        //按比例匹配 100% = 上边那个了
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").minimumShouldMatch("80%"));


        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
       //提高权重
        multiMatchQueryBuilder.field("name",10);
        sourceBuilder.query(multiMatchQueryBuilder);

        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});


        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);

    }


    //bool查询
    @Test
    public void searchBybool() throws IOException {

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        //或者匹配
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.OR));
        //按比例匹配 100% = 上边那个了
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").minimumShouldMatch("80%"));


        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
        //提高权重
        multiMatchQueryBuilder.field("name",10);

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);


        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});


        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);

    }


    /**
     * 过滤器使用和排序
     * @throws IOException
     */
    @Test
    public void  filterQuery() throws IOException {

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();
        //或者匹配
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.OR));
        //按比例匹配 100% = 上边那个了
//        sourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").minimumShouldMatch("80%"));


        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
        //提高权重
        multiMatchQueryBuilder.field("name",10);


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);


        /////////////////////////////////  过滤 //////////////////////////////////////////////////////////

        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});

        ///////////////////////////////////////////////////////////////////////////////////////////



        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);
    }


    /**
     * 高亮操作
     */

    /**
     * 分词查询
     */
    @Test
    public void highlight() throws IOException {

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = searchRequest.source();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").minimumShouldMatch("50%");
        //提高权重
        multiMatchQueryBuilder.field("name",10);
        sourceBuilder.query(multiMatchQueryBuilder);

        sourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});


        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag1>");
        highlightBuilder.postTags("</tag2>");

        highlightBuilder.field(new HighlightBuilder.Field("name")).field(new HighlightBuilder.Field("description"));

        sourceBuilder.highlighter(highlightBuilder);


        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        outPutResult(searchResponse);

    }




    /**
     *
     * 对结果输出
     * @param searchResponse
     */
    public void outPutResult(SearchResponse searchResponse){
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {



            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Object name = sourceAsMap.get("name");
            Object description = sourceAsMap.get("description");
            Object studymodel = sourceAsMap.get("studymodel");


            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null){
                for (Map.Entry<String, HighlightField> stringHighlightFieldEntry : highlightFields.entrySet()) {
                    String key = stringHighlightFieldEntry.getKey();
                    HighlightField value = stringHighlightFieldEntry.getValue();
                    Text[] fragments = value.getFragments();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Text fragment : fragments) {
                        stringBuilder.append(fragment.string());
                    }

                    if(key.equals("name")){
                        name = stringBuilder.toString();
                    }

                    if(key.equals("description")){
                        description = stringBuilder.toString();
                    }





                }

            }


            System.out.println("index:" + index);
            System.out.println("type:" + type);
            System.out.println("id:" + id);
            System.out.println("score:" + score);
            System.out.println("sourceAsString:" + sourceAsString);
            System.out.println("name:" + name);
            System.out.println("description:" + description);
            System.out.println("studymodel:" + studymodel);




        }
    }




}
