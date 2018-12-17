import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Window extends Application {


    private ObservableList<Pendulum> pendulumList = FXCollections.observableArrayList();
    private double[] y_arr = new double[2];
    private Counter counter = new Counter();
    private AnimationTimer timer;
    private double amplitude;
    private Pendulum pendulumToMove = null;


    @Override
    public void start(Stage primaryStage) throws Exception {

        final Pane root = new Pane();
        LayoutGenerator generator = new LayoutGenerator();
        GridPane grid = new GridPane();
        ObservableList<String> options =
                FXCollections.observableArrayList("3", "4", "5", "6", "7", "8", "9", "10");

        final Scene scene = new Scene(root, 1400, 700);
        primaryStage.setTitle("Symulacja Kołyski Newtona");
        primaryStage.setScene(scene);
        primaryStage.show();

        final ComboBox comboBox = new ComboBox(options);
        comboBox.setPromptText("wybierz");
        grid.setConstraints(comboBox, 0, 1);
        grid.setMargin(comboBox, new Insets(0, 20, 10, 70));
        grid.getChildren().add(comboBox);

        Button buttonStart = new Button("Start");
        buttonStart.setFont(new Font("Arial", 26));
        buttonStart.setTextFill(Color.GREEN);
        grid.setConstraints(buttonStart, 0, 4);
        grid.setMargin(buttonStart, new Insets(30, 20, 20, 70));
        grid.getChildren().add(buttonStart);

        Button buttonStop = new Button("Stop");
        buttonStop.setFont(new Font("Arial", 26));
        buttonStop.setTextFill(Color.RED);
        grid.setConstraints(buttonStop, 0, 4);
        grid.setMargin(buttonStop, new Insets(30, 20, 20, 70));
        grid.getChildren().add(buttonStop);
        buttonStop.setVisible(false);

        Button buttonReset = new Button("Reset");
        buttonReset.setFont(new Font("Arial", 23));
        grid.setConstraints(buttonReset, 0, 5);
        grid.setMargin(buttonReset, new Insets(10, 20, 20, 70));
        grid.getChildren().add(buttonReset);
        buttonReset.setDisable(true);

        Label label = new Label("Liczba kul:");
        label.setFont(new Font("Arial", 22));
        grid.setConstraints(label, 0, 0);
        grid.setMargin(label, new Insets(30, 20, 5, 70));
        grid.getChildren().add(label);

        Label label2 = new Label("Kąt wychylenia:");
        label2.setFont(new Font("Arial", 20));
        grid.setConstraints(label2, 0, 2);
        grid.setMargin(label2, new Insets(10, 20, 5, 70));
        grid.getChildren().add(label2);

        TextField textField = new TextField();
        textField.setFont(new Font("Arial", 20));
        textField.setMaxWidth(100.0);
        grid.setConstraints(textField, 0, 3);
        grid.setMargin(textField, new Insets(10, 20, 5, 70));
        grid.getChildren().add(textField);


        root.getChildren().add(grid);
        comboBox.setValue(3);
        textField.setText("40");
        comboAction(root, comboBox, generator);


        comboBox.setOnAction((event) -> {
            comboAction(root, comboBox, generator);
        });


        buttonStart.setOnAction((event) -> {
            buttonStop.setVisible(true);
            buttonStart.setVisible(false);
            buttonReset.setDisable(true);
            comboBox.setDisable(true);
            textField.setDisable(true);
            pendulumToMove = null;

            double newAmplitude = Double.parseDouble(textField.getText());

            if (amplitude != newAmplitude) {
                if (newAmplitude < 180 && newAmplitude > 0) {
                    amplitude = newAmplitude;
                } else textField.setText(String.valueOf((int) amplitude));
                y_arr[0] = 0;
                y_arr[1] = 0;
            }

            startAnimation(root);

        });

        buttonStop.setOnAction((event) -> {
            buttonStart.setVisible(true);
            buttonReset.setDisable(false);
            buttonStop.setVisible(false);
            comboBox.setDisable(false);
            textField.setDisable(false);
            timer.stop();
        });

        buttonReset.setOnAction((event) -> {
            buttonReset.setDisable(true);
            for (Pendulum sph : pendulumList) {
                root.getChildren().remove(sph.getCircle());
                root.getChildren().remove(sph.getLine());
            }
            comboAction(root, comboBox, generator);
        });


        pendulumList.addListener((ListChangeListener<Pendulum>) c -> {
            while (c.next()) {
                if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {

                        root.getChildren().remove(pendulumList.get(i).getCircle());
                        root.getChildren().remove(pendulumList.get(i).getLine());
                        root.getChildren().add(pendulumList.get(i).getCircle());
                        root.getChildren().add(pendulumList.get(i).getLine());
                    }
                }
            }
        });
    }


    private void startAnimation(final Pane root) {
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);
        timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {

                if(pendulumToMove == null){
                    pendulumToMove = pendulumList.get(pendulumList.size()-1);
                }

                if (lastUpdateTime.get() > 0) {
                    long elapsedTime = timestamp - lastUpdateTime.get();

                    if(checkCollision()){
                        if(pendulumToMove == pendulumList.get(0)){
                            resetPendulum(pendulumToMove);
                            pendulumToMove = pendulumList.get(pendulumList.size()-1);
                        }
                        else{
                            resetPendulum(pendulumToMove);
                            pendulumToMove = pendulumList.get(0);
                        }
                    }
                    movePendulums(elapsedTime, pendulumToMove);

                }
                lastUpdateTime.set(timestamp);

            }

        };
        timer.start();
    }


    private boolean checkCollision() {
        Pendulum firstPendulum, lastPendulum;

        firstPendulum = pendulumList.get(0);
        lastPendulum = pendulumList.get(pendulumList.size()-1);

        if(firstPendulum.checkCollision(pendulumList.get(1))){
            System.out.println("Wykryto kolizję pomiędzy P1 i P2!");
            //timer.stop();
            return true;
        }

        if(lastPendulum.checkCollision(pendulumList.get(pendulumList.size()-2))){
            System.out.println("Wykryto kolizję pomiędzy P" + (pendulumList.size()-2) + " i P" + (pendulumList.size()-1) + "!");
            //timer.stop();
            return true;
        }
        return false;
    }

    private void resetPendulum(Pendulum pendulum){
        pendulum.setSphereX(pendulum.getStartSphereX());
        pendulum.setSphereY(pendulum.getStartSphereY());
    }

    private void movePendulums(long elapsedTime, Pendulum pendulum) {

        double elapsedSeconds = elapsedTime / 1_000_000_000.0;
        int index = pendulumList.indexOf(pendulum);

        //System.out.println(elapsedSeconds);

        if (y_arr[0] == 0.0 && y_arr[1] == 0.0) {
            y_arr[0] = amplitude / 180.0 * Math.PI;
        }
        y_arr = counter.nextDegree(y_arr);

        float newX = 400.0f * (float) Math.sin(y_arr[0]);
        float newY = 400.0f * (float) Math.cos(y_arr[0]);
        pendulum.setSphereX(newX + 350.0f + getDistanceFromFirstBall(index)
                + (float)pendulum.getCircle().getRadius());
        pendulum.setSphereY(newY + 80.0f);


    }

    private float getDistanceFromFirstBall(int index){
        float distance = 0;
        for(int i = 0; i < index; i++){
            distance += pendulumList.get(i).getCircle().getRadius() * 2;
        }
        return distance;
    }

    private void comboAction(final Pane root, ComboBox comboBox, LayoutGenerator generator) {
        int index = Integer.parseInt(comboBox.getSelectionModel().getSelectedItem().toString());

        //czyszczenie starych elementów
        for (Pendulum sph : pendulumList) {
            root.getChildren().remove(sph.getCircle());
            root.getChildren().remove(sph.getLine());
        }
        root.getChildren().remove(generator.getRectangleBase());
        generator.clearListOfPendulums();

        //dodawanie nowych elementów
        generator.generate(index);
        pendulumList = generator.getListOfPendulums();


        for (Pendulum sph : pendulumList) {
            root.getChildren().add(sph.getCircle());
            root.getChildren().add(sph.getLine());
        }
        root.getChildren().add(generator.getRectangleBase());

        //wahadło na start
        y_arr[0] = 0;
        y_arr[1] = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
