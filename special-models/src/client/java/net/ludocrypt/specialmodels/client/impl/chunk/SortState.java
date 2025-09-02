package net.ludocrypt.specialmodels.client.impl.chunk;

import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public record SortState(VertexFormat.DrawMode drawMode, int vertexCount, @Nullable Vector3f[] sortingPoints,
						@Nullable VertexSorter quadSorting) { }
