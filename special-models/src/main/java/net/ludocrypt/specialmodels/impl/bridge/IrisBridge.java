package net.ludocrypt.specialmodels.impl.bridge;

import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.specialmodels.impl.SpecialModels;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class IrisBridge {

	public static final boolean IRIS_LOADED = FabricLoader.getInstance().isModLoaded("iris");

	public static boolean areShadersInUse() {

		if (IRIS_LOADED) {

			try {
				Class<?> irisApi = Class.forName("net.coderbot.iris.apiimpl.IrisApiV0Impl");
				Field irisInstance = irisApi.getField("INSTANCE");
				Method isShaderInUse = irisApi.getMethod("isShaderPackInUse", new Class[0]);
				Object areThey = isShaderInUse.invoke(irisInstance.get(null), new Object[0]);

				if (areThey instanceof Boolean depends) {
					return depends;
				}

			} catch (Exception e) {
				SpecialModels.LOGGER.error("Could not determine if Iris shaders is used", e);
				return false;
			}

		}

		return false;
	}

}
