package com.csc205.project1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a cube in 3D space.
 *
 * This class demonstrates object composition using Point3D and Line3D, and
 * encapsulates cube geometry with immutable state.
 */
public class Cube3D {

    private static final Logger logger = Logger.getLogger(Cube3D.class.getName());
    private static final double EPSILON = 1e-8;

    private final List<Point3D> vertices;
    private final double edgeLength;

    /**
     * Constructs an axis-aligned cube centered at the given point.
     *
     * @param center the cube center point
     * @param edgeLength the length of each edge
     */
    public Cube3D(Point3D center, double edgeLength) {
        if (center == null) {
            logger.log(Level.SEVERE, "Cube center cannot be null");
            throw new IllegalArgumentException("Center point cannot be null");
        }
        if (edgeLength <= 0) {
            logger.log(Level.SEVERE, "Cube edge length must be positive: {0}", edgeLength);
            throw new IllegalArgumentException("Edge length must be positive");
        }
        this.edgeLength = edgeLength;
        this.vertices = Collections.unmodifiableList(buildAxisAlignedVertices(center, edgeLength));
        logger.log(Level.INFO, "Created Cube3D at center {0} with edge length {1}", new Object[]{center, edgeLength});
    }

    private Cube3D(List<Point3D> vertices, double edgeLength) {
        this.edgeLength = edgeLength;
        this.vertices = Collections.unmodifiableList(new ArrayList<>(vertices));
    }

    private static List<Point3D> buildAxisAlignedVertices(Point3D center, double edgeLength) {
        double half = edgeLength / 2.0;
        List<Point3D> result = new ArrayList<>();
        for (int xSign = -1; xSign <= 1; xSign += 2) {
            for (int ySign = -1; ySign <= 1; ySign += 2) {
                for (int zSign = -1; zSign <= 1; zSign += 2) {
                    result.add(center.translate(xSign * half, ySign * half, zSign * half));
                }
            }
        }
        return result;
    }

    public List<Point3D> getVertices() {
        return vertices;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    public Point3D getCenter() {
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;
        for (Point3D vertex : vertices) {
            sumX += vertex.getX();
            sumY += vertex.getY();
            sumZ += vertex.getZ();
        }
        int size = vertices.size();
        return new Point3D(sumX / size, sumY / size, sumZ / size);
    }

    /**
     * Returns the twelve edges of the cube.
     *
     * @return an unmodifiable list of edges composing the cube
     */
    public List<Line3D> getEdges() {
        List<Line3D> edges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Point3D a = vertices.get(i);
                Point3D b = vertices.get(j);
                int diffCount = 0;
                if (Math.abs(a.getX() - b.getX()) > EPSILON) diffCount++;
                if (Math.abs(a.getY() - b.getY()) > EPSILON) diffCount++;
                if (Math.abs(a.getZ() - b.getZ()) > EPSILON) diffCount++;
                if (diffCount == 1) {
                    edges.add(new Line3D(a, b));
                }
            }
        }
        return Collections.unmodifiableList(edges);
    }

    public double volume() {
        double volume = edgeLength * edgeLength * edgeLength;
        logger.log(Level.INFO, "Cube volume = {0}", volume);
        return volume;
    }

    public double surfaceArea() {
        double surfaceArea = 6 * edgeLength * edgeLength;
        logger.log(Level.INFO, "Cube surface area = {0}", surfaceArea);
        return surfaceArea;
    }

    public double perimeterLength() {
        double perimeter = 12 * edgeLength;
        logger.log(Level.INFO, "Cube perimeter length = {0}", perimeter);
        return perimeter;
    }

    public Cube3D translate(double dx, double dy, double dz) {
        List<Point3D> translated = new ArrayList<>();
        for (Point3D vertex : vertices) {
            translated.add(vertex.translate(dx, dy, dz));
        }
        logger.log(Level.INFO, "Translated cube by ({0}, {1}, {2})", new Object[]{dx, dy, dz});
        return new Cube3D(translated, edgeLength);
    }

    private List<Point3D> rotateVertices(double angleRadians, RotationAxis axis) {
        List<Point3D> rotated = new ArrayList<>();
        Point3D center = getCenter();
        for (Point3D vertex : vertices) {
            Point3D relative = vertex.subtract(center);
            Point3D rotatedRelative;
            if (axis == RotationAxis.X) {
                rotatedRelative = relative.rotateX(angleRadians);
            } else if (axis == RotationAxis.Y) {
                rotatedRelative = relative.rotateY(angleRadians);
            } else {
                rotatedRelative = relative.rotateZ(angleRadians);
            }
            rotated.add(rotatedRelative.translate(center.getX(), center.getY(), center.getZ()));
        }
        return rotated;
    }

    public Cube3D rotateX(double angleRadians) {
        List<Point3D> rotated = rotateVertices(angleRadians, RotationAxis.X);
        logger.log(Level.INFO, "Rotated cube around X-axis by {0} radians", angleRadians);
        return new Cube3D(rotated, edgeLength);
    }

    public Cube3D rotateY(double angleRadians) {
        List<Point3D> rotated = rotateVertices(angleRadians, RotationAxis.Y);
        logger.log(Level.INFO, "Rotated cube around Y-axis by {0} radians", angleRadians);
        return new Cube3D(rotated, edgeLength);
    }

    public Cube3D rotateZ(double angleRadians) {
        List<Point3D> rotated = rotateVertices(angleRadians, RotationAxis.Z);
        logger.log(Level.INFO, "Rotated cube around Z-axis by {0} radians", angleRadians);
        return new Cube3D(rotated, edgeLength);
    }

    public boolean isValidCube() {
        if (vertices.size() != 8) {
            logger.log(Level.WARNING, "Invalid cube: expected 8 vertices but found {0}", vertices.size());
            return false;
        }
        if (Math.abs(volume() - Math.pow(edgeLength, 3)) > EPSILON) {
            logger.log(Level.WARNING, "Invalid cube: inconsistent edge length calculations");
            return false;
        }

        int edgeCount = 0;
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                double distance = vertices.get(i).distanceTo(vertices.get(j));
                if (Math.abs(distance - edgeLength) < 1e-6) {
                    edgeCount++;
                }
            }
        }

        boolean valid = edgeCount == 12;
        logger.log(Level.INFO, "Cube validation: edgeCount = {0}, valid = {1}", new Object[]{edgeCount, valid});
        return valid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cube3D other = (Cube3D) obj;
        return Math.abs(this.edgeLength - other.edgeLength) < EPSILON && vertices.equals(other.vertices);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(vertices, edgeLength);
    }

    @Override
    public String toString() {
        return String.format("Cube3D(edgeLength=%.2f, center=%s)", edgeLength, getCenter());
    }

    private enum RotationAxis {
        X, Y, Z
    }
}
