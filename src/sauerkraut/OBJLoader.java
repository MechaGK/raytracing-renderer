package sauerkraut;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class OBJLoader extends ArrayList<ArrayList> {
    public OBJLoader(File f) throws FileNotFoundException {
        super(4);
        for (int i = 0; i < 4; i++) {
            super.add(new ArrayList());
        }
        Scanner sc = new Scanner(f);
        while (sc.hasNext()) {
            switch (sc.next()) {
                case "v":
                    this.vertex().add(this.vector(sc.nextLine(), Vector3D::new));
                    break;
                case "vt":
                    this.texcoord().add(this.vector(sc.nextLine(), Vector2D::new));
                    break;
                case "vn":
                    this.normal().add(this.vector(sc.nextLine(), Vector3D::new));
                    break;
                case "f":
                    this.face().addAll(this.readFace(sc.nextLine()));
                    break;
            }
        }
    }

    static private <T> List<T> readVec(final String s, Function<String, T> f) {
        return Arrays.stream(s.split(" ")).filter(i -> !i.trim().isEmpty()).map(f).collect(Collectors.toList());
    }

    static private <T> T vector(final String s, Function<double[], T> c) {
        return c.apply(readVec(s, Double::new).stream().mapToDouble(i -> i).toArray());
    }

    static private List<Integer> readFace(String s) {
        Scanner sc = new Scanner(s);
        List<Integer> face = new ArrayList<>();
        while (sc.hasNext()) face.add(sc.nextInt() - 1);
        return face;
    }

    public List<Vector3D> vertex() {
        return super.get(0);
    }

    public List<Vector2D> texcoord() {
        return super.get(1);
    }

    public List<Vector3D> normal() {
        return super.get(2);
    }

    public List<Integer> face() {
        return super.get(3);
    }

    public static void main(String[] args) {
        try {
            OBJLoader obj = new OBJLoader(new File(args[0]));
            System.out.println(obj.face());
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
