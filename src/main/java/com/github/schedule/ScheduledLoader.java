package com.github.schedule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ScheduledLoader implements CommandLineRunner {

	@Resource
	private CronTaskRegister cronTaskRegister;

	@Override
	public void run(String... args) throws Exception {
		// add task to scheduledTasks
	}
}
