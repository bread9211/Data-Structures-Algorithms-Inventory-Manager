import java.sql.Date;
import java.time.LocalDate;

public class Item {
    private int SKU;
    private int instanceID;
    private int amount;
    private LocalDate acquiredDate;
    
    public Item(int ID, int num, LocalDate date){
        SKU = ID;
        amount = num;
        acquiredDate = date;
        instanceID = -1;
    }
    public Item(int num, LocalDate date){
        this(-1,num,date);
    }
    public void removeItem(int quantity){
        amount -= quantity;
    }
    public int getStock(){
        return amount;
    }   
    public int getSKU(){
        return SKU;
    }
    public int getID(){
        return instanceID;
    }
    public LocalDate getAcquired(){
        return acquiredDate;
    }
    public void setSKU(int ID){
        SKU = ID;
    }
    public void setID(int ID){
        instanceID = ID;
    }
    public boolean isPerishable(){
        return false;
    }
    public LocalDate getExpr(){
        return null;
    }
    public String toString(){
        return  "SKU: " + SKU + "   Stock: " + amount + "   Acquired: " + acquiredDate;
    }
}
