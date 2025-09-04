package net.ludocrypt.limlib.api.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public record FunctionMap<K, V, A>(Optional<BiFunction<K, A, V>> defaultMapper, Map<K, Function<A, V>> functionMap, Map<K, V> cache) {

	public FunctionMap(BiFunction<K, A, V> defaultMapper) {
		this(Optional.of(defaultMapper), new HashMap<>(), new HashMap<>());
	}

	public FunctionMap() {
		this(Optional.empty(), new HashMap<>(), new HashMap<>());
	}

	public Function<A, V> put(K key, Function<A, V> compute) {
		return this.functionMap.put(key, compute);
	}

	public Function<A, V> put(K key) {

		if (this.defaultMapper.isEmpty()) {
			throw new UnsupportedOperationException("No default mapper is supplied");
		}

		return this.functionMap.put(key, (arg) -> this.defaultMapper.get().apply(key, arg));
	}

	public V eval(K key, A arg) {

		if (this.functionMap.containsKey(key)) {
			return this.cache.computeIfAbsent(key, (k) -> this.functionMap.get(k).apply(arg));
		} else {
			throw new NullPointerException("functionMap %s does not contain key %s".formatted(functionMap, key));
		}

	}

	public Map<K, V> getCache() {
		return this.cache;
	}

	public Map<K, Function<A, V>> getFunctionMap() {
		return this.functionMap;
	}

}
