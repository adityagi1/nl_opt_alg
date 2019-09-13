import Jama.*;

import java.lang.Math;

public class NewtonMethodOptimizer {
    public static void main(String[] args) {
        double [] currPoint= {1.0 , 1.0};
        for (int i = 1; i <= 7; i++) {
            currPoint = oneIteration(currPoint);
        }
        System.out.println("Solution : " + currPoint[0] + ", " + currPoint[1]);
        System.out.println("Objective Function Value: " + objFunctionEvaluator(currPoint) );

    }

    public static double[] gradientCalculator(double[] argPoint) {
        double x1 = argPoint[0];
        double x2 = argPoint[1];
        double g1 = Math.exp(x1 + 2 * x2 - 0.1) + Math.exp(x1 - 2 * x2 - 0.1) - Math.exp(-1 * x1 - 0.2);
        double g2 = 2 * Math.exp(x1 + 2 * x2 - 0.1) - 2 * Math.exp(x1 - 2 * x2 - 0.1);
        double[] ret_arr = new double[2];
        ret_arr[0] = g1;
        ret_arr[1] = g2;
        return ret_arr;
    }

    public static double objFunctionEvaluator(double[] evalPoint) {
        double x1 = evalPoint[0];
        double x2 = evalPoint[1];
        return (Math.exp(x1 + 2 * x2 - 0.1) + Math.exp(x1 - 2 * x2 - 0.1) + Math.exp(-1 * x1 - 0.2));
    }


    public static double[] nextPointCalculator(double[] prevPoint, double[] gradient, double[][] Hessian, double stepSize) {
        Matrix hInv = new Matrix(Hessian).inverse();
        //We are taking the gradient to be a row-vector with gradient.length columns and 1 row.
        Matrix g = new Matrix(gradient.length, 1);
        //Set the elements of g using the provided gradient argument.
        for (int i = 0; i < gradient.length; i++) {
            g.set(i, 0, gradient[i]);
        }
        Matrix pVector = new Matrix(prevPoint.length, 1);
        for (int j = 0; j < prevPoint.length; j++) {
            pVector.set(j, 0, prevPoint[j]);
        }
        Matrix nVector = pVector.minus(hInv.times(g).times(stepSize));
        double[] ret_arr = new double[nVector.getRowDimension()];
        for (int k = 0; k < nVector.getRowDimension(); k++) {
            ret_arr[k] = nVector.get(k, 0);
        }
        return ret_arr;
    }

    public static double[][] hessianCalculator(double[] argPoint) {
        double x1 = argPoint[0];
        double x2 = argPoint[1];
        double h00 = Math.exp(x1 + 2 * x2 - 0.1) + Math.exp(x1 - 2 * x2 - 0.1) + Math.exp(-1 * x1 - 0.2);
        double h01 = 2 * Math.exp(x1 + 2 * x2 - 0.1) - 2 * Math.exp(x1 - 2 * x2 - 0.1);
        double h10 = 2 * Math.exp(x1 + 2 * x2 - 0.1) - 2 * Math.exp(x1 - 2 * x2 - 0.1);
        double h11 = 4 * Math.exp(x1 + 2 * x2 - 0.1) + 4 * Math.exp(x1 - 2 * x2 - 0.1);
        double[][] hessianArray = {{h00, h01}, {h10, h11}};
        return hessianArray;
    }

    /*Executes one iteration of the Optimization method in question(Newton's Method in this case).
    Step size is calculated using the backtracking parameters _alpha & _beta. (Hard-coded)*/
    public static double[] oneIteration(double[] startingPoint) {
        double myGradient[] = gradientCalculator(startingPoint);
        double[][] myHessian = hessianCalculator(startingPoint);
        double stepSize = 1;
        //Use stepsize=1 to calculate new point.
        double[] currPoint = nextPointCalculator(startingPoint, myGradient, myHessian, stepSize);
        while (objFunctionEvaluator(currPoint) >= objFunctionEvaluator(startingPoint)) {
            stepSize *= 0.6;
            currPoint = nextPointCalculator(startingPoint, myGradient, myHessian, stepSize);
        }
        return currPoint;
    }
}