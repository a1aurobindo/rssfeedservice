package com.example.mfrfsdemo.repository;

import com.example.mfrfsdemo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    List<News> findByCategoryIdAndNewsTitle(Integer categoryId, String newsTitle);
}
