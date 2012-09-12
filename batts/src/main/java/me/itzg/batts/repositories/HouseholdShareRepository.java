package me.itzg.batts.repositories;

import java.util.Collection;
import java.util.Date;

import me.itzg.batts.domain.HouseholdShare;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseholdShareRepository extends MongoRepository<HouseholdShare, Integer> {

	Collection<HouseholdShare> findByExpiresLessThan(Date threshold);
}
