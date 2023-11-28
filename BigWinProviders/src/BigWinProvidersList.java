import java.util.ArrayList;

public class BigWinProvidersList {
    private ArrayList<BigWinProviders> bigWinProvidersList = new ArrayList<>();

    public BigWinProvidersList (ArrayList<BigWinProviders> bigWinProvidersList)
    {
        this.bigWinProvidersList = bigWinProvidersList;
    }

    public ArrayList<BigWinProviders> getBigWinProvidersList() {
        return bigWinProvidersList;
    }

    public BigWinProviders getProviderByPrefix(String prefix)
    {
        for (int i=0; i<bigWinProvidersList.size(); i++) {
            if(bigWinProvidersList.get(i).getProviderPrefix().equals(prefix))
                return bigWinProvidersList.get(i);
       }
        return null;
    }

    public void addProvider(BigWinProviders provider)
    {
        bigWinProvidersList.add(provider);
    }

}
