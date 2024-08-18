package com.lvchenglong.blogbackend.controller;

import com.lvchenglong.blogbackend.common.Result;
import com.lvchenglong.blogbackend.utils.AddressUtil;
import com.lvchenglong.blogbackend.utils.HttpContextUtil;
import com.lvchenglong.blogbackend.utils.IPUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ip")
public class IpController {
    @GetMapping
    public Result getIpInfo(){
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        String cityInfo = AddressUtil.getCityInfo(ip);
        // return "IP:" + ip + "Address:" + cityInfo;
        Map<String, String> response = new HashMap<>();
        response.put("ip", ip);
        response.put("address", cityInfo);
        return Result.success(response);
    }

}
