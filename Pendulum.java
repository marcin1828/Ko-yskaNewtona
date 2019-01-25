import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Pendulum {

    private String name;
    private float sphereX;
    private float sphereY;
    private float startSphereX;
    private float startSphereY;
    private float sphereRadius;
    private double sphereMass;
    private double velocity;
    private double angularVelocity;
    private double angle;
    private float height;
    private Circle circle;
    private Line line;
    private Pendulum leftNeighbour;
    private Pendulum rightNeighbour;
    private Pendulum collisionHandled;
    private String collisionHandledStr;



    //---- Getters and Setters---------------------------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public float getStartSphereX() {
        return startSphereX;
    }
    public float getStartSphereY() {
        return startSphereY;
    }
    public double getSphereMass() { return sphereMass; }
    public float getSphereRadius() { return sphereRadius; }
    public float getSphereX() {
        return sphereX;
    }
    public float getSphereY() {
        return sphereY;
    }
    public double getVelocity() { return velocity; }
    public void setVelocity(double velocity) {
        this.velocity = velocity;
        this.angularVelocity = velocity; // w przypadku wahadeł o dł. 1 m
    }
    public float getHeight() { return height; }

    public void setSphereX(float sphereX) {
        this.sphereX = sphereX;
        this.circle.setCenterX(sphereX);
        this.line.setEndX(sphereX);
    }

    public void setSphereY(float sphereY) {
        this.sphereY = sphereY;
        this.circle.setCenterY(sphereY);
        this.line.setEndY(sphereY);
    }

    public Circle getCircle(){
        return circle;
    }
    public Line getLine(){
        return line;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
        this.velocity = angularVelocity; // w przypadku wahadeł o dł. 1 m
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Pendulum getLeftNeighbour() {
        return leftNeighbour;
    }

    public void setLeftNeighbour(Pendulum leftNeighbour) {
        this.leftNeighbour = leftNeighbour;
    }

    public Pendulum getRightNeighbour() {
        return rightNeighbour;
    }

    public void setRightNeighbour(Pendulum rightNeighbour) {
        this.rightNeighbour = rightNeighbour;
    }

    public Pendulum getCollisionHandled() {
        return collisionHandled;
    }

    public void setCollisionHandled(Pendulum collisionHandled) {
        this.collisionHandled = collisionHandled;
        if(collisionHandled != null) this.collisionHandledStr = collisionHandled.getName();
        else this.collisionHandledStr = "null";
    }

    public String getCollisionHandledStr() {
        return collisionHandledStr;
    }

    //--------------------------------------------------------



    public Pendulum(String name, float sphereX, float sphereRadius, float attachY, float height){
        this.name = name;
        this.sphereX = sphereX;
        this.sphereY = attachY + height;
        this.sphereRadius = sphereRadius;
        this.sphereMass = Math.PI * sphereRadius * sphereRadius / 100;
        this.height = height;
        this.startSphereX = sphereX;
        this.startSphereY = sphereY;
        this.angularVelocity = 0;
        this.angle = 0;
        this.leftNeighbour = null;
        this.rightNeighbour = null;
        this.collisionHandled = null;

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

    public boolean checkCollision(Pendulum neighbour){

        float distance = (float)Math.sqrt((this.sphereX - neighbour.sphereX)
                *(this.sphereX - neighbour.sphereX)+(this.sphereY - neighbour.sphereY)
                *(this.sphereY - neighbour.sphereY));
        //distance + 0.001f
        if((distance) <= this.sphereRadius + neighbour.sphereRadius) return true;
        return false;
    }

}
