package net.ludocrypt.specialmodels.client.impl.access;

import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.ludocrypt.specialmodels.client.impl.chunk.SpecialBufferBuilderStorage;
import net.ludocrypt.specialmodels.client.impl.chunk.SpecialBuiltChunkStorage;
import net.ludocrypt.specialmodels.client.impl.chunk.SpecialChunkBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public interface WorldChunkBuilderAccess {

	SpecialChunkBuilder getSpecialChunkBuilder();

	Future<?> getLastFullSpecialBuiltChunkUpdate();

	BlockingQueue<SpecialChunkBuilder.BuiltChunk> getRecentlyCompiledSpecialChunks();

	AtomicReference<SpecialChunkBuilder.RenderableChunks> getRenderableSpecialChunks();

	ObjectArrayList<SpecialChunkBuilder.ChunkInfo> getSpecialChunkInfoList();

	SpecialBuiltChunkStorage getSpecialChunks();

	SpecialBufferBuilderStorage getSpecialBufferBuilderStorage();

	boolean shouldNeedsFullSpecialBuiltChunkUpdate();

	AtomicBoolean shouldNeedsSpecialFrustumUpdate();

	AtomicLong getNextFullSpecialUpdateMilliseconds();

	void setWorldSpecial(ClientWorld world);

	void reloadSpecial();

	void setupSpecialTerrain(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator);

	void addSpecialChunksToBuild(Camera camera, Queue<SpecialChunkBuilder.ChunkInfo> chunkInfoQueue);

	void addSpecialBuiltChunk(SpecialChunkBuilder.BuiltChunk builtChunk);

	void updateSpecialBuiltChunks(LinkedHashSet<SpecialChunkBuilder.ChunkInfo> builtChunks,
								  SpecialChunkBuilder.ChunkInfoListMap builtChunkMap, Vec3d cameraPos,
								  Queue<SpecialChunkBuilder.ChunkInfo> chunksToBuild, boolean chunkCullingEnabled);

	@Nullable
	SpecialChunkBuilder.BuiltChunk getAdjacentSpecialChunk(BlockPos pos, SpecialChunkBuilder.BuiltChunk chunk,
														   Direction direction);

	boolean isSpecialChunkNearMaxViewDistance(BlockPos blockPos, SpecialChunkBuilder.BuiltChunk builtChunk);

	void applySpecialFrustum(Frustum frustum);

	void findSpecialChunksToRebuild(Camera camera);

}
