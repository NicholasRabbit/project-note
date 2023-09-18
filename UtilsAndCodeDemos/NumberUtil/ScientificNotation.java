package com.test.num;

import java.text.DecimalFormat;

public class ScientificNotation {

    public static void main(String[] args) {

        double d = 0.000031d;
        System.out.println(d);

        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setMaximumFractionDigits(8);
        String format = decimalFormat.format(d);
        System.out.println(format);


    }
}
