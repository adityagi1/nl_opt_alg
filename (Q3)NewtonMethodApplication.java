import Jama.*;
import java.lang.Math;
import java.util.ArrayList;

public class NewtonMethodApplication {
    public static ArrayList<Matrix> _randomVectors = new ArrayList<Matrix>();
    public static int _numDimensions = 30;

    public static void main(String[] args) {
        // Initialize _randomVectors to _ hold _numDimension random vectors.
        Matrix rVector = new Matrix(_numDimensions,1);
        for (int num =0 ; num < _numDimensions; num++) {
        for (int row = 0; row < _numDimensions; row++) {
            rVector.set(row, 0, Math.random());
        }
        _randomVectors.add(rVector);
        }
        //Intializing currPoint to be an array of 0.1's.
        Matrix currVector = new Matrix(_numDimensions, 1);
        for (int a = 0; a < _numDimensions; a++) {
            currVector.set(a, 0, 0.1);
        }
        System.out.println("Initial Vector: " + objFunctionEvaluator(currVector));
        for (int i = 1; i <= 300; i++) {
            currVector = oneIteration(currVector);
        }

        System.out.println("Objective Function Value at Optimality: " + objFunctionEvaluator(currVector) );

    }


    /*Cacluates and returns the gradient vector NEED TO RETURN A MATRIX FOR CONSISTENCY.
     Takes a vector 'x' (Matrix object) as an argument.*/
    public static Matrix gradientCalculator(Matrix x) {
        Matrix grad = new Matrix(1, _numDimensions);
        for (int j = 0; j < _numDimensions; j++) {
            double runningSum = 0;
            for (int i = 0; i < _randomVectors.size(); i++) {
                runningSum += _randomVectors.get(i).get(j, 0) / (1 - _randomVectors.get(i).transpose().times(x).get(0, 0));
            }
            double val = ((2 * x.get(j, 0)) / (1 - Math.pow(x.get(j, 0), 2))) + runningSum;
            grad.set(0, j, val);
        }
        return grad;
    }

    public static Matrix hessianCalculator(Matrix x) {
        Matrix H = new Matrix(_numDimensions, _numDimensions);
        for (int row = 0; row < H.getRowDimension(); row++) {
            for (int col = 0; col < H.getColumnDimension(); col++) {
                if (row == col) {
                    double val1 = (2 * (1 - Math.pow(x.get(row, 0), 2)) + 4 * Math.pow(x.get(row, 0), 2)) /
                            (Math.pow((1 - Math.pow(x.get(row, 0), 2)), 2));
                    double runningSum1 = 0;
                    for (int k = 0; k < _numDimensions; k++) {
                        runningSum1 += (Math.pow(_randomVectors.get(k).get(row, 0), 2)) /
                                (Math.pow((1 - _randomVectors.get(k).transpose().times(x).get(0, 0)), 2));
                    }
                    H.set(row, col, val1 + runningSum1);
                } else {
                    double runningSum2 = 0;
                    for (int k = 0; k < _numDimensions; k++) {
                        runningSum2 += (_randomVectors.get(k).get(row, 0) * _randomVectors.get(k).get(col, 0)) /
                                (Math.pow((1 - _randomVectors.get(k).transpose().times(x).get(0, 0)), 2));
                    }
                    H.set(row, col, runningSum2);
                }
            }
        }
        return H;
    }

    public static double objFunctionEvaluator(Matrix x) {
        double runningSum1 = 0.0;
        double runningSum2 = 0.0;
        for (int i = 0; i < _numDimensions; i++) {
            runningSum1 += (-1) * (Math.log(1 - Math.pow(x.get(i, 0), 2)));
        }
        for (int i = 0; i < _numDimensions; i++) {
            runningSum2 += ((-1) * (Math.log(Math.abs(1 - _randomVectors.get(i).transpose().times(x).get(0, 0)))));
        }
        return runningSum1 + runningSum2;
    }

    public static Matrix nextPointCalculator(Matrix prevVector, Matrix gradient, Matrix Hessian, double stepSize) {
        Matrix hInv = Hessian.inverse();
        //We are taking the gradient to be a row-vector with gradient.length columns and 1 row.
        Matrix nVector = prevVector.minus(hInv.times(gradient.transpose()).times(stepSize));
       return nVector;
    }

    /*Executes one iteration of the Optimization method in question(Newton's Method in this case).
    Step size is calculated using the backtracking parameters _alpha & _beta. (Hard-coded)*/
    public static Matrix oneIteration(Matrix startingVector) {
        Matrix myGradient = gradientCalculator(startingVector);
        Matrix myHessian = hessianCalculator(startingVector);
        double stepSize = 0.1;
        //Use stepsize = 0.1 to calculate new point.
        Matrix currVector = nextPointCalculator(startingVector, myGradient, myHessian, stepSize);
        while (objFunctionEvaluator(currVector) > objFunctionEvaluator(startingVector)) {
            stepSize *= 0.5;
            currVector = nextPointCalculator(startingVector, myGradient, myHessian, stepSize);
        }

        return currVector;


    }
}