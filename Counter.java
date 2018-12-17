import static java.lang.Math.*;
import java.util.function.BiFunction;


public class Counter {

    double g = 9.81;
    double l = 1.0;
    double dt = 0.01;

    double[] k1 = new double[2];
    double[] k2 = new double[2];
    double[] k3 = new double[2];
    double[] k4 = new double[2];
    double[] y1 = new double[2];
    double[] y2 = new double[2];
    double[] y3 = new double[2];
    double[] y4 = new double[2];
    double[] result = new double[2];


    double previous = 0;


    public double[] rhs(double[] y){
        double[] result;
        result = new double[2];

        result[0] = y[1];
        result[1] = -g/l*Math.sin(y[0]);

        return result;
    }

    double[] runge(double[] y) {
        k1 = rhs(y);


        y1[0] = y[0] + dt / 2.0 * k1[0];
        y1[1] = y[1] + dt / 2.0 * k1[1];

        k2 = rhs(y1);

        y2[0] = y[0] + dt / 2.0 * k2[0];
        y2[1] = y[1] + dt / 2.0 * k2[1];
        k3 = rhs(y2);


        y3[0] = y[0] + dt * k3[0];
        y3[1] = y[1] + dt * k3[1] ;
        k4 = rhs(y3);

        result[0] = y[0] + dt / 6.0 * (k1[0] + 2.0 * k2[0] + 2.0 * k3[0] + k4[0]);
        result[1] = y[1] + dt / 6.0 * (k1[1] + 2.0 * k2[1] + 2.0 * k3[1] + k4[1]);


        //System.out.println(result[0] + " Różnica: " + (result[0] - previous) + "\n");
        //System.out.print((int)(result[0] * 180 / Math.PI) + "(" + Math.round((result[0] * 180 / Math.PI - previous * 180 / Math.PI)*10)/10 + ") -> ");

        //previous = result[0];
        return result;
    }


    /*public void calculateDifferential() {

        double t0 = 0;
        double[] y_arr = new double[2];
        y_arr[0] = 40.0 / 180.0 * Math.PI;
        y_arr[1] = 0.0;

        while(t0 < 10) {
            y_arr = runge(y_arr);
        }

    }*/
    public double[] nextDegree(double[] y_arr){
        //double[] y_arr = new double[2];


        return runge(y_arr);
    }
}

