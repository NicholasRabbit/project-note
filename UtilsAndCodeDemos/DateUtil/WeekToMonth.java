package com.test.date;

import java.util.Calendar;
import java.util.Date;

public class WeekToMonth {

    public static void main(String[] args) {
        // Test with some examples
        System.out.println(getMonthFromWeek(1, 2023)); // January
        System.out.println(getMonthFromWeek(5, 2022)); // February
        System.out.println(getMonthFromWeek(10, 2023)); // March
        System.out.println(getMonthFromWeek(20, 2023)); // May
        System.out.println(getMonthFromWeek(52, 2023)); // December
    }

    // A method that takes a week number and a year and returns the month name
    public static String getMonthFromWeek(int week, int year) {
        // Create a calendar instance and set the week and year
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.YEAR, year);

        // Get the first day of the week as a date object
        Date date = cal.getTime();

        // Get the month number from the date (0 for January, 11 for December)
        int month = date.getMonth();

        // Return the month name based on the month number
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Invalid month";
        }
    }
}