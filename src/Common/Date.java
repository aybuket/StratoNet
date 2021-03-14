package Common;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day)
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String dateFormat()
    {
        return year+"-"+month+"-"+day;
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
}
