public class Item {
    private int SKU;
    private int amount;
    
    public Item(int ID, int num){
        SKU = ID;
        amount = num;
    }
    public Item(int ID){
        this(ID,1);
    }
    
    public int purchase(int purchased, Date current){
        if(amount - purchased > 0){
            amount -= purchased;
            return -1;
        }else
            amount = 0;
            return purchaed - amount;
    }
    public int getStock(){
        return amount;
    }
    public int getID(){
        return SKU;
    }
}
