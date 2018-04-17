package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*新手必看帮助页面
* */
@RestController
@RequestMapping("/1.0/helppage")
public class HelpPageController extends FacetuisController {

    public class HelpPage{
        private String title;
        private String content;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse list(){
        HelpPage hp1 = new HelpPage();
        hp1.setTitle("自己够买的，为什么在拼多多上面看不到订单？");
        hp1.setContent("亲爱的用户，您好 您这个问题是因为最近.......");
        HelpPage hp2 = new HelpPage();
        hp2.setTitle("自己够买的，为什么在拼多多上面看不到订单？");
        hp2.setContent("亲爱的用户，您好 您这个问题是因为最近.......");
        HelpPage hp3 = new HelpPage();
        hp3.setTitle("自己够买的，为什么在拼多多上面看不到订单？");
        hp3.setContent("亲爱的用户，您好 您这个问题是因为最近.......");
        HelpPage hp4 = new HelpPage();
        hp4.setTitle("自己够买的，为什么在拼多多上面看不到订单？");
        hp4.setContent("亲爱的用户，您好 您这个问题是因为最近.......");
        List<HelpPage> list = new ArrayList<>();
        list.add(hp1);
        list.add(hp2);
        list.add(hp3);
        list.add(hp4);

        Page<HelpPage> page = new PageImpl<>(list);
        return successResult(page);
    }

}
