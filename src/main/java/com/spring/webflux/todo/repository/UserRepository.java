package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {}
