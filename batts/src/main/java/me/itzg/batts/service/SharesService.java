package me.itzg.batts.service;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import me.itzg.batts.domain.BattsUser;
import me.itzg.batts.domain.Household;
import me.itzg.batts.domain.HouseholdShare;
import me.itzg.batts.repositories.HouseholdShareRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharesService {

	private static final long EXPIRATION_INTERVAL = 24*60*60*1000;
	@Autowired
	HouseholdShareRepository repository;

	public HouseholdShare allocate(Household household, BattsUser battsUser) {
		purge();
		
		Random rand = new Random();
		// generate 4 digit value
		int shareCode = rand.nextInt(8999) + 1000;
		while (repository.findOne(shareCode) != null) {
			shareCode = rand.nextInt(8999) + 1000;
		}
		
		HouseholdShare share = new HouseholdShare(shareCode, 
				new Date(System.currentTimeMillis()+EXPIRATION_INTERVAL), 
				household, battsUser);
		
		repository.save(share);
		
		return share;
	}
	
	public HouseholdShare join(int shareCode) {
		purge();
		
		HouseholdShare result = repository.findOne(shareCode);
		
		if (result != null) {
			// one time use, so delete it
			repository.delete(result);
		}
		
		return result;
	}
	
	private void purge() {
		Collection<HouseholdShare> expired = repository.findByExpiresLessThan(new Date());
		for (HouseholdShare householdShare : expired) {
			repository.delete(householdShare);
		}
	}
}
