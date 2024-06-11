package com.dime.lsd.persistence.repository;

import com.dime.lsd.persistence.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
