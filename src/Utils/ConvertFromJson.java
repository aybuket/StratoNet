package Utils;

import Types.*;
import org.json.simple.*;

import java.net.http.HttpResponse;

public class ConvertFromJson {

    public static Apod convertApodResponse(HttpResponse<String> response) {
        String[] splitted = response.body()
                .replace("{", "")
                .replace("}", "")
                .split("\",\"");
        Apod apodResponse = new Apod();
        for (String line : splitted) {
            String[] parts = line.split("\":\"");
            String key = parts[0].trim().replace("\"", "");
            String value = parts[1].trim().replace("\"", "");
            switch (key) {
                case "copyright":
                    apodResponse.setCopyright(value);
                    break;
                case "date":
                    apodResponse.setDate(value);
                    break;
                case "explanation":
                    apodResponse.setExplanation(value);
                    break;
                case "hdurl":
                    apodResponse.setHdurl(value);
                    break;
                case "media_type":
                    apodResponse.setMediaType(value);
                    break;
                case "service_version":
                    apodResponse.setServiceVersion(value);
                    break;
                case "title":
                    apodResponse.setTitle(value);
                    break;
                case "url":
                    apodResponse.setUrl(value);
                    break;
            }
        }
        return apodResponse;
    }

    public static InsightWeather convertInsightWeatherResponse(HttpResponse<String> response) {
        JSONObject obj = (JSONObject) JSONValue.parse(response.body());
        InsightWeather insightWeatherResponse = new InsightWeather();
        for (Object key : obj.keySet()) {
            switch (key.toString()) {
                case "sol_keys":
                    insightWeatherResponse.setSolKeys(JsonArrayToArray.convertToStringArray((JSONArray) obj.get(key)));
                    break;
                case "validity_checks":
                    insightWeatherResponse.setValidityCheck(extractValidityCheck((JSONObject) obj.get(key)));
                    break;
                default:
                    JSONObject value = (JSONObject) obj.get(key);
                    JSONObject pre = (JSONObject) value.getOrDefault("PRE", null);
                    PREValue[] preValues = {PREValue.av, PREValue.ct, PREValue.mn, PREValue.mx};
                    JSONObject wdMostCommon = (JSONObject) value.get("WD");

                    if (pre != null)
                    {
                        preValues[0].setValue((double)pre.getOrDefault("av",null));
                        preValues[1].setValue((double)(long)pre.getOrDefault("ct",null));
                        preValues[2].setValue((double)pre.getOrDefault("mn",null));
                        preValues[3].setValue((double)pre.getOrDefault("mx",null));
                    }

                    Sol sol = new Sol(key.toString(),
                            (String)value.getOrDefault("First_UTC", null),
                            (String)value.getOrDefault("Last_UTC", null),
                            (int)(long)value.getOrDefault("Month_ordinal", null),
                            (String)value.getOrDefault("Northern_season", null),
                            preValues,
                            (String)value.getOrDefault("Season", null),
                            (String)value.getOrDefault("Southern_season", null),
                            (String)wdMostCommon.getOrDefault("most_common", null));

                    insightWeatherResponse.addSol(sol);
            }
        }
        return insightWeatherResponse;
    }

    private static ValidityCheck extractValidityCheck(JSONObject obj) {
        ValidityCheck validityCheck = new ValidityCheck();
        for (Object key : obj.keySet()) {
            switch (key.toString()) {
                case "sol_hours_required":
                    validityCheck.setSolHoursRequired((int)(long)obj.get(key));
                    break;
                case "sols_checked":
                    validityCheck.setSolsChecked(JsonArrayToArray.convertToStringArray((JSONArray) obj.get(key)));
                    break;
                default:
                    ValiditySol validitySol = new ValiditySol();
                    validitySol.setSolKey(key.toString());
                    JSONObject getPre = (JSONObject) ((JSONObject) obj.get(key)).getOrDefault("PRE", null);
                    if (getPre != null) {
                        Object valid = getPre.getOrDefault("valid", null);
                        if (valid != null) {
                            validitySol.setValid(Boolean.getBoolean(valid.toString()));
                        }

                        Object solHoursWithData = getPre.getOrDefault("sol_hours_with_data", null);
                        if (solHoursWithData != null) {
                            validitySol.setSolHoursWithData(JsonArrayToArray.convertToStringArray((JSONArray) solHoursWithData));
                        }
                    }
            }
        }
        return validityCheck;
    }
}