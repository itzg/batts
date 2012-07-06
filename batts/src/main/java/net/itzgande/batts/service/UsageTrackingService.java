package net.itzgande.batts.service;

import java.util.Date;

import net.itzgande.batts.domain.BattsUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UsageTrackingService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	public void accessed(BattsUser user) {
		mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(user.getOpenId())), 
				new Update().inc("accessCount", 1).set("lastAccess", new Date()), 
				BattsUser.class);
	}
}
