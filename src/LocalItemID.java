import java.lang.Math;
public class LocalItemID{
    private ItemID reference;
    private int stock;
    private int demand;
    private int dailySold;
    private float volatility;

    public LocalItemID(ItemID itemID, int stock, int demand){
        reference = itemID;
        this.stock = stock;
        this.demand = demand;
        dailySold = 0;
        volatility = 1;
    }
    public LocalItemID(ItemID itemID, int stock){
        reference = itemID;
        this.stock = stock;
        this.demand = stock;
        dailySold = 0;
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
    public void sell(int sold){
        dailySold += sold;
    }

    //Daily increment
    public void dateIncrement(){
        volatility = (2*volatility + dailySold/demand)/3;
        if(demand == 0)
            demand = dailySold;
        else
            demand = Math.round(dailySold*volatility);
        dailySold = 0;
    }

    public int predict(){
        return demand - stock;
    }
}