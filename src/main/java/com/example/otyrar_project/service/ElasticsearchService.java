package com.example.otyrar_project.service;

import com.example.otyrar_project.controller.ControllerMain;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ElasticsearchService {
    Logger logger= LoggerFactory.getLogger(ElasticsearchService.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void processElasticsearchData() throws IOException {
        logger.info("starting calculating avg traffic rate");
        SearchRequest searchRequest = new SearchRequest("test1");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

// Aggregation to get hits per month
        searchSourceBuilder.aggregation(
                AggregationBuilders
                        .dateHistogram("monthly_traffic")
                        .field("timestamp")
                        .calendarInterval(DateHistogramInterval.MONTH)
                        .subAggregation(AggregationBuilders.sum("total_hits").field("timestamp"))
        );

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

// Extract data from the response, e.g., total hits
        long totalHits = hits.getTotalHits().value;
        logger.info("total hist is " + totalHits);

// Extract aggregations, if any
        logger.info(searchRequest.toString());
        ParsedDateHistogram dateHistogram = searchResponse.getAggregations().get("monthly_traffic");
        List<? extends Histogram.Bucket> buckets = dateHistogram.getBuckets();
        logger.info("buckets is :" + dateHistogram.toString());
        logger.info("The size of buckets " + String.valueOf(buckets.size()));

// Process the buckets to get data for each month
        for (Histogram.Bucket bucket : buckets) {
            logger.info("foreach is working");
            DocValueFormat.DateTime dateTime = (DocValueFormat.DateTime) bucket.getKey();
            String monthlyTotalHits = ((Sum) bucket.getAggregations().get("total_hits")).getValueAsString();
            logger.info("date time: " + dateTime + " monthlyTotalHist " + monthlyTotalHits);
            // Process or store the data as needed
        }
    }
}