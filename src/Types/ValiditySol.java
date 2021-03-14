package Types;

public class ValiditySol {
    private String solKey = null;
    private String[] solHoursWithData = null;
    private boolean valid;

    public String getSolKey() {
        return solKey;
    }

    public void setSolKey(String solKey) {
        this.solKey = solKey;
    }

    public String[] getSolHoursWithData() {
        return solHoursWithData;
    }

    public void setSolHoursWithData(String[] solHoursWithData) {
        this.solHoursWithData = solHoursWithData;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}