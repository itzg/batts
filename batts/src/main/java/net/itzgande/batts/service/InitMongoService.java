package net.itzgande.batts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.itzgande.batts.domain.BatteryBundle;
import net.itzgande.batts.domain.BatteryTypes;
import net.itzgande.batts.domain.BatteryTypes.Type;
import net.itzgande.batts.domain.Household;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

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
			Household household = new Household();
			List<BatteryBundle> emptyBundles = new ArrayList<BatteryBundle>();
			for (Type type : batteryTypes.getTypes()) {
				emptyBundles.add(new BatteryBundle(type.getKey(), 0));
			}
			household.setAvailable(emptyBundles);
			
			mongoTemplate.insert(household);
		}
	}
}
