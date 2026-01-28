import java.sql.Date;

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
    public boolean checkExpr(Date current){
        return exprDate.before(current);
    }

    public int purchase(int quantity, Date current){
        if(checkExpr(current)){
            return quantity;
        }else if(amount - quantity > 0){
            amount -= quantity;
            return -1;
        }else
            amount = 0;
            return quantity - amount;
    }
    public boolean isPerishable(){
        return true;
    }
}
