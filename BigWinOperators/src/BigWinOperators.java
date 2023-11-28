public class BigWinOperators {
    private String apiUserName;
    private String operatorName;

    public BigWinOperators(String apiUserName, String operatorName)
    {
        this.apiUserName = apiUserName;
        this.operatorName = operatorName;
    }

    public String getApiUserName() {
        return apiUserName;
    }

    public String getOperatorName() {
        return operatorName;
    }
}
