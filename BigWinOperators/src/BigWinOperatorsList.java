import java.util.ArrayList;

public class BigWinOperatorsList {
    private ArrayList<BigWinOperators> bigWinOperatorsList = new ArrayList<>();

    public BigWinOperatorsList (ArrayList<BigWinOperators> bigWinOperatorsList)
    {
        this.bigWinOperatorsList = bigWinOperatorsList;
    }

    public BigWinOperators getOperatorByAPIUser(String userName)
    {
        for (int i=0; i<bigWinOperatorsList.size(); i++)
        {
            if (bigWinOperatorsList.get(i).getApiUserName().equals(userName))
                return bigWinOperatorsList.get(i);
        }
        return null;
    }

    public void addOperator(BigWinOperators operator)
    {
        bigWinOperatorsList.add(operator);
    }
}
