import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class LayoutGenerator {

    ObservableList<Pendulum> listOfPendulums = FXCollections.observableArrayList();
    Rectangle rectangle;

    public void generate(int amountOfSphere, boolean isRandom){

        final Random random = new Random();
        float x = 350.0f;
        double minRadius = 10.0, maxRadius = 40.0;
        float rectangleWidth = 100.0f;

        for(int i = 0; i < amountOfSphere; i++){
            double radius = 0;
            if(isRandom){
                radius = minRadius + (maxRadius-minRadius) * random.nextDouble();
            }
            else{
                radius = 30.0;
            }
            x += radius;
            listOfPendulums.add(new Pendulum("Wahadło " + i,x,(float)radius,80.0f, 400.0f));
            rectangleWidth += 2 * radius;
            x += radius;
        }

        rectangle = new Rectangle(300.0f, 0, rectangleWidth, 80.0f);
        rectangle.setFill(Color.BROWN);
    }

    public ObservableList<Pendulum> getListOfPendulums(){
        return listOfPendulums;
    }

    public Rectangle getRectangleBase(){
        return rectangle;
    }

    public void clearListOfPendulums(){
        listOfPendulums.clear();
    }

}
