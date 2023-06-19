package com.example.mfrfsdemo.controller;

import com.example.mfrfsdemo.component.RssFeedView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController()
public class RssFeedController {

    @Autowired
    private RssFeedView view;

    @GetMapping("/rss")
    public View getFeed() {
        return view;
    }
}
