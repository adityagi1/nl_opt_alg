import java.text.DecimalFormat;
import java.util.ArrayList;
import java.lang.Math;

public class SteepestDescentOptimizer{

    /*Calculates the gradient at a particular point.*/
    public static double[] gradientCalculator(double [] argPoint) {
        double x1 = argPoint[0];
        double x2 = argPoint[1];
        double g1=  Math.exp(x1 + 2*x2 - 0.1) + Math.exp(x1 - 2*x2 - 0.1) - Math.exp(-1*x1 - 0.2);
        double g2= 2 * Math.exp(x1 + 2*x2 - 0.1) - 2 * Math.exp(x1 - 2*x2 - 0.1);
        double [] ret_arr = new double[2];
        ret_arr[0] = g1;
        ret_arr[1] = g2;
        return ret_arr;
    }

    /*Evaluates the objective function at EVALPOINT.*/
    public static double objFunctionEvaluator(double[] evalPoint) {
        double x1 = evalPoint[0];
        double x2 = evalPoint[1];
        return (Math.exp(x1 + 2*x2 - 0.1) + Math.exp(x1 - 2*x2 -0.1) + Math.exp(-1*x1 - 0.2));
    }

    public static double[] nextPointCalculator (double[] prevPoint, double[] gradient, double stepSize) {
        double[] nextPoint = new double[2];
        for (int i=0; i<nextPoint.length; i++) {
            nextPoint[i] = prevPoint[i] - stepSize * gradient[i];
        }
        return nextPoint;
    }

    /*Executes one iteration of the gradient method. Step size is calculated using the backtracking
    parameters _alpha & _beta.*/
    public static double[] oneIteration(double[] startingPoint) {
        double myGradient[] = gradientCalculator(startingPoint);
        double stepSize = 1;
        //Use stepsize=1 to calculate new point.
        double[] currPoint = nextPointCalculator(startingPoint,myGradient,stepSize);
        while (objFunctionEvaluator(currPoint) >= objFunctionEvaluator(startingPoint)) {
            stepSize *= 0.6;
            currPoint = nextPointCalculator(startingPoint,myGradient,stepSize);
        }
        return currPoint;
    }

    public static void main(String[] args) {
        double [] currPoint= {1.0 , 1.0};
        for (int i =1; i <= 50; i++) {
            currPoint = oneIteration(currPoint);
        }
        System.out.println("Solution: " + currPoint[0] + currPoint[1]);
        System.out.println(("Objective Function Value at Optimality:" + objFunctionEvaluator(currPoint)));
        System.out.println( "ln of Objective Function at Optimality: "+  Math.log(objFunctionEvaluator(currPoint)));


        }
    }
