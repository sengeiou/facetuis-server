package com.facetuis.server.admin.web;

import com.facetuis.server.admin.web.request.LoginRequest;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/login")
@RestController
public class AdminLoginController extends FacetuisController {

    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse login(@RequestBody LoginRequest loginRequest, BindingResult bindingResult){



        return successResult();
    }
}
