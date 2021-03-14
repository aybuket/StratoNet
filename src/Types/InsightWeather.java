package Types;

import java.util.HashMap;
import java.util.Map;

public class InsightWeather {
    private String[] solKeys = null;
    private Map<String,Sol> sols = new HashMap<String, Sol>();
    private ValidityCheck validityCheck = null;

    public String[] getSolKeys() {
        return solKeys;
    }

    public void setSolKeys(String[] solKeys) {
        this.solKeys = solKeys;
    }

    public Map<String,Sol> getSols() {
        return sols;
    }

    public void addSol(Sol sol) {
        this.sols.put(sol.getKey(), sol);
    }

    public ValidityCheck getValidityCheck() {
        return validityCheck;
    }

    public void setValidityCheck(ValidityCheck validityCheck) {
        this.validityCheck = validityCheck;
    }
}
