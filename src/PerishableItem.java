import java.time.LocalDate;

public class PerishableItem extends Item{
    private LocalDate exprDate;
    
    public PerishableItem(int SKU, int num, LocalDate pDate, LocalDate eDate){
        super(SKU,num,pDate);
        exprDate = eDate;
    }
    public PerishableItem(int num, LocalDate pDate, LocalDate eDate){
        super(num,pDate);
        exprDate = eDate;
    }
    
    public LocalDate getExpr(){
        return exprDate;
    }
    public boolean checkExpr(LocalDate current){
        return exprDate.isBefore(current);
    }
    public boolean isPerishable(){
        return true;
    }

    public String toString(){
        return super.toString() + "   Expires: " + exprDate;
    }
}
