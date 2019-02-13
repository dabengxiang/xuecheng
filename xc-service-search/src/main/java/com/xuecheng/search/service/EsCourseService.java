package com.xuecheng.search.service;

import com.xuecheng.framework.domain.search.CourseSearchParam;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public void list(int page, int size, CourseSearchParam courseSearchParam) {

        SearchRequest searchRequest = new SearchRequest("indexName");
        searchRequest.types(typeName);
        SearchSourceBuilder sourceBuilder = searchRequest.source();

        if(page<1){
            page = 1;
        }

        sourceBuilder.from((page-1) * size);
        sourceBuilder.size(size);

        String keyword = courseSearchParam.getKeyword();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();


        if(StringUtils.isNotBlank(keyword)){
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "name", "description").minimumShouldMatch("70%");
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }else{
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(matchAllQueryBuilder);
        }

        if(StringUtils.isNotBlank(courseSearchParam.getMt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));

        }

        if(StringUtils.isNotBlank(courseSearchParam.getSt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));

        }




//        restHighLevelClient.search()


    }
}

