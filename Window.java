import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    CheckBox checkBoxRandom = new CheckBox();
    private TableView<Pendulum> table = new TableView<Pendulum>();
    TableColumn nameColumn = new TableColumn("Wahadło");
    TableColumn collisionColumn = new TableColumn("Kolizja z");
    TableColumn radiusColumn = new TableColumn("Promień");
    TableColumn massColumn = new TableColumn("Masa");
    TableColumn angularVelocityColumn = new TableColumn("Pręd. kątowa");
    TableColumn angleColumn = new TableColumn("Kąt");


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


        checkBoxRandom.setText("Różne masy kul");
        checkBoxRandom.setFont(new Font("Arial", 18));
        checkBoxRandom.setSelected(false);
        grid.setConstraints(checkBoxRandom, 0, 4);
        grid.setMargin(checkBoxRandom, new Insets(30, 20, 20, 70));
        grid.getChildren().add(checkBoxRandom);

        Button buttonStart = new Button("Start");
        buttonStart.setFont(new Font("Arial", 26));
        buttonStart.setTextFill(Color.GREEN);
        grid.setConstraints(buttonStart, 0, 5);
        grid.setMargin(buttonStart, new Insets(30, 20, 20, 70));
        grid.getChildren().add(buttonStart);

        Button buttonStop = new Button("Stop");
        buttonStop.setFont(new Font("Arial", 26));
        buttonStop.setTextFill(Color.RED);
        grid.setConstraints(buttonStop, 0, 5);
        grid.setMargin(buttonStop, new Insets(30, 20, 20, 70));
        grid.getChildren().add(buttonStop);
        buttonStop.setVisible(false);

        Button buttonReset = new Button("Reset");
        buttonReset.setFont(new Font("Arial", 23));
        grid.setConstraints(buttonReset, 0, 6);
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

        nameColumn.setMinWidth(50.0);
        collisionColumn.setMinWidth(50.0);
        massColumn.setMaxWidth(70.0);
        angleColumn.setMinWidth(60.0);
        table.setEditable(false);
        table.getColumns().addAll(nameColumn, collisionColumn, angleColumn, angularVelocityColumn, massColumn, radiusColumn);
        grid.setRowSpan(table, 7);
        grid.setConstraints(table, 4, 0);
        grid.setMargin(table, new Insets(40, 20, 10, 850));
        grid.getChildren().add(table);

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

        //przydzielanie sąsiadów
        Pendulum pend;
        for(int i = 0; i < pendulumList.size(); i++){
            pend = pendulumList.get(i);
            if(i > 0){
                pend.setLeftNeighbour(pendulumList.get(i-1));
            }
            if(i < pendulumList.size()-1){
                pend.setRightNeighbour(pendulumList.get(i+1));
            }
        }

        timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {

                /*if(pendulumToMove == null){
                    pendulumToMove = pendulumList.get(pendulumList.size()-1);
                }*/

                if (lastUpdateTime.get() > 0) {
                    long elapsedTime = timestamp - lastUpdateTime.get();

                    Pendulum pendulumInCollision;

                    for(Pendulum pendulum : pendulumList){
                        pendulumInCollision = checkCollision(pendulum);
                        if(pendulumInCollision != null && pendulumInCollision != pendulum.getCollisionHandled()){

                            System.out.println("Sprawdzenie " + pendulum.getCollisionHandledStr() + " i " + pendulumInCollision.getName());

                            handleCollision(pendulum, pendulumInCollision);
                        }
                    }

                    /*
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
                    */
                    movePendulums(elapsedTime);

                }
                lastUpdateTime.set(timestamp);

            }

        };
        timer.start();
    }


    private Pendulum checkCollision(Pendulum pendulumToCheck) {

        for (Pendulum pendulum : pendulumList) {
            if (pendulumToCheck != pendulum && pendulumToCheck.checkCollision(pendulum)) {
                System.out.println("Wykryto kolizję pomiędzy " + pendulumToCheck.getName() + " i " + pendulum.getName());
                return pendulum;
            }
            else{
                pendulumToCheck.setCollisionHandled(null);
            }
        }
        return null;
    }

    private void handleCollision(Pendulum pendulum1, Pendulum pendulum2){
        double resultantVelocity1, resultantVelocity2;

        if(pendulum1.getCollisionHandled() != pendulum2 && pendulum2.getCollisionHandled() != pendulum1) {

            System.out.println("------------Obsługa kolizji-------------");
            System.out.println("Prędkość przed zderzeniem wahadła [" + pendulum1.getName() + "]: " + pendulum1.getAngularVelocity()
                    + " oraz [" + pendulum2.getName() + "]: " + pendulum2.getAngularVelocity());

            resultantVelocity1 = (pendulum1.getVelocity() * (pendulum1.getSphereMass() - pendulum2.getSphereMass())
                    + 2 * pendulum2.getSphereMass() * pendulum2.getVelocity()) / (pendulum1.getSphereMass() + pendulum2.getSphereMass());
            resultantVelocity2 = (pendulum2.getVelocity() * (pendulum2.getSphereMass() - pendulum1.getSphereMass())
                    + 2 * pendulum1.getSphereMass() * pendulum1.getVelocity()) / (pendulum1.getSphereMass() + pendulum2.getSphereMass());

            pendulum1.setVelocity(resultantVelocity1);
            pendulum2.setVelocity(resultantVelocity2);

            System.out.println("Prędkość po zderzeniu wahadła [" + pendulum1.getName() + "]: " + resultantVelocity1
                    + " oraz [" + pendulum2.getName() + "]: " + resultantVelocity2);

            System.out.println("------------Koniec obsługi kolizji-------------");

            //if(pendulum1.getCollisionHandled() != pendulum2){
            pendulum1.setCollisionHandled(pendulum2);
            pendulum2.setCollisionHandled(pendulum1);
            //}
        }


       /* float pend1_X = pendulum1.getSphereX();
        float pend1_Y = pendulum1.getSphereY();
        float pend2_X = pendulum2.getSphereX();
        float pend2_Y = pendulum2.getSphereY();

        //prosta przechodząca przez środki kul(ax + by = c)
        double a = pend2_Y - pend1_Y;
        double b = pend1_X - pend2_X;
        double c = a*(pend1_X) + b*(pend1_Y);*/

    }

    /*private void resetPendulum(Pendulum pendulum){
        pendulum.setSphereX(pendulum.getStartSphereX());
        pendulum.setSphereY(pendulum.getStartSphereY());
    }*/

    private void movePendulums(long elapsedTime) {

        if(pendulumList.get(pendulumList.size()-1).getAngle() == 0.0) {
            pendulumList.get(pendulumList.size() - 1).setAngle(amplitude / 180.0 * Math.PI);
        }

        /*if(pendulumList.get(0).getAngle() == 0.0) {
            pendulumList.get(0).setAngle(-amplitude / 180.0 * Math.PI);
        }*/

        for(Pendulum pendulum : pendulumList) {
            double elapsedSeconds = elapsedTime / 1_000_000_000.0;
            int index = pendulumList.indexOf(pendulum);

            //System.out.println(elapsedSeconds);
            y_arr[0] = pendulum.getAngle();
            y_arr[1] = pendulum.getAngularVelocity();
            y_arr = counter.nextDegree(y_arr);

            pendulum.setAngle(y_arr[0]);
            pendulum.setAngularVelocity(y_arr[1]);

            //sposób na odświeżanie tabeli
            table.getColumns().get(0).setVisible(false);
            table.getColumns().get(0).setVisible(true);


            float newX = pendulum.getHeight() * (float) Math.sin(pendulum.getAngle());
            float newY = pendulum.getHeight() * (float) Math.cos(pendulum.getAngle());
            pendulum.setSphereX(newX + 350.0f + getDistanceFromFirstBall(index)
                    + (float) pendulum.getCircle().getRadius());
            pendulum.setSphereY(newY + 80.0f);

        }

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
        generator.generate(index, checkBoxRandom.isSelected());
        pendulumList = generator.getListOfPendulums();

        for (Pendulum sph : pendulumList) {
            root.getChildren().add(sph.getCircle());
            root.getChildren().add(sph.getLine());
            nameColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, String>("name"));
            collisionColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, String>("collisionHandledStr"));
            radiusColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, Float>("sphereRadius"));
            massColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, Double>("sphereMass"));
            angularVelocityColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, Double>("angularVelocity"));
            angleColumn.setCellValueFactory(
                    new PropertyValueFactory<Pendulum, Double>("angle"));
            table.setItems(pendulumList);
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
