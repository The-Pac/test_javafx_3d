package com.example.test_3d.classe;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Cube {
    int x, y, z;


    float[] points =
            {
                    50, 0, 0,  // v0 (iv0 = 0)
                    45, 10, 0, // v1 (iv1 = 1)
                    55, 10, 0  // v2 (iv2 = 2)
            };


    float[] texCoords =
            {
                    0.5f, 0.5f, // t0 (it0 = 0)
                    0.0f, 1.0f, // t1 (it1 = 1)
                    1.0f, 1.0f  // t2 (it2 = 2)
            };
    int[] faces =
            {
                    0, 0, 2, 2, 1, 1, // iv0, it0, iv2, it2, iv1, it1 (front face)
                    0, 0, 1, 1, 2, 2  // iv0, it0, iv1, it1, iv2, it2 back face
            };


    public Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        MeshView meshView = new MeshView();

        meshView.setTranslateX(x);
        meshView.setTranslateY(y);
        meshView.setTranslateZ(z);

        meshView.setScaleX(10.0);
        meshView.setScaleY(10.0);
        meshView.setScaleZ(10.0);

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);
        meshView.setMesh(mesh);
    }
}
