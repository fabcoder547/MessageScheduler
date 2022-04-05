/*
 * 	This will be interceptor for authenticatio purpose....
 *
 * */


package com.app.interceptors;

import com.app.pojo.Client;
import com.app.pojo.Response;
import com.app.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/* AuthInterceptor will be used to authenticate user based on token passed in request headers...**/
@Component
public class AuthInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("In prehandler interceptor");
        String token = request.getHeader("token");
        logger.info("token as header " + token);
        Client client = authService.validateToken(token);
        if (client == null) {
            logger.info("client is NULL.....authentication failed.");
            response.setContentType("application/json");
            response.setStatus(400);
            PrintWriter out = response.getWriter();
            Response resp = new Response(1001, "Authentication failed");
            String responseString = new ObjectMapper().writeValueAsString(resp);
            out.print(responseString);
            return false;
        }
        logger.info("client here in interceptor " + client);
        request.setAttribute("client", client);
        return true;
    }


}
