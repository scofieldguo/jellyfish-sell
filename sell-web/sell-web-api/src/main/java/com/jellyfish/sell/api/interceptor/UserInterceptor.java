package com.jellyfish.sell.api.interceptor;


import com.jellyfish.sell.support.aes.AES;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.service.IUserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Autowired
    private IUserDataService userDataService;

    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.key}")
    private String key;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String userId = request.getParameter("userId");
        try {
            if (userId != null&&!"".equals(userId)) {
               String deUser = AES.decrypt(userId, key, iv);
               Long userIdL = Long.valueOf(deUser);
               UserData userData  = userDataService.findUserDataById(userIdL);
               request.setAttribute("userId", userIdL);
            }
            return true;
        } catch (Exception e) {
            logger.error("userId="+userId+"url="+request.getRequestURL(),e);
            e.printStackTrace();
            return false;
        }
    }
}
