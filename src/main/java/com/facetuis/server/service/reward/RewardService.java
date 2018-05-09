package com.facetuis.server.service.reward;

import com.facetuis.server.dao.reward.RewardRepository;
import com.facetuis.server.model.reward.Reward;
import com.facetuis.server.service.basic.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardService extends BasicService {


    @Autowired
    private RewardRepository rewardRepository;

    public void create(Reward reward){
        rewardRepository.save(reward);
    }


}
