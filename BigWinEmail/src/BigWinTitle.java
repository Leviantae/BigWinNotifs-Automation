import javax.print.attribute.standard.DateTimeAtCreation;
import java.util.Date;

public class BigWinTitle {
    private BigWinProviders provider;
    private int reevoID;
    private Date date;

    private BigWinOperators operator;

    public BigWinTitle (BigWinProviders provider, int reevoID, Date date, BigWinOperators operator)
    {
        this.provider = provider;
        this.reevoID = reevoID;
        this.date = date;
        this.operator = operator;
    }

    public String titleToString()
    {
        return "Big Win Check - " + this.reevoID + " - " + this.operator.getOperatorName() + " - " + this.provider.getProviderName() + " - " + this.date;
    }

}
