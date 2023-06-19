package org.rssdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "agency_feed")
public class AgencyFeed implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "agency_feed_id")
    Integer agencyFeedId;

    @Column(name = "agency_feed_url")
    String agencyFeedUrl;

    @Column(name = "agency_id")
    Integer agencyId;

    @Column(name = "category_id")
    Integer categoryId;

    public Integer getAgencyFeedId() {
        return agencyFeedId;
    }

    public void setAgencyFeedId(Integer agencyFeedId) {
        this.agencyFeedId = agencyFeedId;
    }

    public String getAgencyFeedUrl() {
        return agencyFeedUrl;
    }

    public void setAgencyFeedUrl(String agencyFeedUrl) {
        this.agencyFeedUrl = agencyFeedUrl;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
