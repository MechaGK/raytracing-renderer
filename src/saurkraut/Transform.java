package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Transform {
    // Local to world transformation matrix
    private final double
            twA1, twB1, twC1, //PosX,
            twA2, twB2, twC2, //PosY,
            twA3, twB3, twC3; //PosZ;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    // World to local transformation matrix
    private final double        
            tlA1, tlB1, tlC1, //PosX,
            tlA2, tlB2, tlC2, //PosY,
            tlA3, tlB3, tlC3; //PosZ;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    public final Vector3D position;
    private final Vector3D invTranslation;
    public final Vector3D eulerRotation;

    public Transform(Vector3D position, Vector3D scale, Vector3D eulerRotation) {    
        this.position = position;
        this.eulerRotation = eulerRotation;
        
        double scaleX = scale.getX(), invScaleX = 1/scaleX;
        double scaleY = scale.getY(), invScaleY = 1/scaleY;
        double scaleZ = scale.getZ(), invScaleZ = 1/scaleZ;
        
        double roll = eulerRotation.getX();
        double pitch = eulerRotation.getY();
        double yaw = eulerRotation.getZ();
        
        double cosRoll = Math.cos(roll);
        double sinRoll = Math.sin(roll);
        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        
        double rA1 = cosYaw * cosPitch;
        double rA2 = sinYaw * cosPitch;
        double rA3 = -sinPitch;
        
        double rB1 = -sinYaw * cosRoll + cosYaw * sinPitch * sinRoll;
        double rB2 = cosYaw * cosRoll + sinYaw * sinPitch * sinRoll;
        double rB3 = cosPitch * sinRoll;
        
        double rC1 = sinYaw * sinRoll + cosYaw * sinPitch * cosRoll;
        double rC2 = -cosYaw * sinRoll + sinYaw * sinPitch * cosRoll;
        double rC3 = cosPitch * cosRoll;
        
        // LOCAL TO WORLD SPACE TRANSFORM
        // [translation].[rotation].[scale]
        // Applies scale, then rotation, then translation
        
        // Column A
        twA1 = scaleX*rA1;
        twA2 = scaleX*rA2;
        twA3 = scaleX*rA3;
        
        // Column B
        twB1 = scaleY*rB1;
        twB2 = scaleY*rB2;
        twB3 = scaleY*rB3;
        // Column C
        twC1 = scaleZ*rC1;
        twC2 = scaleZ*rC2;
        twC3 = scaleZ*rC3;

        // WORLD TO LOCAL SPACE TRANSFORM
        // The inverse
        // [inverse scale].[transpose rotation].[inverse translation]
        // Applies inverse translation, then transposed rotation, then inverse scale
        // Column A
        tlA1 = invScaleX*rA1;
        tlA2 = invScaleY*rB1;
        tlA3 = invScaleZ*rC1;
        // Column B        
        tlB1 = invScaleX*rA2;
        tlB2 = invScaleY*rB2;
        tlB3 = invScaleZ*rC2;
        // Column C        
        tlC1 = invScaleX*rA3;
        tlC2 = invScaleY*rB3;
        tlC3 = invScaleZ*rC3;
        
        invTranslation = new Vector3D(
            -tlA1*position.getX() - tlB1*position.getY() - tlC1*position.getZ(),
            -tlA2*position.getX() - tlB2*position.getY() - tlC2*position.getZ(),
            -tlA3*position.getX() - tlB3*position.getY() - tlC3*position.getZ());
    }

    public Vector3D pointToWorld(Vector3D localPoint) {
        double lx = localPoint.getX();
        double ly = localPoint.getY();
        double lz = localPoint.getZ();
        
        return new Vector3D(
                lx*twA1 + ly*twB1 + lz*twC1 + position.getX(),
                lx*twA2 + ly*twB2 + lz*twC2 + position.getY(),
                lx*twA3 + ly*twB3 + lz*twC3 + position.getZ()
        );
    }
    
    public Vector3D pointToLocal(Vector3D worldPoint) {
        double wx = worldPoint.getX();
        double wy = worldPoint.getY();
        double wz = worldPoint.getZ();
        
        return new Vector3D(
                wx*tlA1 + wy*tlB1 + wz*tlC1 + invTranslation.getX(),
                wx*tlA2 + wy*tlB2 + wz*tlC2 + invTranslation.getY(),
                wx*tlA3 + wy*tlB3 + wz*tlC3 + invTranslation.getZ()
        );
    };
    
    public Vector3D vectorToWorld(Vector3D localDirection) {
        double lx = localDirection.getX();
        double ly = localDirection.getY();
        double lz = localDirection.getZ();
        
        return new Vector3D(
                lx*twA1 + ly*twB1 + lz*twC1,
                lx*twA2 + ly*twB2 + lz*twC2,
                lx*twA3 + ly*twB3 + lz*twC3
        );
    }

    public Vector3D vectorToLocal(Vector3D worldPoint) {
        double wx = worldPoint.getX();
        double wy = worldPoint.getY();
        double wz = worldPoint.getZ();
        
        return new Vector3D(
                wx*tlA1 + wy*tlB1 + wz*tlC1,
                wx*tlA2 + wy*tlB2 + wz*tlC2,
                wx*tlA3 + wy*tlB3 + wz*tlC3
        );
    };
    
    public Ray rayToLocal(Ray worldRay) {
        return new Ray(pointToLocal(worldRay.origin), vectorToLocal(worldRay.direction));
    }
    
    public Ray rayToWorld(Ray localRay) {
        return new Ray(pointToWorld(localRay.origin), vectorToWorld(localRay.direction));
    }
}