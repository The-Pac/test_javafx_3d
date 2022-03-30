package com.example.test_3d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.robot.Robot;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    public PerspectiveCamera camera = new PerspectiveCamera();
    public double xAxe = 0.0, yAxe = 0.0, zAxe = 0.0, xCamera = 0, yCamera = 0, zCamera = 0.0, old_xCamera = 0, old_yCamera = 0, default_xCamera = primaryScreenBounds.getWidth() / 2, default_yCamera = primaryScreenBounds.getHeight() / 2, old_zCamera = 0.0;
    public BooleanProperty z_Press = new SimpleBooleanProperty(false), q_Press = new SimpleBooleanProperty(false), d_Press = new SimpleBooleanProperty(false), s_Press = new SimpleBooleanProperty(false), shift_Press = new SimpleBooleanProperty(false);
    public BooleanBinding z_and_q = z_Press.and(q_Press), z_and_d = z_Press.and(d_Press), s_and_q = s_Press.and(q_Press), s_and_d = s_Press.and(d_Press);
    public Robot robot = new Robot();
    public float sensitivity = 0.05f;
    public Pane anchor_2d = new Pane();
    public AmbientLight ambient_light = new AmbientLight();
    public int chunk_size = 16, chunck_number = 2, block_size = 50, pos_x = 0, pos_z = 100, pos_y = 600, thickness = 1;
    public Color box_color;
    public Label fps_label = new Label();
    public Circle crosshair_dot = new Circle();
    public double height, width, delta_x = 0, delta_y = 0;
    public Group root_3d = new Group();

    public Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS), cameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    public static void main(String[] args) {
        launch();
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public void start(Stage stage) {

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        width = screenBounds.getWidth();
        height = screenBounds.getHeight();
        anchor_2d.setPrefSize(width, height);

        Group root = new Group();

        //2D scene

        //Crosshair
        anchor_2d.getChildren().add(create_crosshair());
        fps_label.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        anchor_2d.getChildren().add(fps_label);

        //3D scene
        ambient_light.setColor(Color.YELLOW);
        root_3d.getChildren().add(ambient_light);
        SubScene subScene = new SubScene(root_3d, width, height, true, SceneAntialiasing.BALANCED);

        build_camera();
        subScene.setCamera(camera);
        subScene.setViewOrder(1);

        anchor_2d.getChildren().add(subScene);

        root.getChildren().add(anchor_2d);
        Scene scene = new Scene(root);

        scene.setFill(new ImagePattern(new Image("background.png")));

        build_3Dscene();

        generate_terrain();
        update();

        //event
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

            old_xCamera = xCamera;
            old_yCamera = yCamera;

            xCamera = event.getSceneX();
            yCamera = event.getSceneY();

            double delta_x = xCamera - old_xCamera;
            double delta_y = yCamera - old_yCamera;


            cameraRotateY.setAngle(clamp(((cameraRotateY.getAngle() + delta_x * sensitivity) % 360 + 540) % 360 - 180, -360, 360));
            cameraRotateX.setAngle(clamp(((cameraRotateX.getAngle() - delta_y * sensitivity) % 360 + 540) % 360 - 180, -90, 90));

            System.out.println("Y:" + cameraRotateY.getAngle() + " X:" + cameraRotateX.getAngle());
        });
        //

        stage.setFullScreen(true);
        scene.setCursor(Cursor.NONE);
        stage.setScene(scene);
        stage.show();
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

                root_3d.getChildren().add(box);
            }
            pos_z = default_pos_z;
            pos_x += 50;
        }
    }

    public void build_3Dscene() {

    }

    public AnchorPane create_crosshair() {
        AnchorPane anchorPane_crosshair = new AnchorPane();
        double width = 50, height = 50, crosshair_height = 2, crosshair_width = 20, space = 10;

        anchorPane_crosshair.setPrefSize(width, height);

        Rectangle crosshair_top = new Rectangle(),
                crosshair_bottom = new Rectangle(),
                crosshair_right = new Rectangle(),
                crosshair_left = new Rectangle();


        //crosshair fill
        crosshair_top.setFill(Color.GREEN);
        crosshair_bottom.setFill(Color.GREEN);
        crosshair_left.setFill(Color.GREEN);
        crosshair_right.setFill(Color.GREEN);
        crosshair_dot.setFill(Color.GREEN);

        //width height
        crosshair_top.setWidth(crosshair_width);
        crosshair_top.setHeight(crosshair_height);

        crosshair_bottom.setWidth(crosshair_width);
        crosshair_bottom.setHeight(crosshair_height);

        crosshair_right.setWidth(crosshair_width);
        crosshair_right.setHeight(crosshair_height);

        crosshair_left.setWidth(crosshair_width);
        crosshair_left.setHeight(crosshair_height);

        //Position

        //dot crosshair
        crosshair_dot.setRadius(2);
        crosshair_dot.setCenterX((width / 2));
        crosshair_dot.setCenterY((height / 2));

        //top
        crosshair_top.setX(crosshair_dot.getCenterX() - (crosshair_width / 2));
        crosshair_top.setY(crosshair_dot.getCenterY() - (crosshair_width + space));

        //bottom
        crosshair_bottom.setX(crosshair_dot.getCenterX() - (crosshair_width / 2));
        crosshair_bottom.setY(crosshair_dot.getCenterY() + (crosshair_width + space));

        //right
        crosshair_right.setX(crosshair_dot.getCenterX() + (crosshair_width));
        crosshair_right.setY(crosshair_dot.getCenterY() - (crosshair_height / 2));

        //left
        crosshair_left.setX(crosshair_dot.getCenterX() - (crosshair_width * 2));
        crosshair_left.setY(crosshair_dot.getCenterY() - (crosshair_height / 2));


        //rotate
        crosshair_top.setRotate(90);
        crosshair_bottom.setRotate(90);


        //add crosshair
        anchorPane_crosshair.getChildren().add(crosshair_top);
        anchorPane_crosshair.getChildren().add(crosshair_bottom);
        anchorPane_crosshair.getChildren().add(crosshair_right);
        anchorPane_crosshair.getChildren().add(crosshair_left);
        anchorPane_crosshair.getChildren().add(crosshair_dot);

        anchorPane_crosshair.setTranslateX((this.width / 2) - (width / 2));
        anchorPane_crosshair.setTranslateY((this.height / 2) - (width / 2));

        return anchorPane_crosshair;
    }

    public void update() {
        AnimationTimer frameRateMeter = new AnimationTimer() {
            final long[] frameTimes = new long[100];
            int frameTimeIndex = 0;

            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                long elapsedNanos = now - oldFrameTime;
                long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
                Platform.runLater(() -> fps_label.setText(String.format("FPS: %.3f%n", frameRate)));
                cameraRotateY.setAngle(clamp(((cameraRotateY.getAngle() + delta_x * sensitivity) % 360 + 540) % 360 - 180, -360, 360));
                cameraRotateX.setAngle(clamp(((cameraRotateX.getAngle() - delta_y * sensitivity) % 360 + 540) % 360 - 180, -90, 90));

            }
        };
        frameRateMeter.start();
    }

    public void generate_terrain() {
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
    }

    public void build_camera() {
        camera.setTranslateX(xCamera);
        camera.setTranslateY(yCamera);
        camera.setTranslateZ(zCamera);

        camera.setFieldOfView(70);
        camera.setVerticalFieldOfView(false);
        camera.setFarClip(1000);
        camera.setNearClip(0.1f);

        camera.getTransforms().addAll(cameraRotateX, cameraRotateY);
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

    public MeshView block(int x, int y, int z, float height, float with) {
        float[] points =
                {
                        -with / 2, height / 2, 0,
                        -with / 2, -height / 2, 0,
                        with / 2, height / 2, 0,
                        with / 2, -height / 2, 0
                };


        float[] texCoords =
                {
                        1.0f, 1.0f,
                        1.0f, 0.0f,
                        0.0f, 1.0f,
                        0.0f, 0.0f,
                };
        int[] faces =
                {
                        2, 3, 0, 2, 1, 0,
                        2, 3, 1, 0, 3, 1,

                };
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


        meshView.setDrawMode(DrawMode.LINE);
        meshView.setMaterial(new PhongMaterial(Color.DARKGREEN));
        return meshView;
    }
}