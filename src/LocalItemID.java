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
        this.demand = stock/itemID.getDelTime();
        dailyPurchased = 0;
        volatility = 1;
    }

    public void addStock(int change){
        stock += change;
    }

    //Changes the daily demand based on how many were purchased in a day
    public int purchase(int purchased){
        if(stock - purchased > 0){
            stock -= purchased;
            return -1;
        }else if(stock == purchased){
            stock = 0;
            return 0;
        }else
            return purchased-stock;
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

    //Calculates amount of stock to buy to maintain enough for deliveryTime days of customers
    public int predict(){
        return ((demand * 4 +1)/3)* reference.getDelTime() - stock;
    }
}