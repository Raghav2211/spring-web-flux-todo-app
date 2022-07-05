package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomTodoRepositoryImpl implements CustomTodoRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Mono<Void> disableTodo(String id) {
    return reactiveMongoTemplate
        .findAndModify(
            Query.query(Criteria.where("_id").is(id)), Update.update("isActive", false), Todo.class)
        .then();
  }
}
