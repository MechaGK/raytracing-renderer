package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Transform {
    // Local to world transformation matrix
    private double
            twA1, twB1, twC1, //PosX,
            twA2, twB2, twC2, //PosY,
            twA3, twB3, twC3; //PosZ;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    // World to local transformation matrix
    private double        
            tlA1, tlB1, tlC1, //invPosX,
            tlA2, tlB2, tlC2, //invPosY,
            tlA3, tlB3, tlC3; //invPosZ;
    //      0   , 0   , 0   , 1   <- (implicit)
    
    private double
            rA1, rA2, rA3,
            rB1, rB2, rB3,
            rC1, rC2, rC3;
    
    private Vector3D invTranslation;
    
    public Vector3D position;
    public Vector3D eulerAngles;
    public Vector3D scale;
    
    public Transform(Vector3D position, Vector3D scale, Vector3D eulerAngles) {
        set(position, scale, eulerAngles);
    }
    
    public void set(Vector3D newPosition, Vector3D newScale, Vector3D newEulerAngles) {
        this.position = newPosition;
        this.scale = newScale;
        this.eulerAngles = newEulerAngles;
        
        double scaleX = newScale.getX(), invScaleX = 1/scaleX;
        double scaleY = newScale.getY(), invScaleY = 1/scaleY;
        double scaleZ = newScale.getZ(), invScaleZ = 1/scaleZ;
        
        double roll = newEulerAngles.getX();
        double pitch = newEulerAngles.getY();
        double yaw = newEulerAngles.getZ();
        
        double cosRoll = Math.cos(roll);
        double sinRoll = Math.sin(roll);
        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        
        rA1 = cosYaw * cosPitch;
        rA2 = sinYaw * cosPitch;
        rA3 = -sinPitch;
        
        rB1 = -sinYaw * cosRoll + cosYaw * sinPitch * sinRoll;
        rB2 = cosYaw * cosRoll + sinYaw * sinPitch * sinRoll;
        rB3 = cosPitch * sinRoll;
        
        rC1 = sinYaw * sinRoll + cosYaw * sinPitch * cosRoll;
        rC2 = -cosYaw * sinRoll + sinYaw * sinPitch * cosRoll;
        rC3 = cosPitch * cosRoll;
        
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
        
        setPosition(newPosition);
    }
    
    public void setPosition(Vector3D newPosition) {
        this.position = newPosition;
        this.invTranslation = vectorToLocal(position.negate());
    }
    
    public void setScale(Vector3D newScale) {
        set(position, newScale, eulerAngles);
    }
    
    public void setRotation(Vector3D newEuelerAngles) {
        set(position, scale, newEuelerAngles);
    }

    public Vector3D pointToWorld(Vector3D localPoint) {
        double px = localPoint.getX();
        double py = localPoint.getY();
        double pz = localPoint.getZ();
        
        return new Vector3D(
                px*twA1 + py*twB1 + pz*twC1 + position.getX(),
                px*twA2 + py*twB2 + pz*twC2 + position.getY(),
                px*twA3 + py*twB3 + pz*twC3 + position.getZ()
        );
    }
    
    public Vector3D pointToLocal(Vector3D worldPoint) {
        double px = worldPoint.getX();
        double py = worldPoint.getY();
        double pz = worldPoint.getZ();
        
        return new Vector3D(
                px*tlA1 + py*tlB1 + pz*tlC1 + invTranslation.getX(),
                px*tlA2 + py*tlB2 + pz*tlC2 + invTranslation.getY(),
                px*tlA3 + py*tlB3 + pz*tlC3 + invTranslation.getZ()
        );
    };
    
    public Vector3D vectorToWorld(Vector3D localVector) {
        double vx = localVector.getX();
        double vy = localVector.getY();
        double vz = localVector.getZ();
        
        return new Vector3D(
                vx*twA1 + vy*twB1 + vz*twC1,
                vx*twA2 + vy*twB2 + vz*twC2,
                vx*twA3 + vy*twB3 + vz*twC3
        );
    }

    public Vector3D vectorToLocal(Vector3D worldVector) {
        double vx = worldVector.getX();
        double vy = worldVector.getY();
        double vz = worldVector.getZ();
        
        return new Vector3D(
                vx*tlA1 + vy*tlB1 + vz*tlC1,
                vx*tlA2 + vy*tlB2 + vz*tlC2,
                vx*tlA3 + vy*tlB3 + vz*tlC3
        );
    };
    
    public Vector3D directionToWorld(Vector3D localDirection) {
        double dx = localDirection.getX();
        double dy = localDirection.getY();
        double dz = localDirection.getZ();
        
        return new Vector3D(
                dx*rA1 + dy*rB1 + dz*rC1,
                dx*rA2 + dy*rB2 + dz*rC2,
                dx*rA3 + dy*rB3 + dz*rC3
        );
    }
    
    public Vector3D directionToLocal(Vector3D worldDirection) {
        double dx = worldDirection.getX();
        double dy = worldDirection.getY();
        double dz = worldDirection.getZ();
        
        return new Vector3D(
                dx*rA1 + dy*rA2 + dz*rA3,
                dx*rB1 + dy*rB2 + dz*rB3,
                dx*rC1 + dy*rC2 + dz*rC3
        );
    };
}