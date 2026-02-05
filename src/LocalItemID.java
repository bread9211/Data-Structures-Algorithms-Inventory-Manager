import java.lang.Math;
import java.time.LocalDate;
public class LocalItemID{
    private ItemID reference;
    private int stock;
    private float demand;
    private int dailyPurchased;
    private float volatility;
    private LocalDate lastAction;

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

    public void changeStock(int change){
        stock += change;
    }
    public void purchase(int purchased){
        stock -= purchased;
    }

    public int getStock(){
        return stock;
    }

    //Daily increment
    public void dateIncrement(){
        volatility = dailyPurchased/demand;
        if(demand == 0)
            demand = dailyPurchased;
        else
            demand = Math.round(dailyPurchased*volatility);
        dailyPurchased = 0;
    }

    public int predict(){
        return Math.round(demand - stock);
    }
}