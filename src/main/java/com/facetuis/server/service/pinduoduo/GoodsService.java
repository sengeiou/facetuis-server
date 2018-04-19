package com.facetuis.server.service.pinduoduo;

import com.alibaba.fastjson.JSONObject;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.pinduoduo.response.GoodsDetailResponse;
import com.facetuis.server.service.pinduoduo.response.GoodsDetails;
import com.facetuis.server.service.pinduoduo.response.GoodsSearchResponse;
import com.facetuis.server.service.pinduoduo.utils.PRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class GoodsService {

    @Autowired
    private PRequestUtils pRequestUtils;

    private String API_GOODS_DETAIL = "pdd.ddk.goods.detail";
    private String API_GOODS_LIST_KWYWORDS = "pdd.ddk.goods.search";


    public GoodsDetails getGoodsById(String id){
        SortedMap<String,String> map = new TreeMap<>();
        map.put("goods_id_list","[" + id + "]");
        BaseResult<String> send = pRequestUtils.send(API_GOODS_DETAIL, map);
        GoodsDetailResponse details = JSONObject.parseObject(send.getResult(),GoodsDetailResponse.class);
        if(details.getGoods_detail_response().getGoods_details().size() > 0){
            return details.getGoods_detail_response().getGoods_details().get(0);
        }
        return null;
    }

    public Page<GoodsDetails> findByWrods(String words,String categoryId,String softType,int page){
        SortedMap<String,String> map = new TreeMap<>();
        map.put("keyword",words);
        map.put("category_id",categoryId);
        map.put("sort_type",softType);
        map.put("page",page + "");
        BaseResult<String> send = pRequestUtils.send(API_GOODS_LIST_KWYWORDS, map);
        GoodsSearchResponse details = JSONObject.parseObject(send.getResult(),GoodsSearchResponse.class);
        return new PageImpl<>(details.getGoods_search_response().getGoods_list());
    }

}
