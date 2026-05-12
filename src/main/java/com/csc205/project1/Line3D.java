package com.csc205.project1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a line segment in 3D space defined by two endpoints.
 *
 * This class uses Point3D objects for its endpoints and demonstrates the
 * Composition design pattern by building more complex geometry from simpler
 * immutable point objects.
 */
public class Line3D {

    private static final Logger logger = Logger.getLogger(Line3D.class.getName());

    private final Point3D start;
    private final Point3D end;

    /**
     * Constructs a new Line3D with the specified start and end points.
     *
     * @param start the start point of the line segment
     * @param end the end point of the line segment
     * @throws IllegalArgumentException if either point is null or the points are identical
     */
    public Line3D(Point3D start, Point3D end) {
        if (start == null || end == null) {
            logger.log(Level.SEVERE, "Cannot create Line3D with null endpoints");
            throw new IllegalArgumentException("Line endpoints must not be null");
        }
        if (start.equals(end)) {
            logger.log(Level.SEVERE, "Cannot create Line3D with zero length: start and end are identical");
            throw new IllegalArgumentException("Line endpoints must be distinct points");
        }
        this.start = start;
        this.end = end;
        logger.log(Level.INFO, "Created Line3D from {0} to {1}", new Object[]{start, end});
    }

    public Point3D getStart() {
        return start;
    }

    public Point3D getEnd() {
        return end;
    }

    /**
     * Returns the length of the line segment.
     *
     * @return the Euclidean distance between the start and end points
     */
    public double length() {
        double length = start.distanceTo(end);
        logger.log(Level.INFO, "Line length from {0} to {1} = {2}", new Object[]{start, end, length});
        return length;
    }

    /**
     * Returns the midpoint of the line segment.
     *
     * @return the midpoint between start and end
     */
    public Point3D midpoint() {
        Point3D midpoint = start.midpoint(end);
        logger.log(Level.INFO, "Midpoint of line from {0} to {1} is {2}", new Object[]{start, end, midpoint});
        return midpoint;
    }

    /**
     * Returns the direction vector from the start to the end point.
     *
     * @return a normalized direction vector for this line segment
     */
    public Point3D direction() {
        Point3D delta = end.subtract(start);
        Point3D normalized = delta.normalize();
        if (normalized == null) {
            logger.log(Level.SEVERE, "Unable to compute direction for zero-length line segment");
            throw new IllegalStateException("Cannot compute direction for zero-length line segment");
        }
        logger.log(Level.INFO, "Direction of Line3D from {0} to {1} is {2}", new Object[]{start, end, normalized});
        return normalized;
    }

    /**
     * Determines whether this line is parallel to another line.
     *
     * @param other the other line to compare
     * @return true if the lines are parallel, false otherwise
     */
    public boolean isParallelTo(Line3D other) {
        if (other == null) {
            logger.log(Level.WARNING, "Attempted to compare parallelism with null Line3D");
            return false;
        }
        Point3D directionA = end.subtract(start);
        Point3D directionB = other.end.subtract(other.start);
        Point3D cross = directionA.crossProduct(directionB);
        boolean parallel = cross != null && cross.distanceFromOrigin() < 1e-10;
        logger.log(Level.INFO, "Lines {0} and {1} are parallel: {2}", new Object[]{this, other, parallel});
        return parallel;
    }

    /**
     * Computes the shortest distance between the infinite extensions of two lines.
     *
     * @param other the other line
     * @return the shortest distance between the two lines, or Double.NaN if other is null
     */
    public double shortestDistanceTo(Line3D other) {
        if (other == null) {
            logger.log(Level.WARNING, "Attempted to compute shortest distance to null Line3D");
            return Double.NaN;
        }

        Point3D u = end.subtract(start);
        Point3D v = other.end.subtract(other.start);
        Point3D w0 = start.subtract(other.start);

        double a = u.dotProduct(u);
        double b = u.dotProduct(v);
        double c = v.dotProduct(v);
        double d = u.dotProduct(w0);
        double e = v.dotProduct(w0);

        double denominator = a * c - b * b;

        if (Math.abs(denominator) < 1e-10) {
            Point3D cross = w0.crossProduct(u);
            double distance = cross == null ? Double.NaN : cross.distanceFromOrigin() / u.distanceFromOrigin();
            logger.log(Level.INFO, "Lines are parallel; shortest distance = {0}", distance);
            return distance;
        }

        double sc = (b * e - c * d) / denominator;
        double tc = (a * e - b * d) / denominator;

        Point3D closestPointOnThis = start.translate(u.getX() * sc, u.getY() * sc, u.getZ() * sc);
        Point3D closestPointOnOther = other.start.translate(v.getX() * tc, v.getY() * tc, v.getZ() * tc);
        double distance = closestPointOnThis.distanceTo(closestPointOnOther);

        logger.log(Level.INFO, "Shortest distance between lines {0} and {1} = {2}",
                   new Object[]{this, other, distance});

        return distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Line3D other = (Line3D) obj;
        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return String.format("Line3D[%s -> %s]", start, end);
    }
}
