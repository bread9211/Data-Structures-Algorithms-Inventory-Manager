import java.time.LocalDate;
public class Item {
    private int SKU;
    private int instanceID;
    private int amount;
    
    public Item(int ID, int num){
        SKU = ID;
        amount = num;
        instanceID = -1;
    }
    public Item(int num){
        this(-1,num);
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
        return  "SKU: " + SKU + "   Stock: " + amount;
    }
}
