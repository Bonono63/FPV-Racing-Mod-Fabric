package io.lazurite.fpvracing.physics.thrust;

import io.lazurite.fpvracing.client.input.InputTick;
import io.lazurite.fpvracing.server.entity.flyable.QuadcopterEntity;
import io.lazurite.fpvracing.util.Matrix4fInject;
import io.lazurite.fpvracing.util.math.BetaflightHelper;
import io.lazurite.fpvracing.util.math.QuaternionHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector3f;

public class QuadcopterThrust implements Thrust {
    private QuadcopterEntity quad;

    public QuadcopterThrust(QuadcopterEntity quad) {
        this.quad = quad;
    }

    /**
     * Calculates the amount of force thrust should produce based on throttle and yaw input.
     * @return a {@link Vector3f} containing the direction and amount of force (in newtons)
     */
    public Vector3f getForce() {
        Vector3f thrustVec = getVector();
        thrustVec.scale(calculateCurve() * quad.getValue(QuadcopterEntity.THRUST));

        Vector3f yawVec = getVector();
        yawVec.scale((float) BetaflightHelper.calculateRates(InputTick.axisValues.currY, quad.getValue(QuadcopterEntity.RATE), quad.getValue(QuadcopterEntity.EXPO), quad.getValue(QuadcopterEntity.SUPER_RATE), 0.01f));

        Vector3f out = new Vector3f();
        out.add(thrustVec, yawVec);
        return out;
    }

    /**
     * Get the direction the bottom of the quad is facing.
     * @return {@link Vec3d} containing thrust direction
     */
    public Vector3f getVector() {
        QuaternionHelper.rotateX(quad.getPhysics().getOrientation(), 90);
        Matrix4f mat = new Matrix4f();
        Matrix4fInject.from(mat).fromQuaternion(QuaternionHelper.quat4fToQuaternion(quad.getPhysics().getOrientation()));

        Vector3f out = Matrix4fInject.from(mat).matrixToVector();
        out.scale(-1);
        return out;
    }

    /**
     * Calculates the thrust curve using a power between zero and one (one being perfectly linear).
     * @return a point on the thrust curve
     */
    public float calculateCurve() {
        return (float) (Math.pow(InputTick.axisValues.currT, quad.getValue(QuadcopterEntity.THRUST_CURVE)));
    }

}
