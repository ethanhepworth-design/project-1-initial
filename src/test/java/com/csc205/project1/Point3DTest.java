package com.csc205.project1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    @Test
    void testDistanceAndMidpoint() {
        Point3D a = new Point3D(1, 2, 3);
        Point3D b = new Point3D(4, 6, 3);

        assertEquals(5.0, a.distanceTo(b), 1e-9);
        assertEquals(new Point3D(2.5, 4, 3), a.midpoint(b));
    }

    @Test
    void testVectorOperations() {
        Point3D a = new Point3D(1, 0, 0);
        Point3D b = new Point3D(0, 1, 0);

        assertEquals(0.0, a.dotProduct(b), 1e-9);
        assertEquals(new Point3D(0, 0, 1), a.crossProduct(b));
        assertEquals(1.0, a.normalize().distanceFromOrigin(), 1e-9);
    }

    @Test
    void testRotations() {
        Point3D point = new Point3D(1, 0, 0);
        Point3D rotated = point.rotateZ(Math.PI / 2);
        assertEquals(0.0, rotated.getX(), 1e-9);
        assertEquals(1.0, rotated.getY(), 1e-9);
        assertEquals(0.0, rotated.getZ(), 1e-9);
    }

    @Test
    void testScalingAndTranslation() {
        Point3D point = new Point3D(1, -2, 3);
        assertEquals(new Point3D(2, -4, 6), point.scale(2));
        assertEquals(new Point3D(2, -1, 2), point.translate(1, 1, -1));
    }
}
