package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.response.TeamOrderCountResponse;
import com.facetuis.server.app.web.response.TeamPopleCountResponse;
import com.facetuis.server.app.web.response.TeamUsersResponse;
import com.facetuis.server.model.order.Order;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserRelation;
import com.facetuis.server.service.pinduoduo.OrderCommisionService;
import com.facetuis.server.service.pinduoduo.OrderService;
import com.facetuis.server.service.pinduoduo.response.OrderVO;
import com.facetuis.server.service.pinduoduo.response.TeamIncomVO;
import com.facetuis.server.service.user.UserRelationService;
import com.facetuis.server.service.user.UserService;
import com.facetuis.server.utils.NeedLogin;
import com.facetuis.server.utils.SMSUtils;
import com.facetuis.server.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/1.0/team")
public class TeamController extends FacetuisController {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderCommisionService orderCommisionService;

    @RequestMapping(value = "/people/count",method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse teamPeopleCount(){
        User user = getUser();
        UserRelation relation = userRelationService.getRelation(user.getUuid());
        // 总人数
        int total = userRelationService.getTotal(user,relation);
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


    @RequestMapping("/orders/count")
    @NeedLogin(needLogin = true)
    public BaseResponse getTeamOrdersCount(){
        TeamOrderCountResponse response = new TeamOrderCountResponse();
        PageRequest pageable = PageRequest.of(0,10);
        User user = getUser();
        String todayTime = TimeUtils.date2String(new Date());
        String yesterdayTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),1));
        // 当天订单
        Page<Order> todayOrders = orderService.findByDate(todayTime + " 00:00:00", todayTime + " 23:59:59", user.getUuid(), pageable);
        response.setOrder_today( (int)todayOrders.getTotalElements());
        // 所有订单
        Page<Order> teamOrders = orderService.findTeamOrders(user.getUuid(), pageable);
        response.setOrder_total( (int)teamOrders.getTotalElements());
        // 昨天订单
        Page<Order> yesterdayOrders = orderService.findByDate(yesterdayTime + " 00:00:00", yesterdayTime + " 23:59:59", user.getUuid(), pageable);
        response.setOrder_yesterday((int)yesterdayOrders.getTotalElements());
        //本月订单
        String monthFirst = TimeUtils.getMonthFirstDay();
        String monthLast = TimeUtils.getMonthLastDay();
        Page<Order> monthyOrders = orderService.findByDate(monthFirst + " 00:00:00", monthLast + " 23:59:59", user.getUuid(), pageable);
        response.setOrder_this_month((int)monthyOrders.getTotalElements());
        //上月订单
        String upperMonthFirst = TimeUtils.upperMonthFirst();
        String upperMonthLast = TimeUtils.upperMonthLast();
        Page<Order> upperMonthyOrders = orderService.findByDate(upperMonthFirst + " 00:00:00", upperMonthLast + " 23:59:59", user.getUuid(), pageable);
        response.setOrder_last_month((int)upperMonthyOrders.getTotalElements());
        return successResult(response);
    }

    @RequestMapping(value = "/orders/{status}",method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse teamOrders(@PathVariable int status,String keywords,Pageable request){
        String nickName = "";
        String mobile = "";
        User user = getUser();
        if(!StringUtils.isEmpty(keywords)){
            if(SMSUtils.isChinaPhoneLegal(keywords)){
                mobile = keywords;
            }else {
                nickName = keywords;
            }
        }else{
            Page<OrderVO> statusOrders = orderService.findByStatus(user.getUuid(), status, request);
            return successResult(statusOrders);
        }
        Page<OrderVO> orders = orderService.findByMobileOrNickName(user.getUuid(), mobile, nickName, status, request);
        return successResult(orders);
    }

    @RequestMapping(value = "/people",method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse teamPeople(String keywords){
        String nickName = "";
        String mobile = "";
        User user = getUser();
        List<User> users = null;
        if(!StringUtils.isEmpty(keywords)){
            if(SMSUtils.isChinaPhoneLegal(keywords)){
                mobile = keywords;
            }else {
                nickName = keywords;
            }
            // 根据条件查询团队成员
            users = userService.findByNickNameOrMobile(nickName, mobile);
        }else{
            // 查询所有成员
            List<String> team = userRelationService.getTeam(user.getUuid());
            users = userService.findByIds(team);
        }
        List<TeamUsersResponse> responses = new ArrayList<>();
        if(user != null){
            for(User u : users){
                List<User> byInviteCode = userService.findByInviteCode(u.getRecommandCode());
                TeamUsersResponse response = new TeamUsersResponse();
                BeanUtils.copyProperties(u,response);
                response.setRecommandNumber(byInviteCode.size());
                responses.add(response);
            }
        }
        return successResult(responses);
    }

    /**
     * 查询团队收入
     * @return
     */
    @RequestMapping(value = "/income/count")
    @NeedLogin(needLogin = true)
    public BaseResponse income(){
        User user = getUser();
        TeamIncomVO income = orderCommisionService.getIncome(user.getUuid());
        return successResult(income);
    }
}
