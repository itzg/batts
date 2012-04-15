package net.itzgande.batts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itzgande.batts.domain.Household;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HouseholdSelectionInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(HouseholdSelectionInterceptor.class);
	
	@Autowired
	MongoTemplate mongoTemplate;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.warn("Using simplified HouseholdSelectionInterceptor");
		final Query query = new Query();
		query.fields().include("_id");
		Household household = mongoTemplate.findOne(query, Household.class);
		logger.info("Found the household "+household);
		request.setAttribute(Household.ATTRIBUTE_NAME, household);

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
