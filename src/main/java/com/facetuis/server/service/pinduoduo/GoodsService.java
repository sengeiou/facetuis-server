package com.facetuis.server.service.pinduoduo;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.GoodsDetail;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.pinduoduo.response.GoodsDetailResponse;
import com.facetuis.server.service.pinduoduo.response.GoodsDetails;
import com.facetuis.server.service.pinduoduo.response.GoodsSearchResponse;
import com.facetuis.server.service.pinduoduo.utils.PRequestUtils;
import com.facetuis.server.utils.CommisionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class GoodsService {

    @Autowired
    private PRequestUtils pRequestUtils;

    private String API_GOODS_DETAIL = "pdd.ddk.goods.detail";
    private String API_GOODS_LIST_KWYWORDS = "pdd.ddk.goods.search";


    public GoodsDetails getGoodsById(String id,int level){
        SortedMap<String,String> map = new TreeMap<>();
        map.put("goods_id_list","[" + id + "]");
        BaseResult<String> send = pRequestUtils.send(API_GOODS_DETAIL, map);
        GoodsDetailResponse details = JSONObject.parseObject(send.getResult(),GoodsDetailResponse.class);
        if(details.getGoods_detail_response().getGoods_details().size() > 0){
            GoodsDetails goodsDetails = details.getGoods_detail_response().getGoods_details().get(0);
            if(goodsDetails == null){
                return goodsDetails;
            }
            goodsDetails.setAboutEarn(CommisionUtils.getEarn(level,goodsDetails.getMin_group_price()));
            return goodsDetails;
        }
        return null;
    }

    public Page<GoodsDetails> findByWrods(String words,String categoryId,String softType,int page,int level){
        SortedMap<String,String> map = new TreeMap<>();
        map.put("keyword",words == "0" ? "" : words);
        map.put("category_id",categoryId == "0" ? "" : categoryId);
        map.put("sort_type",softType);
        map.put("page",page + "");
        BaseResult<String> send = pRequestUtils.send(API_GOODS_LIST_KWYWORDS, map);
        GoodsSearchResponse details = JSONObject.parseObject(send.getResult(),GoodsSearchResponse.class);
        if(details != null && details.getTotal_count() > 0){
            List<GoodsDetails> goods_list = details.getGoods_search_response().getGoods_list();
            for(GoodsDetails goodsDetail : goods_list){
                goodsDetail.setAboutEarn(CommisionUtils.getEarn(level,goodsDetail.getMin_group_price()));
            }
            return new PageImpl<>(details.getGoods_search_response().getGoods_list());
        }else{
            return new PageImpl<>(Collections.EMPTY_LIST);
        }
    }

}
