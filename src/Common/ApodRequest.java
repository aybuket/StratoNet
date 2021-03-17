package Common;

public class ApodRequest extends Request{
    private Date date = null;
    private Date startDate = null;
    private Date endDate = null;
    private int count;

    public ApodRequest() {
        super(RequestType.APOD);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean hasDate()
    {
        return date != null;
    }

    public boolean hasStartDate()
    {
        return startDate != null;
    }

    public boolean hasEndDate()
    {
        return endDate != null;
    }

    public boolean hasCount()
    {
        return count != 0;
    }

    @Override
    public String toString() {
        return "ApodRequest{" +
                "date=" + date +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", count=" + count +
                '}';
    }
}
