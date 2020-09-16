package com.github.schedule;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CronTaskRegister implements DisposableBean {

	private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

	@Resource
	private TaskScheduler taskScheduler;

	public TaskScheduler getScheduler() {
		return this.taskScheduler;
	}

	public void addCronTask(Runnable task, String cronExpression) {
		addCronTask(new CronTask(task, cronExpression));
	}

	public void addCronTask(CronTask cronTask) {
		if (cronTask != null) {
			Runnable task = cronTask.getRunnable();
			if (this.scheduledTasks.containsKey(task)) {
				removeCronTask(task);
			}

			this.scheduledTasks.put(task, scheduleCronTask(cronTask));
		}
	}

	public void addFixedRateTask(Runnable task, long interval) {
		addFixedRateTask(new FixedRateTask(task, interval, 0));
	}

	public void addFixedRateTask(FixedRateTask fixedRateTask) {
		if (fixedRateTask != null) {
			Runnable task = fixedRateTask.getRunnable();
			if (this.scheduledTasks.containsKey(task)) {
				removeCronTask(task);
			}

			this.scheduledTasks.put(task, scheduledRateTask(fixedRateTask));
		}
	}

	public ScheduledTask scheduledRateTask(FixedRateTask fixedRateTask) {
		ScheduledTask scheduledTask = new ScheduledTask();
		scheduledTask.future = this.taskScheduler.scheduleWithFixedDelay(fixedRateTask.getRunnable(), fixedRateTask.getInterval());
		return scheduledTask;
	}

	public void removeCronTask(Runnable task) {
		ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
		if (scheduledTask != null)
			scheduledTask.cancel();
	}

	public ScheduledTask scheduleCronTask(CronTask cronTask) {
		ScheduledTask scheduledTask = new ScheduledTask();
		scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());

		return scheduledTask;
	}

	@Override
	public void destroy() {
		for (ScheduledTask task : this.scheduledTasks.values()) {
			task.cancel();
		}

		this.scheduledTasks.clear();
	}
}