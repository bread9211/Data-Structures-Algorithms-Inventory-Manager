import java.lang.Math;
public class LocalItemID{
    private ItemID reference;
    private int stock;
    private int demand;
    private int dailyPurchased;
    private float volatility;

    public LocalItemID(ItemID itemID, int stock, int demand){
        reference = itemID;
        this.stock = stock;
        this.demand = demand;
        dailyPurchased = 0;
        volatility = 1;
    }
    public LocalItemID(ItemID itemID, int stock){
        reference = itemID;
        this.stock = stock;
        this.demand = stock;
        dailyPurchased = 0;
        volatility = 1;
    }

    public ItemID getReference(){
        return reference;
    }

    public void addStock(int change){
        stock += change;
    }
    public int getStock(){
        return stock;
    }
    public void purchase(int purchased){
        dailyPurchased += purchased;
    }

    //Daily increment
    public void dateIncrement(){
        volatility = (2*volatility + dailyPurchased/demand)/3;
        if(demand == 0)
            demand = dailyPurchased;
        else
            demand = Math.round(dailyPurchased*volatility);
        dailyPurchased = 0;
    }

    public int predict(){
        return demand - stock;
    }
}