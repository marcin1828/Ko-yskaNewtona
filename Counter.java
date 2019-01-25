
public class Counter {

    double g = 9.81;
    double l = 1.0;
    double dt = 0.006;

    double[] k1 = new double[2];
    double[] k2 = new double[2];
    double[] k3 = new double[2];
    double[] k4 = new double[2];
    double[] y1 = new double[2];
    double[] y2 = new double[2];
    double[] y3 = new double[2];
    double[] y4 = new double[2];
    double[] result = new double[2];



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
        return result;
    }

    public double[] nextDegree(double[] y_arr){

        return runge(y_arr);
    }
}

