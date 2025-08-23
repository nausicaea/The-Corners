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

	SpecialChunkBuilder specialmodels$getSpecialChunkBuilder();

	Future<?> specialmodels$getLastFullSpecialBuiltChunkUpdate();

	BlockingQueue<SpecialChunkBuilder.BuiltChunk> specialmodels$getRecentlyCompiledSpecialChunks();

	AtomicReference<SpecialChunkBuilder.RenderableChunks> specialmodels$getRenderableSpecialChunks();

	ObjectArrayList<SpecialChunkBuilder.ChunkInfo> specialmodels$getSpecialChunkInfoList();

	SpecialBuiltChunkStorage specialmodels$getSpecialChunks();

	SpecialBufferBuilderStorage specialmodels$getSpecialBufferBuilderStorage();

	boolean specialmodels$shouldNeedsFullSpecialBuiltChunkUpdate();

	AtomicBoolean specialmodels$shouldNeedsSpecialFrustumUpdate();

	AtomicLong specialmodels$getNextFullSpecialUpdateMilliseconds();

	void specialmodels$setWorldSpecial(ClientWorld world);

	void specialmodels$reloadSpecial();

	void specialmodels$setupSpecialTerrain(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator);

	void specialmodels$addSpecialChunksToBuild(Camera camera, Queue<SpecialChunkBuilder.ChunkInfo> chunkInfoQueue);

	void specialmodels$addSpecialBuiltChunk(SpecialChunkBuilder.BuiltChunk builtChunk);

	void specialmodels$updateSpecialBuiltChunks(LinkedHashSet<SpecialChunkBuilder.ChunkInfo> builtChunks,
												SpecialChunkBuilder.ChunkInfoListMap builtChunkMap, Vec3d cameraPos,
												Queue<SpecialChunkBuilder.ChunkInfo> chunksToBuild, boolean chunkCullingEnabled);

	@Nullable
	SpecialChunkBuilder.BuiltChunk specialmodels$getAdjacentSpecialChunk(BlockPos pos, SpecialChunkBuilder.BuiltChunk chunk,
																		 Direction direction);

	boolean specialmodels$isSpecialChunkNearMaxViewDistance(BlockPos blockPos, SpecialChunkBuilder.BuiltChunk builtChunk);

	void specialmodels$applySpecialFrustum(Frustum frustum);

	void specialmodels$findSpecialChunksToRebuild(Camera camera);

}
