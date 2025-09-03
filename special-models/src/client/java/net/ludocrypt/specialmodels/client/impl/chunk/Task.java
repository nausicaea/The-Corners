package net.ludocrypt.specialmodels.client.impl.chunk;

import com.google.common.primitives.Doubles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public interface Task extends Comparable<Task> {

	double getDistance();

	AtomicBoolean getCancelled();

	boolean isHighPriority();

	CompletableFuture<Result> run(SpecialBufferBuilderStorage buffers);

	void cancel();

	String name();

	default int compareTo(Task task) {
		return Doubles.compare(this.getDistance(), task.getDistance());
	}

}
