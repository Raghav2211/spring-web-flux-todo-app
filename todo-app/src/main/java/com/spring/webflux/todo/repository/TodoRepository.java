package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodoRepository extends ReactiveMongoRepository<Todo, String> {

  Flux<Todo> findAllBySectionId(String sectionId);

  Mono<Todo> findBySectionIdAndId(String sectionId, String id);
}
