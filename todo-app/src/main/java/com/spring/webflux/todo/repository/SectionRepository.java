package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Section;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SectionRepository extends ReactiveMongoRepository<Section, String> {
  Flux<Section> findByUserId(String userId);
}
