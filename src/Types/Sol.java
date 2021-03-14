package Types;

public class Sol {
    private String key = null;
    private String firstUTC = null;
    private String lastUTC = null;
    private int monthOrdinal = 0;
    private String northernSeason = null;
    private PREValue[] pre = null;
    private String season = null;
    private String southernSeason = null;
    private String wdMostCommon = null;

    public Sol(String key,
               String firstUTC,
               String lastUTC,
               int monthOrdinal,
               String northernSeason,
               PREValue[] pre,
               String season,
               String southernSeason,
               String wdMostCommon) {
        this.key = key;
        this.firstUTC = firstUTC;
        this.lastUTC = lastUTC;
        this.monthOrdinal = monthOrdinal;
        this.northernSeason = northernSeason;
        this.pre = pre;
        this.season = season;
        this.southernSeason = southernSeason;
        this.wdMostCommon = wdMostCommon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstUTC() {
        return firstUTC;
    }

    public void setFirstUTC(String firstUTC) {
        this.firstUTC = firstUTC;
    }

    public String getLastUTC() {
        return lastUTC;
    }

    public void setLastUTC(String lastUTC) {
        this.lastUTC = lastUTC;
    }

    public int getMonthOrdinal() {
        return monthOrdinal;
    }

    public void setMonthOrdinal(int monthOrdinal) {
        this.monthOrdinal = monthOrdinal;
    }

    public String getNorthernSeason() {
        return northernSeason;
    }

    public void setNorthernSeason(String northernSeason) {
        this.northernSeason = northernSeason;
    }

    public PREValue[] getPre() {
        return pre;
    }

    public void setPre(PREValue[] pre) {
        this.pre = pre;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSouthernSeason() {
        return southernSeason;
    }

    public void setSouthernSeason(String southernSeason) {
        this.southernSeason = southernSeason;
    }

    public String getWdMostCommon() {
        return wdMostCommon;
    }

    public void setWdMostCommon(String wdMostCommon) {
        this.wdMostCommon = wdMostCommon;
    }
}
