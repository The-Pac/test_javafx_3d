package com.example.test_3d;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public PerspectiveCamera camera = new PerspectiveCamera();
    public double xAxe = 0.0, yAxe = 0.0, zAxe = 0.0, xCamera = 0, yCamera = 0, zCamera = 0.0;
    public BooleanProperty z_Press = new SimpleBooleanProperty(false), q_Press = new SimpleBooleanProperty(false), d_Press = new SimpleBooleanProperty(false), s_Press = new SimpleBooleanProperty(false), shift_Press = new SimpleBooleanProperty(false);
    public BooleanBinding z_and_q = z_Press.and(q_Press), z_and_d = z_Press.and(d_Press), s_and_q = s_Press.and(q_Press), s_and_d = s_Press.and(d_Press);


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        camera.setTranslateX(xCamera);
        camera.setTranslateY(yCamera);
        camera.setTranslateZ(zCamera);

        scene.setFill(Color.BLUE);

        z_and_q.addListener((observable, oldValue, newValue) -> {
            //Z
            zAxe = zAxe + 10;
            camera.setTranslateZ(zAxe);
            //Q
            xAxe = xAxe - 10;
            camera.setTranslateX(xAxe);
        });

        z_and_d.addListener((observable, oldValue, newValue) -> {
            //Z
            zAxe = zAxe + 10;
            camera.setTranslateZ(zAxe);
            //D
            xAxe = xAxe + 10;
            camera.setTranslateX(xAxe);
        });

        s_and_q.addListener((observable, oldValue, newValue) -> {
            //S
            zAxe = zAxe - 10;
            camera.setTranslateZ(zAxe);
            //Q
            xAxe = xAxe - 10;
            camera.setTranslateX(xAxe);
        });

        s_and_d.addListener((observable, oldValue, newValue) -> {
            //S
            zAxe = zAxe - 10;
            camera.setTranslateZ(zAxe);
            //D
            xAxe = xAxe + 10;
            camera.setTranslateX(xAxe);
        });

        scene.setOnKeyPressed(keyEvent -> {
            double value_move = 10;
            switch (keyEvent.getCode()) {
                case Z:
                    z_Press.set(true);
                    if (shift_Press.get()) {
                        value_move = 10 * 2;
                    }
                    zAxe = zAxe + value_move;
                    camera.setTranslateZ(zAxe);
                    break;
                case D:
                    d_Press.set(true);
                    if (shift_Press.get()) {
                        value_move = 10 * 2;
                    }
                    xAxe = xAxe + value_move;
                    camera.setTranslateX(xAxe);
                    break;
                case Q:
                    q_Press.set(true);
                    if (shift_Press.get()) {
                        value_move = 10 * 2;
                    }
                    xAxe = xAxe - value_move;
                    camera.setTranslateX(xAxe);
                    break;
                case S:
                    s_Press.set(true);
                    if (shift_Press.get()) {
                        value_move = 10 * 2;
                    }
                    zAxe = zAxe - value_move;
                    camera.setTranslateZ(zAxe);
                    break;
                case SPACE:
                    yAxe = yAxe - 10;
                    camera.setTranslateY(yAxe);
                    break;
                case C:
                    yAxe = yAxe + 10;
                    camera.setTranslateY(yAxe);
                    break;
                case SHIFT:
                    shift_Press.set(true);
                    break;
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case Z:
                    z_Press.set(false);
                    break;
                case D:
                    d_Press.set(false);
                    break;
                case Q:
                    q_Press.set(false);
                    break;
                case S:
                    s_Press.set(false);
                    break;
                case SPACE:
                    break;
                case C:
                    break;
                case SHIFT:
                    shift_Press.set(false);
                    break;
            }
        });

        scene.setOnMouseMoved(event -> {
            xCamera = event.getX();
            yCamera = event.getY();
            double delta_x = event.getSceneX() - xCamera;
            double delta_y = event.getSceneY() - yCamera;

            camera.setTranslateX(camera.getTranslateX() - delta_x * 0.05);
            camera.setTranslateY(camera.getTranslateY() - delta_y * 0.05);

        });
        scene.setCursor(Cursor.NONE);
        scene.setCamera(camera);
        stage.setScene(scene);
        stage.show();
    }
}