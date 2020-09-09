package com.github.controller;

import com.github.model.vo.TaskVo;
import com.github.schedule.CronTaskRegister;
import com.github.schedule.SchedulingRunnable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * change your task plan during the application running
 * <p>
 * you can update a task by deleting it & adding a new one
 */
@RestController
public class TaskController {

	@Resource
	private CronTaskRegister cronTaskRegister;

	/**
	 * add task
	 */
	@PostMapping("task")
	public void addTask(@RequestBody TaskVo taskVo) {
		SchedulingRunnable schedulingRunnable = new SchedulingRunnable(taskVo.getBeanName(), taskVo.getMethodName(), taskVo.getMethodParams());
		cronTaskRegister.addCronTask(schedulingRunnable, taskVo.getCron());
	}

	/**
	 * delete a tak
	 */
	@DeleteMapping("task")
	public void deleteTask(@RequestBody TaskVo taskVo) {
		SchedulingRunnable schedulingRunnable = new SchedulingRunnable(taskVo.getBeanName(), taskVo.getMethodName(), taskVo.getMethodParams());
		cronTaskRegister.removeCronTask(schedulingRunnable);
	}
}
