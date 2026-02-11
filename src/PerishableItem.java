import java.time.LocalDate;

public class PerishableItem extends Item{
    private LocalDate exprDate;
    
    public PerishableItem(int SKU, int num, LocalDate exprDate){
        super(SKU,num);
        exprDate = exprDate;
    }
    public PerishableItem(int num, LocalDate pDate, LocalDate eDate){
        super(num);
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
