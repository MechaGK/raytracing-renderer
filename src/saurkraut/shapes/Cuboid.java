package saurkraut.shapes;

import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import saurkraut.Material;
import saurkraut.Ray;


public class Cuboid extends Shape {

    private final Vector3D dimensions;

    public Cuboid(Material material, Vector3D position, Vector3D dimensions /* , Vector3D eulerAngles */) {
        super(material, position);
        this.dimensions = dimensions;
    }

    @Override
    public Vector3D intersect(Ray ray) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector3D getNormal(Vector3D point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color getColor(Vector3D point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
