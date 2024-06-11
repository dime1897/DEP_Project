package com.dime.lsd;

import com.dime.lsd.persistence.model.Project;
import com.dime.lsd.persistence.model.Task;
import com.dime.lsd.persistence.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dime.lsd.persistence.repository.ProjectRepository;
import com.dime.lsd.persistence.repository.TaskRepository;
import com.dime.lsd.persistence.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class SpringDataJpaRepositoriesWithLombokEndApplication implements ApplicationRunner {

	@Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TaskRepository taskRepository;

	private static final Logger LOG = LoggerFactory.getLogger(SpringDataJpaRepositoriesWithLombokEndApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaRepositoriesWithLombokEndApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Iterable<Project> allProjects = projectRepository.findAll();
		LOG.info("All Projects: {}", allProjects);

		Optional<Task> project1 = taskRepository.findById(1L);
		LOG.info("Task by id 1: {}", project1);

		long noOfWorkers = workerRepository.count();
		LOG.info("Number of workers: {}", noOfWorkers);

		//Example adding new a project using Lombok builder
		Project new_project = Project.builder()
				.code("NEWPRJ")
				.name("My Test Project")
				.description("This example make me feeling like I got it!")
				.build();

		//Adding tasks using lombok builder testing also default values
		Task new_task = Task.builder()
				.name("NEWTSK1")
				.description("First task of my first project")
				.dueDate(LocalDate.of(2024,11,22))
				.project(new_project)
				.build();
		Task new_task2 = Task.builder()
				.id(52L)
				.name("NEWTSK2")
				.description("Second task of my first project")
				.dueDate(LocalDate.of(2024,8,1))
				.project(new_project)
				.build();
		Task new_task3 = Task.builder()
				.id(53L)
				.name("NEWTSK3")
				.description("Third task of my first project")
				.dueDate(LocalDate.of(2024,12,7))
				.project(new_project)
				.status(TaskStatus.ON_HOLD)
				.build();

		projectRepository.save(new_project);
		taskRepository.save(new_task);
		taskRepository.save(new_task2);
		taskRepository.save(new_task3);

		Optional<Task> project5 = taskRepository.findById(5L);
		LOG.info("All Tasks by ID 5: {}", project5);

		Optional<Task> project6 = taskRepository.findById(6L);
		LOG.info("All Tasks by ID 6: {}", project6);

		Optional<Task> project52 = taskRepository.findById(52L);
		LOG.info("All Tasks by ID 5.2: {}", project52);

		Optional<Task> project53 = taskRepository.findById(53L);
		LOG.info("All Tasks by ID 5.3: {}", project53);
	}

}
