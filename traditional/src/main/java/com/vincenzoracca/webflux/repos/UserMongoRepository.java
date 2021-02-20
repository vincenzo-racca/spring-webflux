package com.vincenzoracca.webflux.repos;

import com.vincenzoracca.webflux.domains.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserMongoRepository extends ReactiveMongoRepository<User, String> {
}
