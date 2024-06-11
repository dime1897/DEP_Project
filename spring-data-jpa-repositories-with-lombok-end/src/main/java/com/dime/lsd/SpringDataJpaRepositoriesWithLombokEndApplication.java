package com.dime.lsd;

import com.dime.lsd.persistence.model.Project;
import com.dime.lsd.persistence.model.Task;
import com.dime.lsd.persistence.model.TaskStatus;
import com.dime.lsd.persistence.model.Worker;
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

		//Adding project using Lombok builder
		Project new_project = Project.builder()
				.code("NEWPRJ")
				.name("My Test Project")
				.description("This example make me feel like I got it!")
				.build();
		projectRepository.save(new_project);
		//Adding tasks using lombok builder testing also default values
		Task new_task = Task.builder()
				.name("NEWTSK1")
				.description("First task of my first project")
				.dueDate(LocalDate.of(2024,11,22))
				.project(new_project)
				.build();
		taskRepository.save(new_task);
		Task new_task2 = Task.builder()
				.name("NEWTSK2")
				.description("Second task of my first project")
				.dueDate(LocalDate.of(2024,8,1))
				.project(new_project)
				.build();
		taskRepository.save(new_task2);
		Task new_task3 = Task.builder()
				.name("NEWTSK3")
				.description("Third task of my first project")
				.dueDate(LocalDate.of(2024,12,7))
				.project(new_project)
				.status(TaskStatus.ON_HOLD)
				.build();
		taskRepository.save(new_task3);

		//Adding workers using lombok builders
		Worker mario_rossi = Worker.builder()
				.firstName("Mario")
				.lastName("Rossi")
				.email("mario.rossi@test.com")
				.build();
		workerRepository.save(mario_rossi);
		Worker nicola_bicocchi = Worker.builder()
				.firstName("Nicola")
				.lastName("Bicocchi")
				.email("nicola.bicocchi@test.com")
				.build();
		workerRepository.save(nicola_bicocchi);

		//Adding task with assignee
		Task new_task4 = Task.builder()
				.name("NEWTSK4")
				.description("Fourth task of my first project")
				.dueDate(LocalDate.of(2025,2,1))
				.project(new_project)
				.assignee(mario_rossi)
				.build();
		taskRepository.save(new_task4);

		Optional<Task> project5 = taskRepository.findById(5L);
		LOG.info("Task by ID 5: {}", project5);

		Optional<Task> project6 = taskRepository.findById(6L);
		LOG.info("Task by ID 6: {}", project6);

		Optional<Task> project7 = taskRepository.findById(7L);
		LOG.info("Task by ID 7: {}", project7);

		Optional<Task> project8 = taskRepository.findById(8L);
		LOG.info("Task by ID 8: {}", project8);

		Optional<Project> myproject = projectRepository.findById(4L);
		LOG.info("My new project with tasks: {}", myproject);
	}

}
