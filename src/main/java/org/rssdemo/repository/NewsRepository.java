package org.rssdemo.repository;


import org.rssdemo.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {

    List<News> findByCategoryIdAndNewsTitle(Integer categoryId, String newsTitle);

    Page<News> findByCategoryIdAndNewsPublishDateTimeBetween(Integer categoryId, Date pubDate, Date endDate, Pageable page);

    Page<News> findByNewsPublishDateTimeBetween(Date startDate, Date endDate, Pageable page);

    Page<News> findByCategoryId(Integer categoryId, Pageable page);

    Page<News> findAll(Pageable page);
}
