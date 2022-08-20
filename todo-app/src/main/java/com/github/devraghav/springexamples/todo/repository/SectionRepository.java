package com.github.devraghav.springexamples.todo.repository;

import com.github.devraghav.springexamples.todo.entity.Section;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SectionRepository extends ReactiveMongoRepository<Section, String> {
  Flux<Section> findByUserId(String userId);

  Mono<Boolean> existsByUserIdAndId(String userId, String id);
}
