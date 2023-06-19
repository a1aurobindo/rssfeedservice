package com.example.mfrfsdemo.service;

import com.example.mfrfsdemo.component.RssFeedReaderComponent;
import com.rometools.rome.io.FeedException;

import java.io.IOException;

public class RssFeedService {

    RssFeedReaderComponent rssComponent;
    void refreshRssFeed() throws IOException, FeedException {
       rssComponent.refreshNewsFeed();
    }
}
