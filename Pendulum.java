import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Pendulum {

    private float sphereX;
    private float sphereY;
    private float startSphereX;
    private float startSphereY;
    private float sphereRadius;
    private float height;
    private Circle circle;
    private Line line;
    private int leftNeighbour;
    private int rightNeighbour;

    public float getStartSphereX() {
        return startSphereX;
    }

    public float getStartSphereY() {
        return startSphereY;
    }

    public void setSphereX(float sphereX) {
        this.sphereX = sphereX;
        this.circle.setCenterX(sphereX);
        this.line.setEndX(sphereX);
    }

    public float getSphereX() {
        return sphereX;
    }

    public void setSphereY(float sphereY) {
        this.sphereY = sphereY;
        this.circle.setCenterY(sphereY);
        this.line.setEndY(sphereY);
    }



    public Pendulum(float sphereX, float sphereRadius, float attachY, float height){
        this.sphereX = sphereX;
        this.sphereY = attachY + height;
        this.sphereRadius = sphereRadius;
        this.height = height;
        this.startSphereX = sphereX;
        this.startSphereY = sphereY;

        circle = new Circle();
        circle.setCenterX(sphereX);
        circle.setCenterY(sphereY);
        circle.setRadius(sphereRadius);

        line = new Line();
        line.setStartX(sphereX);
        line.setStartY(attachY);
        line.setEndX(sphereX);
        line.setEndY(sphereY);
    }

    public Circle getCircle(){
        return circle;
    }

    public Line getLine(){
        return line;
    }

    public boolean checkCollision(Pendulum neighbour){

        float distance = (float)Math.sqrt((this.sphereX - neighbour.sphereX)
                *(this.sphereX - neighbour.sphereX)+(this.sphereY - neighbour.sphereY)
                *(this.sphereY - neighbour.sphereY));
        if((distance + 0.001f) < this.sphereRadius + neighbour.sphereRadius) return true;
        return false;
    }

}
