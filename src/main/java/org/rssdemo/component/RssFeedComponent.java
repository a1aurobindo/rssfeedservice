package org.rssdemo.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import org.apache.commons.lang3.StringUtils;
import org.rssdemo.domain.NewsViewDTO;
import org.rssdemo.entity.AgencyFeed;
import org.rssdemo.entity.Category;
import org.rssdemo.entity.News;
import org.rssdemo.entity.NewsCategoryUserMeta;
import org.rssdemo.repository.AgencyFeedRepository;
import org.rssdemo.repository.CategoryRepository;
import org.rssdemo.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Component
public class RssFeedComponent {

    @Value("${aws.dynamodb.table.rss_meta}")
    String tableName;
    @Value("${aws.dynamodb.table.rss_meta.partkey}")
    String partitionKeyName;
    @Value("${aws.dynamodb.table.rss_meta.sortkey}")
    String sortKeyName;

    WireFeed feed = null;

    AgencyFeedRepository agencyFeedRepository;
    NewsRepository newsRepository;

    CategoryRepository categoryRepository;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public RssFeedComponent(NewsRepository newsRepository,
                            AgencyFeedRepository agencyFeedRepository,
                            CategoryRepository categoryRepository) {
        this.newsRepository = newsRepository;
        this.agencyFeedRepository = agencyFeedRepository;
        this.categoryRepository = categoryRepository;
    }

    public WireFeed getSyndFeedForUrl(String url) throws Exception {
        try(InputStream is = new URL(url).openConnection().getInputStream()) {
            WireFeedInput input = new WireFeedInput();
            feed = input.build(new InputSource(is));
            return feed;
        }  catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public void refreshNewsFeed() throws IOException, FeedException {
        List<AgencyFeed> agencyFeeds = agencyFeedRepository.findAll();
        List<News> newsList = new ArrayList<>();

        Channel channel = null;
        for(AgencyFeed agencyFeed : agencyFeeds) {

            try {
                channel = (Channel) this.getSyndFeedForUrl(agencyFeed.getAgencyFeedUrl());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if(CollectionUtils.isEmpty(channel.getItems())) {
               continue;
            }
            channel.getItems().stream().forEach(item -> {

                System.err.println(item.getTitle());
                if(!CollectionUtils.isEmpty(newsRepository.findByCategoryIdAndNewsTitle(agencyFeed.getCategoryId(), item.getTitle()))) {
                    System.out.println(item.getTitle());
                    return;
                }
                News news = new News();
                news.setCategoryId(agencyFeed.getCategoryId());
                news.setNewsTitle(item.getTitle());
                news.setNewsLink(item.getLink());
                news.setNewsDescription(StringUtils.substring(item.getDescription().getValue(), 0, 199));
                news.setNewsPublishDateTime(item.getPubDate());
                newsList.add(news);
            });
        }
        newsRepository.saveAll(newsList);
    }

    @Deprecated
    public PageImpl<NewsViewDTO> fetchLatestNews(Pageable page) {
        Pageable pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort());
        PageImpl<News> rssFeedPage = (PageImpl<News>) newsRepository.findAll(pageRequest);
        return mapper.convertValue(rssFeedPage, new TypeReference<RssFeedResponsePage<NewsViewDTO>>() {});
    }

    public PageImpl<NewsViewDTO> fetchNews(String category, Date pubDate, Pageable page) {

        Pageable pageRequest = PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSort());
        PageImpl<News> rssFeedPage;
        if(org.springframework.util.StringUtils.hasText(category)) {
            Category category1  = categoryRepository.findByCategoryTitle(category);
            System.out.println(category1);
            System.out.println(category1);
            if (Objects.isNull(pubDate)) {
                rssFeedPage = (PageImpl<News>) newsRepository.findByCategoryId(category1.getCategoryId(), pageRequest);
            } else {
                rssFeedPage = (PageImpl<News>) newsRepository.findByCategoryIdAndNewsPublishDateTimeBetween(category1.getCategoryId(), pubDate, new Date(pubDate.getTime() + (1000 * 60 * 60 * 24)), pageRequest);
            }
        } else if(Objects.nonNull(pubDate)) {
            ;
            rssFeedPage = (PageImpl<News>) newsRepository.findByNewsPublishDateTimeBetween(pubDate, new Date(pubDate.getTime() + (1000 * 60 * 60 * 24)), pageRequest);
        } else {
            rssFeedPage = (PageImpl<News>) newsRepository.findAll(pageRequest);
        }

        return mapper.convertValue(rssFeedPage, new TypeReference<RssFeedResponsePage<NewsViewDTO>>() {});
    }

    public void countPage(String newsId) {

        Optional<News> news = newsRepository.findById(newsId);
        news.ifPresent(n -> n.setClickCount(n.getClickCount() + 1));
        newsRepository.save(news.get());
    }

    public boolean saveItem(String username, Integer categoryId) {

        try {
            Category category = this.categoryRepository.findById(categoryId).get();
            DynamoDbTable<NewsCategoryUserMeta> mappedTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(NewsCategoryUserMeta.class));

            // Populate the Table.
            NewsCategoryUserMeta custRecord = new NewsCategoryUserMeta();
            custRecord.setCategoryId(category.getCategoryTitle());
            custRecord.setUsername(username);

            NewsCategoryUserMeta newsCategoryUserMeta = getItem(username, categoryId);
            if(Objects.nonNull(newsCategoryUserMeta)) {
                updateItem(username, categoryId, newsCategoryUserMeta.getCount());
            } else {
                // Put the customer data into an Amazon DynamoDB table.
                mappedTable.putItem(custRecord);
            }

            return true;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table with id id101");
        return false;
    }

    private NewsCategoryUserMeta updateItem(String username, Integer category, Integer count) {

        try {

            DynamoDbTable<NewsCategoryUserMeta> mappedTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(NewsCategoryUserMeta.class));
            Key key = Key.builder()
                    .partitionValue(username)
                    .build();

            // Get the item by using the key and update the email value.
            NewsCategoryUserMeta newsCategoryUserMeta = mappedTable.getItem(r->r.key(key));
            newsCategoryUserMeta.setCategoryId(category.toString());
            newsCategoryUserMeta.setCount(count);

            return newsCategoryUserMeta;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public NewsCategoryUserMeta getItem(String username, Integer categoryId) {

        NewsCategoryUserMeta result = null;

        try {
            DynamoDbTable<NewsCategoryUserMeta> mappedTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(NewsCategoryUserMeta.class));
            Key key = Key.builder()
                    .partitionValue(username).sortValue(categoryId)
                    .build();

            // Get the item by using the key.
            result = mappedTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
            System.out.println("******* The description value is " + result.getUsername());

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return result;
    }
}
