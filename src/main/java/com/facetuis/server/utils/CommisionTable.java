package com.facetuis.server.utils;

import com.facetuis.server.model.commision.CommisionSettings;

import java.util.HashMap;
import java.util.Map;

public class CommisionTable {
    private static Map<String,CommisionUser> map = new HashMap<>();

    private static Map<String,CommisionUser> init(){
        if(map != null){
            CommisionSettings cs = new CommisionSettings();
            double zongFenChu = cs.getTotalRatio();
            double teamNoSvip = cs.getTeamNoSvip();
            double teamHaveSvip = cs.getTeamHaveSvip();
            double directorNoSvip = cs.getDirectorNoSvip();  // 总监下无总监
            double directorHaveSvip = cs.getDirectorHaveSvip();  // 总监下有总监
            double directorSelf = cs.getDirectorSelf();  // 总监自买分出
            CommisionUser s1 = new CommisionUser();
            map.put("0000",s1);
            // 购买人0
            s1.setUserRate(zongFenChu);
            // 购买人上级00
            s1.setUser1Rate(zongFenChu * teamNoSvip);
            // 购买人上上级000
            s1.setUser2Rate(0);
            // 购买人上上上级0000
            s1.setUser3Rate(0);
            CommisionUser s2 = new CommisionUser();
            map.put("0010",s2);
            // 购买人0
            s2.setUserRate(zongFenChu);
            // 购买人上级00
            s2.setUser1Rate(zongFenChu * teamNoSvip);
            // 购买人上上级001
            s2.setUser2Rate(zongFenChu * directorNoSvip);
            // 购买人上上上级0010
            s2.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s3 = new CommisionUser();
            map.put("0011",s3);
            // 购买人0
            s3.setUserRate(zongFenChu);
            // 购买人上级00
            s3.setUser1Rate(zongFenChu * teamNoSvip);
            // 购买人上上级001
            s3.setUser2Rate(zongFenChu * directorNoSvip);
            s3.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s4 = new CommisionUser();
            map.put("0100",s4);
            s4.setUserRate(zongFenChu);
            s4.setUser1Rate(zongFenChu * directorNoSvip);
            s4.setUser2Rate(zongFenChu * teamHaveSvip);
            s4.setUser3Rate(0);
            CommisionUser s5 = new CommisionUser();
            map.put("0101",s5);
            s5.setUserRate(zongFenChu);
            s5.setUser1Rate(zongFenChu * directorNoSvip);
            s5.setUser2Rate(zongFenChu * teamHaveSvip);
            s5.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s6 = new CommisionUser();
            map.put("0110",s6);
            s6.setUserRate(zongFenChu);
            s6.setUser1Rate(zongFenChu * directorNoSvip);
            s6.setUser2Rate(zongFenChu * directorHaveSvip);
            s6.setUser3Rate(zongFenChu * teamHaveSvip);
            CommisionUser s7 = new CommisionUser();
            map.put("0111",s7);
            s7.setUserRate(zongFenChu);
            s7.setUser1Rate(zongFenChu * directorNoSvip);
            s7.setUser2Rate(zongFenChu * directorHaveSvip);
            s7.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s8 = new CommisionUser();
            map.put("1000",s8);
            s8.setUserRate(zongFenChu * (directorSelf + 1) );
            s8.setUser1Rate(zongFenChu * teamHaveSvip);
            s8.setUser2Rate(0);
            s8.setUser3Rate(0);
            CommisionUser s9 = new CommisionUser();
            map.put("1001",s9);
            s9.setUserRate(zongFenChu * (directorSelf + 1) );
            s9.setUser1Rate(zongFenChu * teamHaveSvip);
            s9.setUser2Rate(0);
            s9.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s10 = new CommisionUser();
            map.put("1010",s10);
            s10.setUserRate(zongFenChu * (directorSelf + 1) );
            s10.setUser1Rate(zongFenChu * teamHaveSvip);
            s10.setUser2Rate(zongFenChu * directorHaveSvip);
            s10.setUser3Rate(zongFenChu * teamHaveSvip);
            CommisionUser s11 = new CommisionUser();
            map.put("1011",s11);
            s11.setUserRate(zongFenChu * (directorSelf + 1) );
            s11.setUser1Rate(zongFenChu * teamHaveSvip);
            s11.setUser2Rate(zongFenChu * directorHaveSvip);
            s11.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s12 = new CommisionUser();
            map.put("1100",s12);
            s12.setUserRate(zongFenChu * (directorSelf + 1) );
            s12.setUser1Rate(zongFenChu * directorHaveSvip);
            s12.setUser2Rate(zongFenChu * teamHaveSvip);
            s12.setUser3Rate(0);
            CommisionUser s13 = new CommisionUser();
            map.put("1101",s13);
            s13.setUserRate(zongFenChu * (directorSelf + 1) );
            s13.setUser1Rate(zongFenChu * directorHaveSvip);
            s13.setUser2Rate(zongFenChu * teamHaveSvip);
            s13.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s14 = new CommisionUser();
            map.put("1110",s14);
            s14.setUserRate(zongFenChu * (directorSelf + 1) );
            s14.setUser1Rate(zongFenChu * directorHaveSvip);
            s14.setUser2Rate(zongFenChu * directorHaveSvip);
            s14.setUser3Rate(zongFenChu * teamHaveSvip);
            CommisionUser s15 = new CommisionUser();
            map.put("1111",s15);
            s15.setUserRate(zongFenChu * (directorSelf + 1) );
            s15.setUser1Rate(zongFenChu * directorHaveSvip);
            s15.setUser2Rate(zongFenChu * directorHaveSvip);
            s15.setUser3Rate(zongFenChu * directorHaveSvip);
            CommisionUser s16 = new CommisionUser();
            map.put("0001",s16);
            s16.setUserRate(zongFenChu);
            s16.setUser1Rate(zongFenChu * teamNoSvip);
            s16.setUser2Rate(0);
            s16.setUser3Rate(zongFenChu * directorNoSvip);
        }
        return map;
    }


    public CommisionUser getRate(String relation){
        Map<String, CommisionUser> map = init();
        return map.get(relation);
    }
}
