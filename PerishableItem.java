import java.sql.Date;

public class PerishableItem extends Item{
    private Date exprDate;
    
    public PerishableItem(int SKU, int num, Date date){
        super(SKU,num);
        exprDate = date;
    }
    public PerishableItem(int SKU, Date date){
        super(SKU);
        exprDate = date;
    }

    public Date getExpr(){
        return exprDate();
    }
    public bool checkExpr(Date current){
        return exprDate.before(current);
    }

    public int purchase(int purchased, Date current){
        if(checkExpr(current)){
            return purchased;
        }else if(amount - purchased > 0){
            amount -= purchased;
            return -1;
        }else
            amount = 0;
            return purchaed - amount;
    }

}
