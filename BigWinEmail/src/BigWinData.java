import java.util.Date;

public class BigWinData {
    private String timeStamp;
    private String operator;
    private double amount;
    private String currency;
    private String gameTitle;
    private int reevoID;
    private String casinoPlayerID;
    private int providerPlayerID;

    public BigWinData (String timeStamp, String operator, double amount, String currency, String gameTitle, int reevoID, String casinoPlayerID, int providerPlayerID)
    {
        this.timeStamp = timeStamp;
        this.operator = operator;
        this.amount = amount;
        this.currency = currency;
        this.gameTitle = gameTitle;
        this.reevoID = reevoID;
        this.casinoPlayerID = casinoPlayerID;
        this.providerPlayerID = providerPlayerID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public int getReevoID() {
        return reevoID;
    }

    public String getCasinoPlayerID() {
        return casinoPlayerID;
    }

    public String getCurrency() {
        return currency;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getOperator() {
        return operator;
    }

    public int getProviderPlayerID() {
        return providerPlayerID;
    }
}
