package net.itzgande.batts.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.itzgande.batts.domain.BatteryBundle;
import net.itzgande.batts.domain.Household;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/household/api")
public class HouseholdApiController {
	private static final Logger logger = LoggerFactory
			.getLogger(HouseholdApiController.class);
	
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
	
	/**
	 * 
	 * @param count
	 * @return the new available count
	 */
	@RequestMapping(value="incAvailable/{batteryTypeKey}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public int incAvailable(HttpServletRequest request, @PathVariable String batteryTypeKey, @RequestParam int count) {
		logger.info("Incrementing available "+batteryTypeKey+" by "+count);
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		final Query query = new Query(where("_id").is(householdToFind.getId()).and("available.batteryTypeKey").is(batteryTypeKey));
		query.fields().include("available");
		Household result = mongoTemplate.findAndModify(query, 
				new Update().inc("available.$.count", count),
				new FindAndModifyOptions().returnNew(true),
				Household.class);
		for (BatteryBundle bb : result.getAvailable()) {
			if (bb.getBatteryTypeKey().equals(batteryTypeKey)) {
				return bb.getCount();
			}
		}
		logger.error("Updated but battery type didn't match result extraction for "+batteryTypeKey);
		return 0;
	}
	
	public static class Counts {
		public String batteryTypeKey;
		public int available;
		public int inuse;
	}
	
	@RequestMapping(value="counts", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Counts> findBundleCounts(HttpServletRequest request) {
		Household householdToFind = (Household) request.getAttribute(Household.ATTRIBUTE_NAME);
		
		final Query query = query(where("_id").is(householdToFind.getId()));
		query.fields().include("available");
		Household household = mongoTemplate.findOne(query, Household.class);
		
		List<Counts> results = new ArrayList<Counts>();
		for (BatteryBundle b : household.getAvailable()) {
			final Counts c = new Counts();
			c.batteryTypeKey = b.getBatteryTypeKey();
			c.available = b.getCount();
			results.add(c);
		}
		
		return results;
	}
}
