package com.elens.data.rbces.conf;

import com.elens.data.rbces.vo.EsPage;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @BelongsProject: elensdata-oauth
 * @BelongsPackage: com.elens.data.rbces.conf
 * @Author: xuweichao
 * @CreateTime: 2018-12-29 14:07
 * @Description: es 工具类
 */
@Log
@Component
public class ElasticsearchUtil {


    @Autowired
    private TransportClient transportClient;

    private static TransportClient client;


    /**
     * @PostContruct 是spring框架的注解
     * spring容器初始化的时候执行该方法
     */
    @PostConstruct
    public void init() {
        client = this.transportClient;
    }


    /**
     * 数据添加
     *
     * @param data  要增加的数据
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     * @return
     */
    public static String addData(Map<String, Object> data, String index, String type, String id) {

        IndexResponse response = client.prepareIndex(index, type, id).setSource(data).get();

        log.info("addData response status:{},id:{}" + response.status().getStatus() + "  " + response.getId());
        return response.getId();
    }

    /**
     * 数据添加 自动生成id
     *
     * @param data  要增加的数据
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @return
     */
    public static String addData(Map<String, Object> data, String index, String type) {
        return addData(data, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public static void deleteDataById(String index, String type, String id) {

        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

        log.info("deleteDataById response status:{},id:{}" + response.status().getStatus() + "  " + response.getId());
    }

    /**
     * 通过ID 更新数据
     *
     * @param map   要增加的数据
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     * @return
     */
    public static UpdateResponse updateDataById(Map<String, Object> map, String index, String type, String id) {

        UpdateRequest updateRequest = new UpdateRequest();

        updateRequest.index(index).type(type).id(id).doc(map);

        ActionFuture<UpdateResponse> update = client.update(updateRequest);
        return update.actionGet();
    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {

        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);

        if (StringUtils.isNotEmpty(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }
        GetResponse getResponse = getRequestBuilder.execute().actionGet();

        return getResponse.getSource();
    }


    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public static EsPage searchDataPage(String index, String type,
                                        int startPage, int pageSize,
                                        QueryBuilder query, String fields,
                                        String sortField, String highlightField, SortOrder sortOrder) {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            if (sortOrder != null) {
                searchRequestBuilder.addSort(sortField, sortOrder);
            } else {
                searchRequestBuilder.addSort(sortField, SortOrder.DESC);

            }
        }

        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(query);

        // 分页应用
        searchRequestBuilder.setFrom((startPage - 1) * pageSize).setSize(pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(false);

        //打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
        log.info("\n{}" + searchRequestBuilder);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        log.info("共查询到[{}]条数据,处理数据条数[{}]" + totalHits + "  " + length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);

            if (sourceList.size() == 0) {
                return null;
            } else {

                return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
            }
        }

        return null;

    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param query          查询条件
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, QueryBuilder query,
                                                           Integer size, String fields, String sortField,
                                                           String highlightField) {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }

        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(query);

        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        searchRequestBuilder.setFetchSource(true);

        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        if (size != null && size > 0) {
            searchRequestBuilder.setSize(size);
        }

        //打印的内容 可以在 Elasticsearch head 和 Kibana 上执行查询
        log.info("\n" + searchRequestBuilder);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

//        log.info("共查询到[{" + totalHits + "}]条数据,处理数据条数[{" + length + "}]");

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }

        return null;

    }


    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());

            if (StringUtils.isNotEmpty(highlightField)) {

//                log.info("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }

        return sourceList;
    }


    /**
     * 多种聚合类型 查询
     *
     * @param index
     * @param type
     * @param query
     * @param size
     * @param aggregationBuilders
     * @return
     */
    public static Map<String, Object> getAggregations(String index, String type,
                                                      QueryBuilder query, int size,
                                                      List<AggregationBuilder> aggregationBuilders) {

        SearchRequestBuilder searchBuilder = client
                .prepareSearch(index.split(","))
                .setQuery(query)
                .setSize(size);

        if (StringUtils.isNotEmpty(type)) {
            searchBuilder.setTypes(type.split(","));
        }

        List<String> keyList = new ArrayList<>();
        for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
            keyList.add(aggregationBuilder.getName());
            searchBuilder = searchBuilder.addAggregation(aggregationBuilder);
        }
        log.info("query --->>> \n" + searchBuilder);

        SearchResponse search = searchBuilder.get();
        log.info("result --->>> \n" + search);

        Map<String, Object> result = new HashMap<>();
        for (String str : keyList) {
            Aggregation agg = search.getAggregations().get(str);
            if (agg == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            if (agg instanceof Terms) {
                List<Bucket> buckets = (List<Bucket>) ((Terms) agg).getBuckets();

                for (Bucket bt : buckets) {
                    map.put(bt.getKey().toString(), bt.getDocCount());
                }
                result.put(str, map);
            } else if (agg instanceof Histogram) {
                List<InternalDateHistogram.Bucket> buckets = ((InternalDateHistogram) agg).getBuckets();

                for (InternalDateHistogram.Bucket bt : buckets) {
                    map.put(bt.getKeyAsString(), bt.getDocCount());
                }
                result.put(str, map);
            }

        }
        return result;
    }




    /**
     * 输出大结果集
     *
     * @param index
     * @param type
     * @param fields
     * @param sortedField
     * @param size
     * @param queryBuilder
     * @return
     */
    public static List<Map<String, Object>> scollSearchListData(String index, String type,
                                                                String fields, String sortedField,
                                                                int size,
                                                                QueryBuilder queryBuilder) {
        SearchRequestBuilder searchRequestBuilder = client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
                .setSize(size)
                .setScroll(new TimeValue(2000));
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        //设置排序的字段
        if (StringUtils.isNotEmpty(sortedField)) {
            searchRequestBuilder.addSort(SortBuilders.fieldSort(sortedField));
        }
        log.info("=={}\n" + searchRequestBuilder.toString());
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        //获取总数量
        long totalCount = response.getHits().getTotalHits();
        //计算总页数,每次搜索数量为分片数*设置的size大小
        int page = totalCount % size == 0 ? (int) totalCount / size : (int) totalCount / size + 1;
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < page; i++) {
            //再次发送请求,并使用上次搜索结果的ScrollId
            response = client.prepareSearchScroll(response.getScrollId())
                    .setScroll(new TimeValue(20000)).execute()
                    .actionGet();

            for (SearchHit searchHit : response.getHits().getHits()) {
                Map<String, Object> map = searchHit.getSourceAsMap();
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取符合查询条件的文档数量
     *
     * @param index
     * @param type
     * @param query
     * @return
     */
    public static Long getDataCount(String index, String type, QueryBuilder query) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index.split(","));
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setQuery(query);
        searchRequestBuilder.setFetchSource(true);
        searchRequestBuilder.setSize(0);
        log.info("\n" + searchRequestBuilder);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        log.info("result --->>> \n" + searchResponse);

        long totalHits = searchResponse.getHits().totalHits;
        log.info("--totalHits--" + totalHits);

        return totalHits;
    }

    /**
     * 预处理数据 防止空指针
     *
     * @param data
     * @param fields
     */
    public static void initData(Map<String, Object> data, String fields) {

        String[] keys = fields.split(",");
        initData(data, keys);
    }

    /**
     * 预处理数据 防止空指针
     *
     * @param data
     * @param keys 字段数组
     */
    public static void initData(Map<String, Object> data, String[] keys) {
        for (int i = 0, size = keys.length; i < size; i++) {
            Object str = null;
            try {
                str = data.get(keys[i]);
            } catch (Exception e) {
            }
            data.put(keys[i], str == null ? "" : str);
        }
    }
}
