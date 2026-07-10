package com.trier.trier_report.service.impl;

import com.trier.trier_report.client.NewsClient;
import com.trier.trier_report.dto.NewsResponse;
import com.trier.trier_report.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsClient newsClient;

    @Autowired
    public NewsServiceImpl(NewsClient newsClient) {
        this.newsClient = newsClient;
    }

    @Override
    // Cache by (q, from, sortBy)
    @Cacheable(cacheNames = "news", cacheManager = "newsCacheManager", key = "#q + '|' + #from + '|' + #sortBy")
    public NewsResponse getNews(String q, String from, String sortBy) {
        return newsClient.fetchNews(q, from, sortBy);
    }
}
