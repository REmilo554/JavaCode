package com.example.geometryfigures.Figures;

public class Triangle {
    private final double firstSide;
    private final double secondSide;
    private final double thirdSide;

    public Triangle(double firstSide, double secondSide, double thirdSide) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.thirdSide = thirdSide;
    }

    public double getFirstSide() {
        return firstSide;
    }
    public double getSecondSide() {
        return secondSide;
    }
    public double getThirdSide() {
        return thirdSide;
    }

    public double getArea() {
        double semiperimeter = (firstSide + secondSide + thirdSide) / 2.0;

        return Math.sqrt(semiperimeter * (semiperimeter - firstSide) * (semiperimeter - secondSide) * (semiperimeter - thirdSide));
    }

    public double getPerimeter() {
        return firstSide + secondSide + thirdSide;
    }
}
