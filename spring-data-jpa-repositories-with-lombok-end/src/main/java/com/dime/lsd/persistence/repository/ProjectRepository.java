package com.dime.lsd.persistence.repository;

import com.dime.lsd.persistence.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
