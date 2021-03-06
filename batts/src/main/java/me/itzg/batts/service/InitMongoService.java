package me.itzg.batts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.itzg.batts.domain.BatteryBundle;
import me.itzg.batts.domain.BatteryTypes;
import me.itzg.batts.domain.Household;
import me.itzg.batts.domain.BatteryTypes.Type;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class InitMongoService {
	private static Logger logger = Logger.getLogger(InitMongoService.class);

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	BatteryTypes batteryTypes;

	public void init() throws IOException {
		logger.info("starting with mongoTemplate " + mongoTemplate);

		if (!mongoTemplate.collectionExists(Household.class)) {
			logger.info("Household collection not present, so inserting a default one");
			createHousehold();
		}
	}
	
	public Household createHousehold() {
		Household household = new Household();
		List<BatteryBundle> emptyBundles = new ArrayList<BatteryBundle>();
		for (Type type : batteryTypes.getTypes()) {
			emptyBundles.add(new BatteryBundle(type.getKey(), 0));
		}
		household.setAvailable(emptyBundles);
		
		mongoTemplate.insert(household);

		return household;
	}
}
