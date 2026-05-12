package com.csc205.project1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Line3DTest {

    @Test
    void testLengthAndMidpoint() {
        Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 3, 4));
        assertEquals(5.0, line.length(), 1e-9);
        assertEquals(new Point3D(0, 1.5, 2), line.midpoint());
    }

    @Test
    void testDirectionAndParallel() {
        Line3D lineA = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 1, 0));
        Line3D lineB = new Line3D(new Point3D(0, 0, 1), new Point3D(1, 1, 1));

        assertEquals(new Point3D(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0), lineA.direction());
        assertTrue(lineA.isParallelTo(lineB));
    }

    @Test
    void testShortestDistanceToSkewLines() {
        Line3D lineA = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 0, 0));
        Line3D lineB = new Line3D(new Point3D(0, 1, 1), new Point3D(0, 2, 1));

        assertEquals(1.414213562, lineA.shortestDistanceTo(lineB), 1e-9);
    }
}
