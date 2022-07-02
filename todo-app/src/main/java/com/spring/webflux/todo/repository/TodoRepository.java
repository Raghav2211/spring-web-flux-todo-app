package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends ReactiveMongoRepository<Todo, String> {}
