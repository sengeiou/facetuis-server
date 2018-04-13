package com.facetuis.server.service.headline;

import com.facetuis.server.model.headline.Headline;
import com.facetuis.server.service.headline.vo.HeadlineVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeadlineService {



    public PageImpl<HeadlineVO> list(){



        HeadlineVO headline = new HeadlineVO();
        HeadlineVO.HeadlineContent hc = headline.new HeadlineContent();
        hc.setImg("http://xxxx/img");
        hc.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs = new ArrayList<>();
        hcs.add(hc);
        headline.setContent(hcs);
        headline.setId("1");
        headline.setTitle("title");
        headline.setType("TXT");


        HeadlineVO headline1 = new HeadlineVO();
        HeadlineVO.HeadlineContent hc1 = headline.new HeadlineContent();
        hc1.setImg("http://xxxx/img");
        hc1.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs1 = new ArrayList<>();
        hcs1.add(hc1);
        headline1.setContent(hcs1 );
        headline1.setId("1");
        headline1.setTitle("title");
        headline1.setType("TXT");

        HeadlineVO headline2 = new HeadlineVO();
        HeadlineVO.HeadlineContent hc2 = headline.new HeadlineContent();
        hc2.setImg("http://xxxx/img");
        hc2.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs2 = new ArrayList<>();
        hcs2.add(hc2);
        headline2.setContent(hcs2);
        headline2.setId("1");
        headline2.setTitle("title");
        headline2.setType("TXT");
        List<HeadlineVO> list = new ArrayList<>();
        list.add(headline);
        list.add(headline1);
        list.add(headline2);
        return new PageImpl<HeadlineVO>(list);
    }

    public PageImpl<HeadlineVO> recommandList(){
        HeadlineVO headline = new HeadlineVO();
        HeadlineVO.HeadlineContent hc = headline.new HeadlineContent();
        hc.setImg("http://xxxx/img");
        hc.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs = new ArrayList<>();
        hcs.add(hc);
        headline.setContent(hcs);
        headline.setId("1");
        headline.setTitle("title");
        headline.setType("TXT");


        HeadlineVO headline1 = new HeadlineVO();
        HeadlineVO.HeadlineContent hc1 = headline.new HeadlineContent();
        hc1.setImg("http://xxxx/img");
        hc1.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs1 = new ArrayList<>();
        hcs1.add(hc1);
        headline1.setContent(hcs1 );
        headline1.setId("1");
        headline1.setTitle("title");
        headline1.setType("TXT");

        HeadlineVO headline2 = new HeadlineVO();
        HeadlineVO.HeadlineContent hc2 = headline.new HeadlineContent();
        hc2.setImg("http://xxxx/img");
        hc2.setTxt("描述");
        List< HeadlineVO.HeadlineContent> hcs2 = new ArrayList<>();
        hcs2.add(hc2);
        headline2.setContent(hcs2);
        headline2.setId("1");
        headline2.setTitle("title");
        headline2.setType("TXT");
        List<HeadlineVO> list = new ArrayList<>();
        list.add(headline);
        list.add(headline1);
        list.add(headline2);
        return new PageImpl<HeadlineVO>(list);
    }

}
