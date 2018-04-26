package com.facetuis.server.service.pinduoduo;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.service.basic.BasicService;
import com.facetuis.server.service.pinduoduo.commision.CommisionContext;
import com.facetuis.server.service.pinduoduo.commision.CommisionStrategy;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.service.pinduoduo.response.OrderListBody;
import com.facetuis.server.service.pinduoduo.response.OrderListResponse;
import com.facetuis.server.service.pinduoduo.response.TeamIncomVO;
import com.facetuis.server.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderCommisionService extends BasicService {

    @Autowired
    private OrderCommisionRepository orderCommisionRepository;
    @Autowired
    private CommisionContext commisionContext;
    @Autowired
    private UserRepository userRepository;

    /**
     * 订单佣金计算
     * 遍历订单
     * @param response
     */
    public void compute(OrderListResponse response){
        if(response == null || response.getOrder_list_get_response() == null){
            return;
        }
        List<OrderDetail> order_list = response.getOrder_list_get_response().getOrder_list();
        for(OrderDetail orderDetail : order_list){
            CommisionStrategy strategy = commisionContext.getStrategy(orderDetail.getOrderStatus());
            if(strategy != null) {
                OrderCommision orderCommision = strategy.doCompute(orderDetail);
                if(orderCommision != null){
                    orderCommisionRepository.save(orderCommision);
                }
            }
        }
        return;
    }

    /**
     * 统计指定用户的团队收入数据
     * @param userId
     * @return
     */
    public TeamIncomVO getIncome(String userId){
        TeamIncomVO vo = new TeamIncomVO();
        //////////////////////////////////////////////////////////////////////////
        ////////////////////////////////总收入////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////
        Long begTime = 0l;// 有史以来的订单
        Long endTime = System.currentTimeMillis()/1000; // 订单结束时间
        // 3级收入
        Long teamUser3Total = orderCommisionRepository.findTeamUser3Total(userId, begTime, endTime);
        // 2级收入
        Long teamUser2Total = orderCommisionRepository.findTeamUser2Total(userId, begTime, endTime);
        // 1级收入
        Long teamUser1Total = orderCommisionRepository.findTeamUser1Total(userId, begTime, endTime);
        vo.setIncome_total(teamUser1Total + teamUser2Total + teamUser3Total);


        ////////////////////////////////////////////////////////////////////////////
        //////////////////////////// 今天收入////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        String begStr = TimeUtils.date2String(new Date()) + " 00:00:00";
        String endStr = TimeUtils.date2String(new Date()) + " 23:59:59";
        begTime = TimeUtils.stringToDateTime(begStr).getTime() / 1000;
        endTime = TimeUtils.stringToDateTime(endStr).getTime() / 1000;
        // 3级收入
        Long teamUser3TotalToday = orderCommisionRepository.findTeamUser3Total(userId, begTime, endTime);
        // 2级收入
        Long teamUser2TotalToday = orderCommisionRepository.findTeamUser2Total(userId, begTime, endTime);
        // 1级收入
        Long teamUser1TotalToday = orderCommisionRepository.findTeamUser1Total(userId, begTime, endTime);
        vo.setIncome_today(teamUser1TotalToday + teamUser2TotalToday + teamUser3TotalToday);
        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////// 昨天收入//////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        begStr =  TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),1)) + " 00:00:00";
        endStr =  TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),1)) + " 23:59:59";
        begTime = TimeUtils.stringToDateTime(begStr).getTime() / 1000;
        endTime = TimeUtils.stringToDateTime(endStr).getTime() / 1000;
        // 3级收入
        Long teamUser3TotalYeartoday = orderCommisionRepository.findTeamUser3Total(userId, begTime, endTime);
        // 2级收入
        Long teamUser2TotalYeartoday = orderCommisionRepository.findTeamUser2Total(userId, begTime, endTime);
        // 1级收入
        Long teamUser1TotalYeartoday = orderCommisionRepository.findTeamUser1Total(userId, begTime, endTime);
        vo.setIncome_yesterday(teamUser1TotalYeartoday + teamUser2TotalYeartoday + teamUser3TotalYeartoday);

        /////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////本月收入////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////
        begStr =  TimeUtils.getMonthFirstDay() + " 00:00:00";
        endStr =  TimeUtils.getMonthLastDay() + " 23:59:59";
        begTime = TimeUtils.stringToDateTime(begStr).getTime() / 1000;
        endTime = TimeUtils.stringToDateTime(endStr).getTime() / 1000;
        // 3级收入
        Long teamUser3TotalMonth = orderCommisionRepository.findTeamUser3Total(userId, begTime, endTime);
        // 2级收入
        Long teamUser2TotalMonth = orderCommisionRepository.findTeamUser2Total(userId, begTime, endTime);
        // 1级收入
        Long teamUser1TotalMonth = orderCommisionRepository.findTeamUser1Total(userId, begTime, endTime);
        vo.setIncome_this_month(teamUser3TotalMonth + teamUser2TotalMonth + teamUser1TotalMonth);

        /////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////上月收入////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////
        begStr =  TimeUtils.getMonthFirstDay() + " 00:00:00";
        endStr =  TimeUtils.getMonthLastDay() + " 23:59:59";
        begTime = TimeUtils.stringToDateTime(begStr).getTime() / 1000;
        endTime = TimeUtils.stringToDateTime(endStr).getTime() / 1000;
        // 3级收入
        Long teamUser3TotalLastMonth = orderCommisionRepository.findTeamUser3Total(userId, begTime, endTime);
        // 2级收入
        Long teamUser2TotalLastMonth = orderCommisionRepository.findTeamUser2Total(userId, begTime, endTime);
        // 1级收入
        Long teamUser1TotalLastMonth = orderCommisionRepository.findTeamUser1Total(userId, begTime, endTime);
        vo.setIncome_last_month(teamUser3TotalLastMonth + teamUser2TotalLastMonth + teamUser1TotalLastMonth);
        return vo;
    }


}
