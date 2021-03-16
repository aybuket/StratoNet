package Common;

public class Authentication {

    private ApplicationType type;
    private static final int failureLimit = 3;
    private int failureCount = 0;

    public Authentication(ApplicationType type)
    {
        this.type = type;
    }
}
