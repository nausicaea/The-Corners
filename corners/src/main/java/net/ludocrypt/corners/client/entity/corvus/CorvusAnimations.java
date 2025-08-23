package net.ludocrypt.corners.client.entity.corvus;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class CorvusAnimations {

	public static final Keyframe ROTATE_ORIGIN = new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F),
		Transformation.Interpolations.CUBIC);

	public static Animation.Builder tuckWings(Animation.Builder curr, Keyframe leftWing,
			Keyframe leftWing2, Keyframe leftWingMid, Keyframe rightWing,
			Keyframe rightWing2, Keyframe rightWingMid, float time) {
		return curr
			.addBoneAnimation("left_wing",
				new Transformation(Transformation.Targets.ROTATE, leftWing,
					new Keyframe(time, AnimationHelper.createRotationalVector(0.0F, 0.0F, 90.0F), Transformation.Interpolations.CUBIC)))
			.addBoneAnimation("left_wing_2",
				new Transformation(Transformation.Targets.ROTATE, leftWing2,
					new Keyframe(time, AnimationHelper.createRotationalVector(20.0F, -90.0F, -30.0F),
						Transformation.Interpolations.CUBIC)))
			.addBoneAnimation("left_wing_mid_r1",
				new Transformation(Transformation.Targets.ROTATE, leftWingMid,
					new Keyframe(time, AnimationHelper.createRotationalVector(0.0F, 90.0F, 0.0F), Transformation.Interpolations.CUBIC)))
			.addBoneAnimation("right_wing",
				new Transformation(Transformation.Targets.ROTATE, rightWing,
					new Keyframe(time, AnimationHelper.createRotationalVector(0.0F, 0.0F, -90.0F), Transformation.Interpolations.CUBIC)))
			.addBoneAnimation("right_wing_2",
				new Transformation(Transformation.Targets.ROTATE, rightWing2,
					new Keyframe(time, AnimationHelper.createRotationalVector(20.0F, 90.0F, 30.0F), Transformation.Interpolations.CUBIC)))
			.addBoneAnimation("right_wing_mid_r1", new Transformation(Transformation.Targets.ROTATE, rightWingMid,
				new Keyframe(time, AnimationHelper.createRotationalVector(0.0F, -90.0F, 0.0F), Transformation.Interpolations.CUBIC)));
	}

}
