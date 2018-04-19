package com.facetuis.server.service.pinduoduo.commision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommisionContext {

    @Autowired
    private CommisionAddStrategy commisionAddStrategy;

    @Autowired
    private CommisionSubStrategy commisionSubStrategy;

    public CommisionStrategy getStrategy(int status){
        switch (status){
            case 0:
                return commisionAddStrategy;
            case 1:
                return commisionAddStrategy;
            case 2:
                return commisionAddStrategy;
            case 3:
                return commisionAddStrategy;
            case 4:
                return commisionSubStrategy;
            case 5:
                return commisionAddStrategy;
            default:
                return null;
        }
    }
}
