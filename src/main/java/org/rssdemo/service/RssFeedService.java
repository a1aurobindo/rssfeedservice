package org.rssdemo.service;

import org.rssdemo.component.RssFeedComponent;
import com.rometools.rome.io.FeedException;
import org.rssdemo.domain.NewsViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class RssFeedService {

    private RssFeedComponent rssComponent;

    public RssFeedService(RssFeedComponent rssComponent) {
        this.rssComponent = rssComponent;
    }

    public void refreshRssFeed() {
        try {
            this.rssComponent.refreshNewsFeed();
        } catch(IOException e) {

        } catch(FeedException e) {

        }
    }

    public Page<NewsViewDTO> fetchRssFeed(String category, Date pubDate, Pageable page) {

        try {
            return this.rssComponent.fetchNews(category, pubDate, page);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean saveFeed(String feedUrl) {


        return true;
    }

    public void countFeed(String username, Integer categoryId, String newsId) {

        this.rssComponent.saveItem(username, categoryId);
//        this.
        this.rssComponent.countPage(newsId);
    }
}
