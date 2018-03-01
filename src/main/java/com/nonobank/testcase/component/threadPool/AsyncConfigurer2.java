package com.nonobank.testcase.component.threadPool;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import com.nonobank.testcase.component.config.TaskThreadPoolConfig;

@Component
public class AsyncConfigurer2 implements AsyncConfigurer {
	
	public static Logger logger = LoggerFactory.getLogger(AsyncConfigurer2.class);
	
	@Autowired
	TaskThreadPoolConfig taskThreadPoolConfig;

	@Override
	public Executor getAsyncExecutor() {
		// TODO Auto-generated method stub
		logger.info("初始化线程池");
		
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		threadPool.setCorePoolSize(taskThreadPoolConfig.getCorePoolSize());
		threadPool.setMaxPoolSize(taskThreadPoolConfig.getMaxPoolSize());
		threadPool.setKeepAliveSeconds(taskThreadPoolConfig.getKeepAliveSeconds());
		threadPool.setQueueCapacity(taskThreadPoolConfig.getQueueCapacity());
		threadPool.setThreadNamePrefix("Async2-");
		threadPool.initialize();
		return threadPool;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return new AsyncUncaughtExceptionHandler2();
	}
	
	class AsyncUncaughtExceptionHandler2 implements AsyncUncaughtExceptionHandler{
		
		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
			// TODO Auto-generated method stub
			logger.error("捕获线程异常信息...");
			
//			logger.error("Exception message - " + ex.getMessage());
			
//			ex.getCause().printStackTrace();
			
			ex.printStackTrace();
			
//            logger.error("Method name - " + method.getName());
            /**
            for (Object param : params) {
            	logger.error("Parameter value - " + param);
            }**/
		}
	}

}
