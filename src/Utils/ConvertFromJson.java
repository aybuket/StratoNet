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
                case "copyright" -> apodResponse.setCopyright(value);
                case "date" -> apodResponse.setDate(value);
                case "explanation" -> apodResponse.setExplanation(value);
                case "hdurl" -> apodResponse.setHdurl(value);
                case "media_type" -> apodResponse.setMediaType(value);
                case "service_version" -> apodResponse.setServiceVersion(value);
                case "title" -> apodResponse.setTitle(value);
                case "url" -> apodResponse.setUrl(value);
            }
        }
        return apodResponse;
    }

    public static InsightWeather convertInsightWeatherResponse(HttpResponse<String> response) {
        JSONObject obj = (JSONObject) JSONValue.parse(response.body());
        InsightWeather insightWeatherResponse = new InsightWeather();
        for (Object key : obj.keySet()) {
            switch (key.toString()) {
                case "sol_keys" -> insightWeatherResponse.setSolKeys(JsonArrayToArray.convertToStringArray((JSONArray) obj.get(key)));
                case "validity_checks"-> insightWeatherResponse.setValidityCheck(extractValidityCheck((JSONObject) obj.get(key)));
                default -> insightWeatherResponse.addSol(extractSol(key, obj));
            }
        }
        return insightWeatherResponse;
    }

    private static Sol extractSol(Object key, JSONObject obj)
    {
        JSONObject value = (JSONObject) obj.get(key);
        JSONObject pre = (JSONObject) value.get("PRE");
        PREValue[] preValues = {PREValue.av, PREValue.ct, PREValue.mn, PREValue.mx};
        JSONObject wdMostCommon = (JSONObject) value.get("WD");

        if (pre != null) {
            preValues[0].setValue((double) pre.get("av"));
            preValues[1].setValue((double) (long) pre.get("ct"));
            preValues[2].setValue((double) pre.get("mn"));
            preValues[3].setValue((double) pre.get("mx"));
        }

        return new Sol(key.toString(),
                (String) value.get("First_UTC"),
                (String) value.get("Last_UTC"),
                (int) (long) value.get("Month_ordinal"),
                (String) value.get("Northern_season"),
                preValues,
                (String) value.get("Season"),
                (String) value.get("Southern_season"),
                (String) wdMostCommon.get("most_common"));
    }

    private static ValidityCheck extractValidityCheck(JSONObject obj) {
        ValidityCheck validityCheck = new ValidityCheck();
        for (Object key : obj.keySet()) {
            switch (key.toString()) {
                case "sol_hours_required" -> validityCheck.setSolHoursRequired((int) (long) obj.get(key));
                case "sols_checked" -> validityCheck.setSolsChecked(JsonArrayToArray.convertToStringArray((JSONArray) obj.get(key)));
                default -> validityCheck.addValiditySols(extractValiditySol(key, obj));
            }
        }
        return validityCheck;
    }

    private static ValiditySol extractValiditySol(Object key, JSONObject obj)
    {
        ValiditySol validitySol = new ValiditySol();
        validitySol.setSolKey(key.toString());
        JSONObject getPre = (JSONObject) ((JSONObject) obj.get(key)).get("PRE");
        if (getPre != null) {
            Object valid = getPre.get("valid");
            if (valid != null) {
                validitySol.setValid(Boolean.getBoolean(valid.toString()));
            }

            Object solHoursWithData = getPre.get("sol_hours_with_data");
            if (solHoursWithData != null) {
                validitySol.setSolHoursWithData(JsonArrayToArray.convertToStringArray((JSONArray) solHoursWithData));
            }
        }
        return validitySol;
    }
}