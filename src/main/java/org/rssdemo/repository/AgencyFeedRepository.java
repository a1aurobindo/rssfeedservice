package org.rssdemo.repository;

import org.rssdemo.entity.AgencyFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyFeedRepository extends JpaRepository<AgencyFeed, Integer> {
}
