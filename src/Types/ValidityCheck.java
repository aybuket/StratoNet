package Types;

import java.util.ArrayList;

public class ValidityCheck {
    private int solHoursRequired;
    private ArrayList<ValiditySol> validitySols = new ArrayList<ValiditySol>();
    private String[] solsChecked = null;

    public int getSolHoursRequired() {
        return solHoursRequired;
    }

    public void setSolHoursRequired(int solHoursRequired) {
        this.solHoursRequired = solHoursRequired;
    }

    public String[] getSolsChecked() {
        return solsChecked;
    }

    public void setSolsChecked(String[] solsChecked) {
        this.solsChecked = solsChecked;
    }

    public ValiditySol[] getValiditySols() {
        return (ValiditySol[]) validitySols.toArray();
    }

    public void addValiditySols(ValiditySol validitySol) {
        this.validitySols.add(validitySol);
    }
}