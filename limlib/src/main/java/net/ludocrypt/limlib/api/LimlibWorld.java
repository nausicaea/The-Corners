package net.ludocrypt.limlib.api;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public record LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier,
						  Function<LimlibRegistryProvider, DimensionOptions> dimensionOptionsSupplier) {
	public LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier,
					   Function<LimlibRegistryProvider, DimensionOptions> dimensionOptionsSupplier) {
		this.dimensionTypeSupplier = Suppliers.memoize(dimensionTypeSupplier);
		this.dimensionOptionsSupplier = dimensionOptionsSupplier;
	}
}
