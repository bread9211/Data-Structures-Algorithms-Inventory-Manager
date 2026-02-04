import java.sql.Date;
import java.time.LocalDate;

public class PerishableItem extends Item{
    private Date exprDate;
    
    public PerishableItem(int SKU, int num, Date pDate, Date eDate){
        super(SKU,num,pDate);
        exprDate = eDate;
    }
    public PerishableItem(int num, Date pDate, Date eDate){
        super(num,pDate);
        exprDate = eDate;
    }

    public Date getExpr(){
        return exprDate;
    }
    public boolean checkExpr(){
        return false;
    }
    public boolean isPerishable(){
        return true;
    }

    public String toString(){
        return super.toString() + "   Expires: " + exprDate;
    }
}
