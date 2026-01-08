package org.qinyu;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.DateProperty;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qinyu.entity.table.Martyr;
import org.qinyu.service.MartyrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticDocumentTest {

    ElasticsearchClient client;

    @Autowired
    private MartyrService martyrService;
    private MatchAllQuery.Builder m;

    @Test
    void testConnection(){
        System.out.println(client);
    }

    @BeforeEach
    void setUp() {

        // URL和API密钥
        String serverUrl = "http://192.168.153.157:9200";

        // 创建低级别客户端
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Authorization", "ApiKey " + apiKey)
//                })
                .build();

        // 使用Jackson映射器创建传输
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // 然后创建API客户端
        client = new ElasticsearchClient(transport);

    }

    @Test
    void testCreateIndex() throws IOException {
        // 设置索引字段
        Map<String, Property> documentMap = new HashMap<>();
        documentMap.put("id", Property.of(property ->
                        property.text(TextProperty.of(keywordProperty ->
                                keywordProperty.index(true)
                        )
                    )
                )
        );


        documentMap.put("name", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                    )
                )
        );

        documentMap.put("hometown", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );
        documentMap.put("photo", Property.of(property ->
                        property.keyword(KeywordProperty.of(keywordProperty ->
                                keywordProperty.index(false))//这里设置分词
                        )
                )
        );
        documentMap.put("gender", Property.of(property ->
                        property.keyword(KeywordProperty.of(keywordProperty ->
                                keywordProperty.index(true))//这里设置分词
                        )
                )
        );
        documentMap.put("politicsStatus", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );documentMap.put("dept", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );
        documentMap.put("position", Property.of(property ->
                        property.keyword(KeywordProperty.of(keywordProperty ->
                                keywordProperty.index(true))//这里设置分词
                        )
                )
        );
        documentMap.put("achievement", Property.of(property ->
                        property.keyword(KeywordProperty.of(keywordProperty ->
                                keywordProperty.index(true))//这里设置分词
                        )
                )
        );
        documentMap.put("death_date", Property.of(property ->
                        property.date(DateProperty.of(dateProperty ->
                                dateProperty.index(true).format("yyyy-MM-dd HH:mm:ss"))//这里设置分词
                        )
                )
        );
        documentMap.put("deathAddress", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );
        documentMap.put("buryPoint", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );
        documentMap.put("deathCampaign", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );
        documentMap.put("deeds", Property.of(property ->
                        property.text(TextProperty.of(textProperty ->
                                textProperty.index(true).analyzer("ik_smart"))//这里设置分词
                        )
                )
        );

        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest
                .Builder()
                .mappings(mappings -> mappings.properties(documentMap))
                .index("memorial")
                .build();
        ElasticsearchIndicesClient indexClient = client.indices();
        // 发起请求
         indexClient.create(request);
    }

    @Test
    void testBulkMartyrDoc() throws IOException {
        int pageNo = 1;
        int pageSize = 500;
        while (true) {
            // 0. 准备文档数据
            Page<Martyr> page = martyrService.lambdaQuery()
                    .page(Page.of(pageNo, pageSize));
            List<Martyr> records = page.getRecords();
            //1. 创建request对象
            BulkRequest.Builder br = new BulkRequest.Builder();
            //2. 准备参数
            for (Martyr martyr : records) {
                String deeds = martyr.getDeeds();
                String gender;
                if (martyr.getGender()==1){
                    gender = "男";
                }else {
                    gender = "女";
                }
                martyr.setDeeds(martyr.getName()+","+gender+",加入"+martyr.getDept()+
                        ","+martyr.getDeathCampaign()+","+deeds+
                        "安葬于"+martyr.getBuryPoint());
                br.operations(op -> op
                        .index(idx -> idx
                                .index("memorial")
                                .id(String.valueOf(martyr.getMid()))
                                .document(martyr)
                        )
                );
            }
            // 3.发起请求
            client.bulk(br.build());
            // 4. 翻页
            pageNo++;
        }
    }

    @Test
    void testMartyrBool() throws IOException {
        SearchRequest.Builder searchBuilder = new SearchRequest.Builder();
        searchBuilder.index("memorial");


        Query query1 = Query.of(q -> q
                .match(
                        m->m.field("dept")
                                .query(""))
        );
        Query query2 = Query.of(q -> q
                .match(m->m.field("deathCampaign")
                        .query(""))
        );
        Query query3 = Query.of(q -> q
                .term(t-> t.field("position")
                        .value("")
                )
        );
        Query query4 = Query.of(q -> q
                .match(m->m.field("deeds")
                        .query("炮兵 红军 红"))
        );
        BoolQuery boolQuery = BoolQuery.of(b -> b
                .should(query1)
                .should(query2)
                .should(query3)
                .should(query4)
        );
        searchBuilder.query(Query.of(q->q.bool(boolQuery)));
        SearchRequest searchRequest = searchBuilder.build();
        SearchResponse<Martyr> response  = client.search(searchRequest, Martyr.class);
        parseMartyrResponse(response);

//        Query query_ = Query.of(q -> q
//                .bool(b1 -> b1
//                        .should(s -> s
//                                .matchPhrase(m1 -> m1
//                                        .field("productName").query(request.getKey()).boost(3f)))
//                        .should(s1 -> s1
//                                .matchPhrase(m2 -> m2
//                                        .field("shopName").query(request.getKey())))
//                        .should(s2 -> s2
//                                .matchPhrase(m3 -> m3
//                                        .field("brandName").query(request.getKey())))
//                ));
//            //根据条件拼接不同query
//            searchBuilder
//                    .query(query);

}


    private void parseMartyrResponse(SearchResponse<Martyr> response) throws IOException {
        //1. 得到命中对象
        List<Hit<Martyr>> hits = response.hits().hits();
        // 2. 得到总条数
        long total = hits.size();
        System.out.println("total="+total);
        // 3. 获取命中数据
        for (Hit<Martyr> hit : hits) {
            Double score=hit.score();
            Martyr martyr = hit.source();
            System.out.println("score:"+score+"\n"+"martyr:"+martyr);
        }
    }

    @AfterEach
    void tearDown()throws IOException {
        if (client != null){
            client.shutdown();
        }
    }


}
