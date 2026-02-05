import java.sql.Date;

public class Item {
    private int SKU;
    private int amount;
    private Date acquiredDate;
    
    public Item(int ID, int num, Date date){
        SKU = ID;
        amount = num;
        acquiredDate = date;
    }
    public Item(int num, Date date){
        this(-1,num,date);
    }
    public int removeItem(int quantity){
        if(amount - quantity > 0){
            amount -= quantity;
            return -1;
        }else
            amount = 0;
            return quantity - amount;
    }
    public int getStock(){
        return amount;
    }   
    public int getSKU(){
        return SKU;
    }
    public Date getAcquired(){
        return acquiredDate;
    }
    public void setSKU(int ID){
        SKU = ID;
    }
    public boolean isPerishable(){
        return false;
    }
    public Date getExpr(){
        return null;
    }
    public String toString(){
        return  "SKU: " + SKU + "   Stock: " + amount + "   Acquired: " + acquiredDate;
    }
}
