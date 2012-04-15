package net.itzgande.batts.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.itzgande.batts.domain.BatteryBundle;
import net.itzgande.batts.domain.Household;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/household/api")
public class HouseholdApiController {
	
	@Autowired
	MongoTemplate mongoTemplate;

	@RequestMapping(value="available", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<BatteryBundle> findAvailableBundles(HttpServletRequest request) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("available");
		Household household = mongoTemplate.findOne(query, Household.class);
		return household.getAvailable();
	}
}
