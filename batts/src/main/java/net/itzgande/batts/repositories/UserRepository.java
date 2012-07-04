package net.itzgande.batts.repositories;

import net.itzgande.batts.domain.BattsUser;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<BattsUser, String> {
	BattsUser findByOpenId(String openId);
}
