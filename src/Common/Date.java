package Common;

import java.io.Serializable;

public class Date implements Serializable {
    private int year;
    private int month;
    private int day;
    private static final Date firstPictureDay = new Date(1995,6,16);

    public Date(int year, int month, int day)
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(){

    }

    public static Date getFirstPictureDay()
    {
        return firstPictureDay;
    }

    public String dateFormat()
    {
        String formattedDate = year+"-";
        formattedDate += (month < 10) ? "0" : "";
        formattedDate += month + "-";
        formattedDate += (day < 10) ? "0" : "";
        formattedDate += day;
        return formattedDate;
    }

    public boolean validateDate(Date minExpected)
    {
        if (minExpected.getYear() > this.getYear())
        {
            return false;
        }
        else if (minExpected.getYear() < this.getYear())
        {
            return true;
        }

        if (minExpected.getMonth() > this.getMonth())
        {
            return false;
        }
        else if (minExpected.getMonth() < this.getMonth())
        {
            return true;
        }

        if (minExpected.getDay() > this.getDay())
        {
            return false;
        }

        return true;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Date{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
