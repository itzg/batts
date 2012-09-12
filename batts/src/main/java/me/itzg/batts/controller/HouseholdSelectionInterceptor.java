package me.itzg.batts.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.itzg.batts.config.BattsUserDetails;
import me.itzg.batts.domain.BattsUser;
import me.itzg.batts.domain.Household;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HouseholdSelectionInterceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(HouseholdSelectionInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Principal user = request.getUserPrincipal();
		if (user == null) {
			logger.warn("user was not available, so redirecting to login");
			response.sendRedirect("/login");
			return false;
		}
		
		BattsUser battsUser = BattsUserDetails.extractFromPrincipal(user);

		if (battsUser.getHousehold() == null) {
			logger.warn("user did not have a household assigned yet, so redirecting to welcome: {}", battsUser);
			response.sendRedirect(response.encodeRedirectURL("/welcome"));
			return false;
		}
		
		request.setAttribute(Household.ATTRIBUTE_NAME, battsUser.getHousehold());

		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
