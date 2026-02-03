import java.sql.Date;

public class Item {
    private int SKU;
    private int instanceID;
    protected int amount;
    private Date acquiredDate;
    
    public Item(int ID, int num, Date date){
        SKU = ID;
        amount = num;
        acquiredDate = date;
        instanceID = 0;
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
    public int getInstance(){
        return instanceID;
    }
    public void setInstance(int newInstance){
        instanceID = newInstance;
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
        return  "Instance ID: " + instanceID + "   SKU: " + SKU + "   Stock: " + amount + "   Acquired: " + acquiredDate;
    }
}
