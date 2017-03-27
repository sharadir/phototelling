package com.phototell.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

public final class Threads {

	private static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();
	private static final Handler mainHandler = new Handler(Looper.getMainLooper());

	public static ExecutorService getCachedPool() {
		return CACHED_THREAD_POOL;
	}

	public static boolean isMainThread() {
		//noinspection ObjectEquality
		return Looper.myLooper() == Looper.getMainLooper();
	}

	public static void runInBackground(@NonNull Runnable runnable) {
		if (Threads.isMainThread()) {
			Threads.getCachedPool().submit(runnable);
		} else {
			runnable.run();
		}
	}

	@NonNull
	public static <V> Future<V> postOnBackground(@NonNull Callable<V> callable) {
		return getCachedPool().submit(callable);
	}

	public static void postOnBackground(@NonNull Runnable runnable) {
		getCachedPool().submit(runnable);
	}

	public static void postOnUiThread(@NonNull Runnable runnable) {
		mainHandler.post(runnable);
	}

	public static void postOnUiThreadDelayed(@NonNull Runnable runnable, long delayMillis) {
		mainHandler.postDelayed(runnable, delayMillis);
	}

}
