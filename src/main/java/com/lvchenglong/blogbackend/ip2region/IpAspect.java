package com.lvchenglong.blogbackend.ip2region;

import com.lvchenglong.blogbackend.utils.AddressUtil;
import com.lvchenglong.blogbackend.utils.HttpContextUtil;
import com.lvchenglong.blogbackend.utils.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Aspect
@Component
public class IpAspect {
    @Pointcut("@annotation(com.lvchenglong.blogbackend.ip2region.ip)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        System.out.println(MessageFormat.format("当前IP为:[{0}]；当前IP地址解析出来的地址为:[{1}]", ip, AddressUtil.getCityInfo(ip)));
        return point.proceed();
    }
}
