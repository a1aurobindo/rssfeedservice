package com.example.mfrfsdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Entity
@Table(name ="news")
public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    Integer newsId;

    @Column
    String newsTitle;

    @Column
    String newsDescription;

    @Column
    Date newsPublishDateTime;

    @Column
    String newsLink;

    @Column
    Integer clickCount;

    @Column
    Integer categoryId;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public Date getNewsPublishDateTime() {
        return newsPublishDateTime;
    }

    public void setNewsPublishDateTime(Date newsPublishDateTime) {
        this.newsPublishDateTime = newsPublishDateTime;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
