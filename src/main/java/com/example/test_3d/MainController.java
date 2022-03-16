package com.example.test_3d;

import com.example.test_3d.classe.Cube;
import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane anchor_main;
    public AmbientLight ambient_light;
    public int chunk_size = 16, chunck_number = 10, block_size = 50, pos_x = 0, pos_z = 100, pos_y = 600, thickness = 2;
    public Color box_color;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ambient_light.setColor(Color.YELLOW);

        for (int y = 0; y < thickness; y++) {
            if (y < 3) {
                box_color = Color.DARKGREEN;
            } else {
                box_color = Color.BROWN;
            }
            for (int x = 0; x < chunck_number; x++) {
                for (int z = 0; z < chunck_number; z++) {
                    create_chunck(box_color, chunk_size, block_size, pos_x, pos_y, pos_z);
                    pos_x += block_size * chunk_size;
                }
                pos_x = 0;
                pos_z += block_size * chunk_size;
            }
            pos_x = 0;
            pos_z = 100;
            pos_y += block_size;
        }

        //Cube cube = new Cube(pos_x,pos_y,pos_z);


    }

    public void create_chunck(Color box_color, int chunk_size, int block_size, int pos_x, int pos_y, int pos_z) {
        int default_pos_z = pos_z;
        for (int col = 0; col < chunk_size; col++) {
            for (int row = 0; row < chunk_size; row++) {
                Box box = new Box(block_size, block_size, block_size);
                box.setCullFace(CullFace.NONE);
                box.setDrawMode(DrawMode.LINE);
                PhongMaterial box_mat = new PhongMaterial();

                box_mat.setDiffuseColor(box_color);
                box_mat.setSpecularColor(box_color.darker());

                box.setMaterial(box_mat);

                box.setTranslateX(pos_x);
                box.setTranslateZ(pos_z);
                box.setTranslateY(pos_y);

                pos_z += 50;

                anchor_main.getChildren().add(box);
            }
            pos_z = default_pos_z;
            pos_x += 50;
        }
    }

    public double lerp(double a0, double a1, double weight) {
        return (1.0 - weight) * a0 + weight * a1;
    }

    public void perlin_noise(int x, int y) {
        int x0 = x;
        int x1 = x + 1;

        int y0 = y;
        int y1 = y + 1;

        //return lerp();
    }
}