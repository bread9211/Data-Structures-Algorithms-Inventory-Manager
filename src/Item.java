import java.sql.Date;

public class Item {
    private int SKU;
    protected int amount;
    private Date purchaseDate;
    
    public Item(int ID, int num, Date date){
        SKU = ID;
        amount = num;
        purchaseDate = date;
    }
    public Item(int ID, Date date){
        this(ID,1,date);
    }
    
    public int removeItem(int quantity, Date current){
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
    public int getID(){
        return SKU;
    }
    public boolean isPerishable(){
        return false;
    }
}
