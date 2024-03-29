package com.github.devraghav.springexamples.todo.repository;

import com.github.devraghav.springexamples.todo.entity.Todo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodoRepository extends ReactiveCrudRepository<Todo, String>, CustomTodoRepository {

  Flux<Todo> findAllBySectionId(String sectionId);

  Mono<Todo> findBySectionIdAndId(String sectionId, String id);
}
