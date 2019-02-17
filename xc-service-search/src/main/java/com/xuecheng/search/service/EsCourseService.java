package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date:2019/2/13
 * Author:gyc
 * Desc:
 */
@Transactional
@Service
public class EsCourseService  {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Value("${xuecheng.course.index}")
    private String indexName;

    @Value("${xuecheng.course.type}")
    private String typeName;


    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) throws IOException {

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);
        SearchSourceBuilder sourceBuilder = searchRequest.source();

        if(page<1){
            page = 1;
        }

        sourceBuilder.from((page-1) * size);
        sourceBuilder.size(size);

        String keyword = courseSearchParam.getKeyword();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();



        //关键字
        if(StringUtils.isNotBlank(keyword)){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "name", "description","teachplan").minimumShouldMatch("70%");
            multiMatchQueryBuilder.boost(10); //提过权重
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }else{
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(matchAllQueryBuilder);
        }

        //难度的设置
        if(StringUtils.isNotBlank(courseSearchParam.getMt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));

        }

        if(StringUtils.isNotBlank(courseSearchParam.getSt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));

        }


        //价格区间
        if(courseSearchParam.getPrice_min()!=null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(courseSearchParam.getPrice_min()));
        }

        if(courseSearchParam.getPrice_max()!=null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(courseSearchParam.getPrice_max()));

        }



        //高亮设置
        HighlightBuilder highlighter = new HighlightBuilder();
        highlighter.preTags("<font class='eslight'>");
        highlighter.postTags("</font>");
        highlighter.field(new HighlightBuilder.Field("name"));
        sourceBuilder.highlighter(highlighter);

//        sourceBuilder.fetchSource(new String[]{"name"} , new String[]{});


        sourceBuilder.query(boolQueryBuilder);



        SearchResponse search = restHighLevelClient.search(searchRequest);

        SearchHits hits = search.getHits();

        List<CoursePub> returnList = new ArrayList<>();

        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();


            CoursePub coursePub = new CoursePub();
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            String name = (String) sourceAsMap.get("name");
            String pic = (String) sourceAsMap.get("pic");

            Double price = (Double) sourceAsMap.get("price");
            Double old_price = (Double) sourceAsMap.get("old_price");


            String highLightName = getHiglitFieldStr(highlightFields, "name");
            if(highLightName!=null){
                name = highLightName;
            }

            coursePub.setId(id);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setPrice(price);
            coursePub.setPrice_old(old_price);
            returnList.add(coursePub);

        }
        QueryResult<CoursePub> queryResult = new QueryResult();
        queryResult.setTotal(hits.getTotalHits());
        queryResult.setList(returnList);


        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);

    }


    /**
     * 通过给与的名字，获取高亮的信息
     * @param highlightFields
     * @param name
     * @return
     */
    private String getHiglitFieldStr(Map<String, HighlightField> highlightFields , String name ){
        HighlightField highlightField = highlightFields.get(name);
        if(highlightField ==null){
            return null;
        }
        Text[] fragments = highlightField.getFragments();
        StringBuilder stringBuilder = new StringBuilder();

        for (Text fragment : fragments) {
            stringBuilder.append(fragment.string());
        }

        return stringBuilder.toString();

    }



}

