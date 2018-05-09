package com.facetuis.server.dao.reward;

import com.facetuis.server.model.reward.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward,String> {
}
