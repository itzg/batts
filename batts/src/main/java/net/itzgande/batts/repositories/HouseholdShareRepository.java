package net.itzgande.batts.repositories;

import java.util.Collection;
import java.util.Date;

import net.itzgande.batts.domain.HouseholdShare;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseholdShareRepository extends MongoRepository<HouseholdShare, Integer> {

	Collection<HouseholdShare> findByExpiresLessThan(Date threshold);
}
