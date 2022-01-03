package com.util.async;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.util.cloud.Environment.getProperty;

/**
 * ExecutorsProvider class provides static access to application shared ExecutorServices to be used by asynchronous
 * methods (tasks implemented using CompletableFutures that run asynchronously).
 */
public class ExecutorsProvider {

	private static final int APP_EXECUTOR_POOL_SIZE = getProperty("pool.size", 5);;

	private static class ExecutorsServiceHolder {
		static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

	}


	public static ExecutorService getExecutorService() {
		return ExecutorsServiceHolder.executorService;
	}
}
