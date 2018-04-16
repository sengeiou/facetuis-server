package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.response.TeamPopleCountResponse;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserRelation;
import com.facetuis.server.service.user.UserRelationService;
import com.facetuis.server.utils.NeedLogin;
import com.facetuis.server.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/1.0/team")
public class TeamController extends FacetuisController {

    @Autowired
    private UserRelationService userRelationService;

    @RequestMapping(value = "/people/count",method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse teamPeopleCount(){
        User user = getUser();
        UserRelation relation = userRelationService.getRelation(user.getUuid());
        // 总人数
        int total = relation.getUser1Total() + relation.getUser2Total() + relation.getUser3Total();
        // 直属高级
        Integer totalHigh = relation.getUser1HighTotal();
        totalHigh = totalHigh == null ? 0 : totalHigh;
        // 今日人数
        List<UserRelation> userRelationsToday = userRelationService.countPeopleToday(user.getUuid());
        // 昨天人数
        List<UserRelation> userRelationYesterday = userRelationService.countPeopleYesterday(user.getUuid());
        Integer people_directly = relation.getUser1Total();
        Integer people_directly_next = relation.getUser2Total() + relation.getUser3Total();

        TeamPopleCountResponse teamPopleCountResponse = new TeamPopleCountResponse();
        teamPopleCountResponse.setPeople_directly(people_directly);
        teamPopleCountResponse.setPeople_total(total);
        teamPopleCountResponse.setPeople_directly_next(people_directly_next);
        teamPopleCountResponse.setPeople_yesterday(userRelationYesterday.size());
        teamPopleCountResponse.setPeople_today(userRelationsToday.size());
        teamPopleCountResponse.setPeople_directly_super(totalHigh);
        return successResult(teamPopleCountResponse);
    }
}
