package saurkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Abstract class for a shape
 */
public abstract class SceneObject {
    // Local to world transformation matrix
    private final double
            LW00, LW01, LW02, LW03,
            LW10, LW11, LW12, LW13,
            LW20, LW21, LW22, LW23;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    // World to local transformation matrix
    private final double        
            WL00, WL01, WL02, WL03,
            WL10, WL11, WL12, WL13,
            WL20, WL21, WL22, WL23;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    private final double r00, r01, r02, r10, r11, r12, r20, r21, r22;
    public final Vector3D position;

    public SceneObject(Vector3D position, Vector3D scale, Vector3D eulerAngles) {    
        this.position = position;
        
        double scaleX = scale.getX(), invScaleX = 1/scaleX;
        double scaleY = scale.getY(), invScaleY = 1/scaleY;
        double scaleZ = scale.getZ(), invScaleZ = 1/scaleZ;
        
        double angX = eulerAngles.getX();
        double angY = eulerAngles.getX();
        double angZ = eulerAngles.getX();
        
        double cX = Math.cos(angX);
        double sX = Math.sin(angX);
        double cY = Math.cos(angY);
        double sY = Math.sin(angY);
        double cZ = Math.cos(angZ);
        double sZ = Math.sin(angZ);
        
        r00 = cZ*cY;    r01 = cZ*sX*sY-cX*sZ;   r02 = cX*cZ*sY+sX*sZ;
        r10 = sZ*cY;    r11 = sX*sY*sZ+cX*cZ;   r12 = cX*sY*sZ-cZ*sX;
        r20 = -sY;      r21 = cY*sX;            r22 = cY*cX;

        // LOCAL TO WORLD SPACE TRANSFORM
        // [translation].[rotation].[scale]
        // Applies scale, then rotation, then translation
        LW00 = scaleX*r00; LW01 = scaleY*r01; LW02 = scaleZ*r02; LW03 = position.getX();
        LW10 = scaleX*r10; LW11 = scaleY*r11; LW12 = scaleZ*r12; LW13 = position.getY();
        LW20 = scaleX*r20; LW21 = scaleY*r21; LW22 = scaleZ*r22; LW23 = position.getZ();

        // WORLD TO LOCAL SPACE TRANSFORM
        // The inverse
        // [inverse scale].[transpose rotation].[inverse translation]
        // Applies inverse translation, then transposed rotation, then inverse scale
        WL00 = invScaleX*r00; WL01 = invScaleX*r10; WL02 = invScaleX*r20; WL03 = -WL00*LW03 - WL01*LW13 - WL02*LW23;
        WL10 = invScaleY*r01; WL11 = invScaleY*r11; WL12 = invScaleY*r21; WL13 = -WL10*LW03 - WL11*LW13 - WL12*LW23;
        WL20 = invScaleZ*r02; WL21 = invScaleZ*r12; WL22 = invScaleZ*r22; WL23 = -WL20*LW03 - WL21*LW13 - WL22*LW23;
    }

    public Vector3D pointToWorld(Vector3D localPoint) {
        double lx = localPoint.getX();
        double ly = localPoint.getY();
        double lz = localPoint.getZ();
        
        return new Vector3D(
                lx*LW00 + ly*LW01 + lz*LW02 + LW03,
                lx*LW10 + ly*LW11 + lz*LW12 + LW13,
                lx*LW20 + ly*LW21 + lz*LW22 + LW23
        );
    }
    
    public Vector3D pointToLocal(Vector3D worldPoint) {
        double wx = worldPoint.getX();
        double wy = worldPoint.getY();
        double wz = worldPoint.getZ();
        
        return new Vector3D(
                wx*WL00 + wy*WL01 + wz*WL02 + WL03,
                wx*WL10 + wy*WL11 + wz*WL12 + WL13,
                wx*WL20 + wy*WL21 + wz*WL22 + WL23
        );
    };
    
    public Vector3D directionToWorld(Vector3D localDirection) {
        double lx = localDirection.getX();
        double ly = localDirection.getY();
        double lz = localDirection.getZ();
        
        return new Vector3D(
                lx*r00 + ly*r01 + lz*r02,
                lx*r10 + ly*r11 + lz*r12,
                lx*r20 + ly*r21 + lz*r22
        );
    }
    
    public Vector3D directionToLocal(Vector3D worldPoint) {
        double wx = worldPoint.getX();
        double wy = worldPoint.getY();
        double wz = worldPoint.getZ();
        
        return new Vector3D(
                wx*r00 + wy*r10 + wz*r20,
                wx*r01 + wy*r11 + wz*r21,
                wx*r02 + wy*r12 + wz*r22
        );
    };
}