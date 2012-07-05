package net.itzgande.batts.repositories;

import net.itzgande.batts.domain.HouseholdShare;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HouseholdShareRepository extends MongoRepository<HouseholdShare, Integer> {

}
