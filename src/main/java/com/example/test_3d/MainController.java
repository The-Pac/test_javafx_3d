package com.example.test_3d;

import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane anchor_main;
    public AmbientLight ambient_light;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ambient_light.setColor(Color.YELLOW);


        Box plateform = new Box(1000, 50, 1000);
        Cylinder cylinder = new Cylinder(50, 100);
        Sphere sphere = new Sphere(50);

        cylinder.setCullFace(CullFace.NONE);
        sphere.setCullFace(CullFace.NONE);
        plateform.setCullFace(CullFace.NONE);

        PhongMaterial sphere_mat = new PhongMaterial();
        PhongMaterial cylinder_mat = new PhongMaterial();
        PhongMaterial platform_mat = new PhongMaterial();

        platform_mat.setDiffuseColor(Color.DARKGREEN);
        cylinder_mat.setDiffuseColor(Color.RED);
        sphere_mat.setDiffuseColor(Color.BLACK);


        sphere.setMaterial(sphere_mat);
        cylinder.setMaterial(cylinder_mat);
        plateform.setMaterial(platform_mat);


        plateform.setTranslateX(400);
        plateform.setTranslateY(600);
        plateform.setTranslateZ(0);

        cylinder.setTranslateX(200);
        cylinder.setTranslateY(550);
        cylinder.setTranslateZ(0);

        sphere.setTranslateX(500);
        sphere.setTranslateY(550);
        sphere.setTranslateZ(0);


        anchor_main.getChildren().add(plateform);
        anchor_main.getChildren().add(cylinder);
        anchor_main.getChildren().add(sphere);
    }
}