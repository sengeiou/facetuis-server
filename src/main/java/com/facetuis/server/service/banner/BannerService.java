package com.facetuis.server.service.banner;

import com.facetuis.server.model.banner.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerService {


    public Page<Banner> getBanner(){
        Banner b1 = new Banner();
        b1.setUuid("1");
        b1.setImgUrl("http://");
        b1.setDescription("ddd");
        b1.setName("111");

        Banner b2 = new Banner();
        b2.setUuid("1");
        b2.setImgUrl("http://");
        b2.setDescription("ddd");
        b2.setName("111");

        Banner b3 = new Banner();
        b3.setUuid("1");
        b3.setImgUrl("http://");
        b3.setDescription("ddd");
        b3.setName("111");

        List<Banner> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        list.add(b3);
        return new PageImpl<Banner>(list);
    }

}
