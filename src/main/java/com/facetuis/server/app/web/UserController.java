package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.response.UserRecommanderResponse;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.user.UserService;
import com.facetuis.server.utils.NeedLogin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1.0/user")
public class UserController extends FacetuisController{

    @Autowired
    private UserService userService;

    @RequestMapping("/recommender")
    @NeedLogin(needLogin = true)
    public BaseResponse getCommander() {
        User user = getUser();
        UserRecommanderResponse response = new UserRecommanderResponse();
        User recommander = userService.findRecommander(user.getUuid());
        if(recommander == null){
            return successResult(response);
        }
        BeanUtils.copyProperties(recommander, response);
        return successResult(new UserRecommanderResponse());
    }




}
