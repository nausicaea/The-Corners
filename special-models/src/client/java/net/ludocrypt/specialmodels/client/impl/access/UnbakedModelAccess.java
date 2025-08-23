package net.ludocrypt.specialmodels.client.impl.access;

import java.util.Map;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.util.Identifier;

public interface UnbakedModelAccess {

	public Map<SpecialModelRenderer, Identifier> specialmodels$getSubModels();

}
