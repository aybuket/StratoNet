package Types;

import java.io.Serializable;

public class PREValue implements Serializable {
    private double av;
    private double ct;
    private double mn;
    private double mx;
    private String key;

    public double getAv() {
        return av;
    }

    public void setAv(double av) {
        this.av = av;
    }

    public double getCt() {
        return ct;
    }

    public void setCt(double ct) {
        this.ct = ct;
    }

    public double getMn() {
        return mn;
    }

    public void setMn(double mn) {
        this.mn = mn;
    }

    public double getMx() {
        return mx;
    }

    public void setMx(double mx) {
        this.mx = mx;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

    @Override
    public String toString() {
        return "PREValue{" +
                "av=" + av +
                ", ct=" + ct +
                ", mn=" + mn +
                ", mx=" + mx +
                '}';
    }
}
