package Types;

public class Apod {

    private String copyright = null;
    private String date = null;
    private String explanation = null;
    private String hdurl = null;
    private String mediaType = null;
    private String serviceVersion = null;
    private String title = null;
    private String url = null;

    public String getCopyright()
    {
        return copyright;
    }

    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getExplanation()
    {
        return explanation;
    }

    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }

    public String getHdurl()
    {
        return hdurl;
    }

    public void setHdurl(String hdurl)
    {
        this.hdurl = hdurl;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    public String getServiceVersion()
    {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion)
    {
        this.serviceVersion = serviceVersion;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        String result = "Copyright: "+getCopyright()+"\n"+
                "Date: "+getDate()+"\n"+
                "Explanation: "+getExplanation()+"\n"+
                "HDURL: "+getHdurl()+"\n"+
                "Media Type: "+getMediaType()+"\n"+
                "Service Version: "+getServiceVersion()+"\n"+
                "Title: "+getTitle()+"\n"+
                "URL: "+getUrl()+"\n";
        return result;
    }
}
