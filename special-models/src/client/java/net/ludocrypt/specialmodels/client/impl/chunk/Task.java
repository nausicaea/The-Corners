package net.ludocrypt.specialmodels.client.impl.chunk;

import com.google.common.primitives.Doubles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Task implements Comparable<Task> {

	protected final double distance;
	protected final AtomicBoolean cancelled = new AtomicBoolean(false);
	protected final boolean highPriority;

	public Task(double distance, boolean highPriority) {
		this.distance = distance;
		this.highPriority = highPriority;
	}

	public abstract CompletableFuture<Result> run(SpecialBufferBuilderStorage buffers);

	public abstract void cancel();

	protected abstract String name();

	public int compareTo(Task task) {
		return Doubles.compare(this.distance, task.distance);
	}

}
