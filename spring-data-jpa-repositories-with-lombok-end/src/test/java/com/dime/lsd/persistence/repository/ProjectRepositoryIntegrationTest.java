package com.dime.lsd.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dime.lsd.SpringDataJpaRepositoriesWithLombokEndApplication;
import com.dime.lsd.persistence.model.Project;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringDataJpaRepositoriesWithLombokEndApplication.class }, initializers = ConfigDataApplicationContextInitializer.class)
class ProjectRepositoryIntegrationTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void givenLoadedProjects_whenFindByID_thenSuccess() {
        Optional<Project> savedProject = projectRepository.findById(1L);
        assertNotNull(savedProject.get());
    }
}