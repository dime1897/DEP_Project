package com.dime.lsd.persistence.repository;

import com.dime.lsd.persistence.model.Worker;
import org.springframework.data.repository.CrudRepository;

public interface WorkerRepository extends CrudRepository<Worker, Long> {
}
