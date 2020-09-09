package com.github.schedule;

import com.github.context.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class SchedulingRunnable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SchedulingRunnable.class);

	private String beanName;

	private String methodName;

	private String params;

	public SchedulingRunnable(String beanName, String methodName) {
		this(beanName, methodName, null);
	}

	public SchedulingRunnable(String beanName, String methodName, String params) {
		this.beanName = beanName;
		this.methodName = methodName;
		this.params = params;
	}

	@Override
	public void run() {
		logger.info("task begin - bean：{}，method：{}，params：{}", beanName, methodName, params);
		long startTime = System.currentTimeMillis();

		try {
			Object target = SpringContextUtils.getBean(beanName);

			Method method;
			if (!StringUtils.isEmpty(params)) {
				method = target.getClass().getDeclaredMethod(methodName, String.class);
			} else {
				method = target.getClass().getDeclaredMethod(methodName);
			}

			ReflectionUtils.makeAccessible(method);
			if (!StringUtils.isEmpty(params)) {
				method.invoke(target, params);
			} else {
				method.invoke(target);
			}
		} catch (Exception ex) {
			logger.error(String.format("task went wrong - bean：%s，method：%s，params：%s ", beanName, methodName, params), ex);
		}

		long times = System.currentTimeMillis() - startTime;
		logger.info("task finished - bean：{}，method：{}，params：{}，duration：{} ms", beanName, methodName, params, times);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SchedulingRunnable that = (SchedulingRunnable) o;
		if (params == null) {
			return beanName.equals(that.beanName) &&
					methodName.equals(that.methodName) &&
					that.params == null;
		}

		return beanName.equals(that.beanName) &&
				methodName.equals(that.methodName) &&
				params.equals(that.params);
	}

	@Override
	public int hashCode() {
		if (params == null) {
			return Objects.hash(beanName, methodName);
		}

		return Objects.hash(beanName, methodName, params);
	}
}
