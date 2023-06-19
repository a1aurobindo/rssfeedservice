package org.rssdemo.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.io.Serializable;

@DynamoDbBean
public class NewsCategoryUserMeta implements Serializable {
    private static final long serialVersionUID = 1L;

    String categoryId;

    String username;

    Integer count;

    @DynamoDbSortKey
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String newsId) {
        this.categoryId = newsId;
    }

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @DynamoDbAttribute("count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
