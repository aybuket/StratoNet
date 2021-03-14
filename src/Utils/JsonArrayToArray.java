package Utils;

import org.json.simple.JSONArray;

public class JsonArrayToArray {

    public static String[] convertToStringArray(JSONArray jsonArray)
    {
        String[] stringArray = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            stringArray[i] = jsonArray.get(i).toString();
        }

        return stringArray;
    }

}
