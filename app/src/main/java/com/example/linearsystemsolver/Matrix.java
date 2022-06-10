package com.example.linearsystemsolver;

import java.util.List;

public class Matrix{

    private final List<ComplexNumber> elements;
    private final List<ComplexNumber> constantsRow;

    public Matrix(List<ComplexNumber> elements, List<ComplexNumber> constantsRow) {
        this.elements = elements;
        this.constantsRow = constantsRow;
    }

    private ComplexNumber[][] setupMatrix() {
        int index = 0;
        ComplexNumber[][] matrix = new ComplexNumber[3][5];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (j<3)
                    matrix[i][j] = elements.get(index++);
                else
                    matrix[i][j] = elements.get(index-(6-j));
            }
        }
        return matrix;
    }

    private ComplexNumber get3x3Determinant(ComplexNumber[][] matrix){
        return getPositiveResult(matrix).subtract(getNegativeResult(matrix));
    }

    private ComplexNumber getPositiveResult(ComplexNumber[][] matrix) {
        ComplexNumber result = new ComplexNumber(0, 0);
        ComplexNumber multiplier = new ComplexNumber(1, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                multiplier = multiplier.multiply(matrix[j][i+j]);
            }
            result = result.add(multiplier);
            multiplier = new ComplexNumber(1, 0);
        }
        return result;
    }

    private ComplexNumber getNegativeResult(ComplexNumber[][] matrix){
        ComplexNumber result = new ComplexNumber(0, 0);
        ComplexNumber multiplier = new ComplexNumber(1, 0);
        for (int i = 4; i > 1; i--) {
            for (int j = 0; j < 3; j++) {
                multiplier = multiplier.multiply(matrix[j][i-j]);
            }
            result = result.add(multiplier);
            multiplier = new ComplexNumber(1, 0);
        }
        return result;
    }

    private ComplexNumber[][] replaceNthColumn(int column){
        ComplexNumber[][] resultantMatrix = setupMatrix();
        if (column != 2) {
            for (int i = 0; i < 3; i++) {
                resultantMatrix[i][column] = constantsRow.get(i);
                resultantMatrix[i][column + 3] = constantsRow.get(i);
            }
        }else{
            for (int i=0; i<3; i++){
                resultantMatrix[i][column] = constantsRow.get(i);
            }
        }
        return resultantMatrix;
    }

    public ComplexNumber[] solveSystemOfEquation() throws ArithmeticException{
        // if the determinant is zero we throw the ArithmeticException
        ComplexNumber[] results = new ComplexNumber[3];
        ComplexNumber determinant = get3x3Determinant(setupMatrix());
        ComplexNumber determinantX = get3x3Determinant(replaceNthColumn(0));
        ComplexNumber determinantY = get3x3Determinant(replaceNthColumn(1));
        ComplexNumber determinantZ = get3x3Determinant(replaceNthColumn(2));
        results[0] = determinantX.divide(determinant);
        results[1] = determinantY.divide(determinant);
        results[2] = determinantZ.divide(determinant);
        return results;
    }
}
