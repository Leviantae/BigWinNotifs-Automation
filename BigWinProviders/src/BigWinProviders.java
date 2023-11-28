public class BigWinProviders {
    private String providerPrefix;
    private String providerName;

    public BigWinProviders (String providerPrefix, String providerName)
    {
        this.providerPrefix = providerPrefix;
        this.providerName = providerName;
    }

    public String getProviderPrefix()
    {
        return this.providerPrefix;
    }

    public String getProviderName()
    {
        return this.providerName;
    }
}
