package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.pinduoduo.GoodsService;
import com.facetuis.server.service.pinduoduo.response.GoodsDetails;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1.0/goods")
public class GoodsController extends FacetuisController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    @RequestMapping("/{goodsId}")
    public BaseResponse get(@PathVariable String goodsId){
        int level = getLevel();
        GoodsDetails details = goodsService.getGoodsById(goodsId,level);
        return  successResult(details);
    }

    @RequestMapping(value = "/keywords/{keywords}/{sortType}")
    public BaseResponse findByWords(@PathVariable String keywords,@PathVariable String sortType,Integer page){
        if(page == null){
            page = 1;
        }
        int level = getLevel();
        Page<GoodsDetails> byWrods = goodsService.findByWrods(keywords, null, sortType,page, level);
        return successResult(byWrods);
    }


    @RequestMapping(value = "/category/{category}/{sortType}")
    public BaseResponse findByCategory(@PathVariable String category,@PathVariable String sortType,Integer page){
        if(page == null){
            page = 1;
        }
        int level = getLevel();
        Page<GoodsDetails> byWrods = goodsService.findByWrods("", category, sortType,page, level);
        return successResult(byWrods);
    }

    private int getLevel(){
        User user = getUser();
        int level = 0;
        if(user != null){
            level = user.getLevel().getLevel();
        }
        return level;
    }

}
