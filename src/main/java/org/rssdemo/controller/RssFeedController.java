package org.rssdemo.controller;

import org.rssdemo.domain.NewsViewDTO;
import org.rssdemo.service.RssFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController(value = "/rss")
public class RssFeedController {

    String hostId;

    @Autowired
    private RssFeedService rssFeedService;

    @GetMapping(value = "/refresh", produces = "text/plain")
    public String refreshFeed() {
        this.rssFeedService.refreshRssFeed();
        return "refreshed!";
    }

    @GetMapping("/getfeed")
    public Page<NewsViewDTO> getFeed(@RequestParam(value = "category", required = false) String category,
                                     @RequestParam(value = "pubdate", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date pubDate,
                                     Pageable pageable) {

        return this.rssFeedService.fetchRssFeed(category, pubDate, pageable);
    }

    @PostMapping("/savefeed")
    public boolean saveFeed(@RequestBody String feedUrl) {

        return this.rssFeedService.saveFeed(feedUrl);
    }

    @PutMapping("/count")
    public void clicked(@RequestParam(value = "newsid", required = false) String newsId, @RequestParam("username") String username, @RequestParam("categoryid") Integer categoryId) {
        this.rssFeedService.countFeed(username, categoryId, newsId);
    }

}
