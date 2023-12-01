import java.util.ArrayList;

public class BigWinOperators {
    private String apiUserName;
    private String operatorName;

    private int bigWinMinAmount;

    private ArrayList<String> operatorEmails = new ArrayList<>();

    public BigWinOperators(String apiUserName, String operatorName, ArrayList<String> operatorEmails, int bigWinMinAmount)
    {
        this.apiUserName = apiUserName;
        this.operatorName = operatorName;
        this.operatorEmails = operatorEmails;
        this.bigWinMinAmount = bigWinMinAmount;
    }

    public String getApiUserName() {
        return apiUserName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public int getBigWinMinAmount() {
        return bigWinMinAmount;
    }

    public ArrayList<String> getOperatorEmails() {
        return operatorEmails;
    }
}
