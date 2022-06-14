package com.example.linearsystemsolver;

public class ComplexNumber {

    private final double realPart;
    private final double imaginaryPart;

    public ComplexNumber(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
        
    }

    public ComplexNumber(String s) throws NumberFormatException {
        int index = -1;
        // if it contains imaginary part
        if (s.contains("j")) {
            // we determine the imaginary part only
            for (int i = s.length() - 1; i >= 0; i--) {
                if (s.charAt(i) == '-' || s.charAt(i) == '+') {
                    index = i;
                    break;
                }
            }
            // if the s contains only imaginary part we set real to zero
            if (index == 0 || index == -1) {
                this.realPart = 0;
                if (s.replace("j", "").equals(""))
                    this.imaginaryPart = 1;
                else if (s.replace("j", "").equals("-"))
                    this.imaginaryPart = -1;
                else
                    this.imaginaryPart = Double.parseDouble(s.replace("j", ""));
            } else {
                // if contains real and imaginary
                this.realPart = Double.parseDouble(s.substring(0, index));
                this.imaginaryPart = Double.parseDouble(s.substring(index, s.length() - 1));
            }
        } else {
            // if it contains only real part
            this.imaginaryPart = 0;
            this.realPart = Double.parseDouble(s);
        }
    }

    public ComplexNumber getConj() {
        return new ComplexNumber(this.realPart, -this.imaginaryPart);
    }

    public ComplexNumber add(ComplexNumber number) {
        return new ComplexNumber((this.realPart + number.realPart), (this.imaginaryPart + number.imaginaryPart));
    }

    public ComplexNumber subtract(ComplexNumber number) {
        return new ComplexNumber((this.realPart - number.realPart), (this.imaginaryPart - number.imaginaryPart));
    }

    public ComplexNumber multiply(ComplexNumber number) {
        double real = this.realPart * number.realPart - this.imaginaryPart * number.imaginaryPart;
        double imaginary = this.realPart * number.imaginaryPart + this.imaginaryPart * number.realPart;
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber divide(ComplexNumber number) throws ArithmeticException {
        double denominator = (number.multiply(number.getConj())).realPart;
        return (this.multiply(number.getConj())).divide(denominator);
    }

    public ComplexNumber divide(double number) throws ArithmeticException {
        return new ComplexNumber(this.realPart / number, this.imaginaryPart / number);
    }

    @Override
    public String toString() {
        if (imaginaryPart != 0) {
            String s = this.imaginaryPart < 0 ? " - " + String.format("%.5f", -this.imaginaryPart) : " + " + String.format("%.5f", this.imaginaryPart);
            return String.format("%.5f", this.realPart) + s + "i";
        }
        return String.format("%.5f", realPart);
    }
}
