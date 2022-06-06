package com.spring.webflux.todo.repository;

import com.spring.webflux.todo.entity.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {}
