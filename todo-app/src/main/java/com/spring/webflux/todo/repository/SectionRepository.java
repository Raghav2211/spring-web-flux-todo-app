package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Section;
import com.spring.webflux.todo.entity.UserTodoList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SectionRepository {
  private final ReactiveMongoTemplate template;

  public Mono<Boolean> addSection(String userId, String sectionIdentifier) {
    var section = new Section();
    section.setName(sectionIdentifier);
    return template
        .upsert(
            Query.query(Criteria.where("_id").is(userId)),
            new Update().addToSet("sections").value(section),
            UserTodoList.class)
        .map(updateResult -> updateResult.getModifiedCount() == 1);
  }

  public static void main(String[] args) {
    Mono.just(1)
        .map(data -> data.equals(1))
        .filter(data -> data)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("heyyy ")))
        .block();
  }
}
