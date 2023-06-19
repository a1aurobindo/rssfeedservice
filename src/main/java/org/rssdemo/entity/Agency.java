package org.rssdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "agency")
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="agency_id")
    Integer agencyId;

    @Column(name = "agency_name")
    String agencyName;

    @Column
    String agencyLogoPatch;

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyLogoPatch() {
        return agencyLogoPatch;
    }

    public void setAgencyLogoPatch(String agencyLogoPatch) {
        this.agencyLogoPatch = agencyLogoPatch;
    }
}
