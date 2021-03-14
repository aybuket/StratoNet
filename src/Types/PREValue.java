package Types;

public enum PREValue {
    av,
    ct,
    mn,
    mx;

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
