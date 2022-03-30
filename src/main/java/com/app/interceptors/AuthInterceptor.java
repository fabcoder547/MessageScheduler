/*
* 	This will be interceptor for authenticatio purpose....
*
* */




package com.app.interceptors;

import com.app.dao.MessageDao;
import com.app.pojo.Client;
import com.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthInterceptor implements HandlerInterceptor {


	@Autowired
	AuthService authService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("In a prehandler method...");
		String token = request.getHeader("token");

		Client client = authService.validateToken(token);
		if (client == null) {
			request.setAttribute("client",null);
			return true;
		}
		System.out.println("client here..." + client.toString());
		request.setAttribute("client", client);
		return true;
	}
















}
