package org.qinyu.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.qinyu.dto.MartyrDTO;
import org.qinyu.entity.Martyr;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleSmartMartyrVO;
import org.qinyu.mapper.MartyrMapper;
import org.qinyu.service.MartyrService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MartyrServiceImpl extends ServiceImpl<MartyrMapper, Martyr> implements MartyrService {
    @Override
    public PageVO<SimpleSmartMartyrVO> smartSearch(MartyrDTO martyrDTO, Integer page, Integer pageSize) throws IOException {
        //TODO 封装为config类
        //1.连接Elasticsearch 搜索引擎
        ElasticsearchClient client;
        // URL和API密钥
        String serverUrl = "http://192.168.153.160:9200";

        // 创建低级别客户端
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .build();

        // 使用Jackson映射器创建传输
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // 然后创建API客户端
        client = new ElasticsearchClient(transport);
        //2.测试连接
        System.out.println("client:" + client);

        SearchRequest.Builder searchBuilder = new SearchRequest.Builder();
        searchBuilder.index("memorial");
        if ((martyrDTO.getPosition() == null && martyrDTO.getDept() == null
                && martyrDTO.getDeathCampaign() == null && martyrDTO.getDeeds() == null) || (martyrDTO.getPosition() == "" && martyrDTO.getDept() == ""
                && martyrDTO.getDeathCampaign() == "" && martyrDTO.getDeeds() == "")) {
            searchBuilder.query(Query.of(q->q.matchAll(t->t)));
        }else{
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
            if (martyrDTO.getDeeds() != null && martyrDTO.getDeeds() != "") {
                Query searchQuery = Query.of(q -> q
                        .match(m -> m.field("deeds")
                                .query(martyrDTO.getDeeds()))
                );
                boolQueryBuilder.should(searchQuery);
            }

            if (martyrDTO.getDept() != null && martyrDTO.getDept() != "") {
                Query query = Query.of(q -> q
                        .match(
                                m -> m.field("dept")
                                        .query(martyrDTO.getDept()))
                );
                boolQueryBuilder.should(query);
            }

            if (martyrDTO.getDeathCampaign() != null && martyrDTO.getDeathCampaign() != "") {
                Query query = Query.of(q -> q
                        .match(m -> m.field("deathCampaign")
                                .query(martyrDTO.getDeathCampaign()))
                );
                boolQueryBuilder.should(query);
            }

            if (martyrDTO.getPosition() != null && martyrDTO.getPosition() != "") {
                Query query = Query.of(q -> q
                        .term(t -> t.field("position")
                                .value(martyrDTO.getPosition())
                        )
                );
                boolQueryBuilder.should(query);
            }

            searchBuilder.query(Query.of(q -> q.bool(boolQueryBuilder.build())));
//        searchBuilder.from((page-1)*pageSize);
        }
        searchBuilder.size(2312);
        SearchRequest searchRequest = searchBuilder.build();
        SearchResponse<Martyr> response = client.search(searchRequest, Martyr.class);

        return parseMartyrResponse(response, page, pageSize);
    }

    //TODO 返回评判百分数
    private PageVO<SimpleSmartMartyrVO> parseMartyrResponse(SearchResponse<Martyr> response, Integer page, Integer pageSize) throws IOException {
        //1. 得到命中对象
        Double maxScore=response.hits().maxScore();
        System.out.println("maxScore:" + maxScore);
        List<Hit<Martyr>> hits = response.hits().hits();
        // 2. 得到总条数
        long total = hits.size();
        System.out.println("total=" + total);
        // 3. 获取命中数据
        List<SimpleSmartMartyrVO> martyrs = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            if ((page - 1) * pageSize + i >= hits.size()) {
                break;
            }
            Hit<Martyr> hit = hits.get((page - 1) * pageSize + i);
            Martyr martyr = hit.source();

            Double score = hit.score();
            score=score/maxScore;
            SimpleSmartMartyrVO martyrVO = null;
            if (martyr != null) {
                martyrVO = new SimpleSmartMartyrVO(martyr);
                martyrVO.setScore(score);
            }
            martyrs.add(martyrVO);
        }
        return new PageVO<>(total, martyrs);
    }
}
