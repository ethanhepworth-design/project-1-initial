package com.csc205.project1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Cube3DTest {

    @Test
    void testVolumeSurfaceAndPerimeter() {
        Cube3D cube = new Cube3D(new Point3D(1, 1, 1), 2);
        assertEquals(8.0, cube.volume(), 1e-9);
        assertEquals(24.0, cube.surfaceArea(), 1e-9);
        assertEquals(24.0, cube.perimeterLength(), 1e-9);
    }

    @Test
    void testEdgesAndValidation() {
        Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 2);
        List<Line3D> edges = cube.getEdges();
        assertEquals(12, edges.size());
        assertTrue(cube.isValidCube());
    }

    @Test
    void testTransformationsPreserveCube() {
        Cube3D cube = new Cube3D(new Point3D(1, 2, 3), 2);
        Cube3D rotated = cube.rotateX(Math.PI / 2).rotateY(Math.PI / 3).rotateZ(Math.PI / 4);
        assertEquals(12, rotated.getEdges().size());
        assertTrue(rotated.isValidCube());

        Cube3D translated = cube.translate(5, -1, 2);
        assertTrue(translated.getCenter().equals(new Point3D(6, 1, 5)));
    }
}
