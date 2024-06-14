package com.dime.ls.subscriberevo;

import com.dime.ls.subscriberevo.handler.ZeroMqReceiver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class SubscriberEvolutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberEvolutionApplication.class, args);
	}

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("ZeroMq-");
		executor.initialize();
		return executor;
	}

	@Bean
    @Scope("prototype")
	@DependsOn({"taskExecutor", "commandLineRunner"})
    public ZeroMqReceiver zeroMqReceiver() {
        return new ZeroMqReceiver();
    }

	@Bean(name = "commandLineRunner")
	@DependsOn("taskExecutor")
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			for(int i = 0; i<1; i++) {
				ctx.getBean(ZeroMqReceiver.class).receiveMessages();
			}
		};
	}
}
