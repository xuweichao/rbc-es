package com.elens.data.rbces.controller;

import com.alibaba.fastjson.JSONObject;
import com.elens.data.rbces.conf.ElasticsearchUtil;
import com.elens.data.rbces.vo.EsPage;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Log
@Api("检索相关接口")
@RestController
@RequestMapping("query")
public class EsController {

    @Autowired
    ElasticsearchUtil esUtil;

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Value("${elasticsearch.type.name}")
    private String esType;


    @ApiOperation(value = "index 是否存在", notes = "index 是否存在")
    @GetMapping("exist/{indexName}")
    public String isExist(@PathVariable String indexName) {
        if (!ElasticsearchUtil.isIndexExist(indexName)) {
            ElasticsearchUtil.createIndex(indexName);
        } else {
            return "索引已经存在";
        }
        return "索引创建成功";
    }


    @ApiOperation(value = "创建索引", notes = "创建索引")
    @GetMapping("/createIndex/{indexName}")
    public String createIndex(@PathVariable String indexName) {
        if (!ElasticsearchUtil.isIndexExist(indexName)) {
            ElasticsearchUtil.createIndex(indexName);
        } else {
            return "索引已经存在";
        }
        return "索引创建成功";
    }

    @ApiOperation(value = " 插入记录", notes = " 插入记录")
    @GetMapping("/insertJson")
    public String insertJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "wwwwwww");
        jsonObject.put("age", 25);
        jsonObject.put("name", "j-" + new Random(100).nextInt());
        jsonObject.put("date", "www");
        String id = ElasticsearchUtil.addData(jsonObject,
                indexName,
                esType,
                jsonObject.getString("id"));
        return id;
    }

//    /**
//     * 插入记录
//     * @return
//     */
//    @RequestMapping("/insertModel")
//    
//    public String insertModel() {
//        EsModel esModel = new EsModel();
//        esModel.setId(DateUtil.formatDate(new Date()));
//        esModel.setName("m-" + new Random(100).nextInt());
//        esModel.setAge(30);
//        esModel.setDate(new Date());
//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(esModel);
//        String id = ElasticsearchUtil.addData(jsonObject, indexName, esType, jsonObject.getString("id"));
//        return id;
//    }

    @ApiOperation(value = " 删除记录", notes = "删除记录")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        if (StringUtil.isNotEmpty(id)) {
            ElasticsearchUtil.deleteDataById(indexName, esType, id);
            return "删除id=" + id;
        } else {
            return "id为空";
        }
    }


    @ApiOperation(value = "更新数据", notes = "更新数据")
    @GetMapping("/update/{id}")
    public String update(@PathVariable String id) {
        if (StringUtil.isNotEmpty(id)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("age", 31);
            jsonObject.put("name", "修改");
            jsonObject.put("date", new Date());
            ElasticsearchUtil.updateDataById(jsonObject,
                    indexName,
                    esType,
                    id);
            return "id=" + id;
        } else {
            return "id为空";
        }
    }


    @ApiOperation(value = "获取数据", notes = "根据id获取数据")
    @GetMapping("/getData/{id}")
    public String getData(@PathVariable String id) {
        if (StringUtil.isNotEmpty(id)) {
            Map<String, Object> map = ElasticsearchUtil.searchDataById(indexName,
                    esType,
                    id,
                    null);
            return JSONObject.toJSONString(map);
        } else {
            return "id为空";
        }
    }

    /**
     * 查询数据
     * 模糊查询
     *
     * @return
     */
    @GetMapping("/queryMatchData")
    public String queryMatchData() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolean matchPhrase = false;
        if (matchPhrase == Boolean.TRUE) {
            boolQuery.must(QueryBuilders.matchPhraseQuery("name", "修"));
        } else {
            boolQuery.must(QueryBuilders.matchQuery("name", "修"));
        }
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(
                indexName,
                esType,
                boolQuery,
                10,
                null,
                null,
                null);
        return JSONObject.toJSONString(list);
    }

    /**
     * 通配符查询数据
     * 通配符查询 ?用来匹配1个任意字符，*用来匹配零个或者多个字符
     *
     * @return
     */
    @GetMapping("/queryWildcardData")
    public String queryWildcardData() {
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name.keyword", "j-*466");
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(
                indexName,
                esType,
                queryBuilder,
                10,
                null,
                null,
                null);
        return JSONObject.toJSONString(list);
    }

    /**
     * 正则查询
     *
     * @return
     */
    @GetMapping("/queryRegexpData")
    public String queryRegexpData() {
        QueryBuilder queryBuilder = QueryBuilders.regexpQuery("name.keyword", "j--[0-9]{1,11}");
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(indexName,
                esType, queryBuilder,
                10,
                null,
                null,
                null);
        return JSONObject.toJSONString(list);
    }

    /**
     * 查询数字范围数据
     *
     * @return
     */
    @GetMapping("/queryIntRangeData")
    public String queryIntRangeData() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(
                QueryBuilders.rangeQuery("age")
                        .from(21)
                        .to(25));
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(
                indexName,
                esType,
                boolQuery,
                10,
                null,
                null,
                null);
        return JSONObject.toJSONString(list);
    }

    /**
     * 查询日期范围数据
     *
     * @return
     */
    @GetMapping("/queryDateRangeData")
    public String queryDateRangeData() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery("date")
                .from("2018-04-25T08:33:44.840Z")
                .to("2018-04-25T10:03:08.081Z"));
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(
                indexName,
                esType,
                boolQuery,
                10,
                null,
                null,
                null);
        return JSONObject.toJSONString(list);
    }

    /**
     * 查询分页
     *
     * @param startPage 第几条记录开始
     *                  从0开始
     *                  第1页 ：http://127.0.0.1:8080/es/queryPage?startPage=0&pageSize=2
     *                  第2页 ：http://127.0.0.1:8080/es/queryPage?startPage=2&pageSize=2
     * @param pageSize  每页大小
     * @return
     */
    @GetMapping("/queryPage")
    public String queryPage(String startPage, String pageSize) {
        if (StringUtil.isNotEmpty(startPage) && StringUtil.isNotEmpty(pageSize)) {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(
                    QueryBuilders.rangeQuery("date")
                            .from("2018-04-25T08:33:44.840Z")
                            .to("2018-04-25T10:03:08.081Z"));
            EsPage list = ElasticsearchUtil.searchDataPage(
                    indexName,
                    esType,
                    Integer.parseInt(startPage),
                    Integer.parseInt(pageSize),
                    boolQuery,
                    null,
                    null,
                    null);
            return JSONObject.toJSONString(list);
        } else {
            return "startPage或者pageSize缺失";
        }
    }

    @ApiOperation(value = "es 多字段聚合测试", notes = "聚合测试")
    @GetMapping("/aggs")
    public @ResponseBody
    Map<String, Object> aggsTest() {
        log.info("聚合测试===>>");
        JSONObject aggs = new JSONObject();
        aggs.put("性别", "sex_name.keyword");
        aggs.put("年龄段", "age_range_name.raw");
        aggs.put("月收入", "income_name.raw");
        aggs.put("婚姻状况", "emotional_name.raw");
        aggs.put("子女情况", "children_flag_name.raw");
        aggs.put("教育水平", "education_name.raw");
        aggs.put("职业", "profession_name.raw");

        List<AggregationBuilder> list = new ArrayList();
        for (Map.Entry<String, Object> agg : aggs.entrySet()) {
            String key = agg.getKey();
            log.info(key + "---" + agg.getValue().toString());
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders
                    .terms(key)
                    .field(agg.getValue().toString());
            list.add(aggregationBuilder);
        }

        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        Map<String, Object> map = ElasticsearchUtil.getAggregations(
                indexName, esType,
                matchAllQueryBuilder,
                0,
                list);
        return map;
    }


}
