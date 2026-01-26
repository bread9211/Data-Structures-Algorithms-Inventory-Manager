public class ItemID {
    private String itemName;
    private int SKU;
    private int buyPrice;
    private int sellPrice;
    private String[] keywords;
    private int stock;
    private int demand;
    private int deliveryTime;
    private int expirationTime;
    
    public ItemID(String name, int ID, int price, int cost, String[] keywords, int deliveryTime){
        this(name,ID,price,cost,keywords,0,1,0);
    }
    public ItemID(String name, int ID, int price, int cost, String[] keywords, int stock, int deliveryTime, int expirationTime){
        itemName = name;
        SKU = ID;
        buyPrice = cost;
        sellPrice = price;
        this.keywords = keywords;
        this.stock = stock;
        this.deliveryTime = deliveryTime;
        this.expirationTime = expirationTime;
    }

    public int getID(){
        return SKU;
    }
    public String getName(){
        return itemName;
    }
    public int getBuyPrice(){
        return buyPrice;
    }
    public int getSellPrice(){
        return sellPrice;
    }
    public int getExpr(){
        return expirationTime;
    }
    public boolean checkKeyword(String kw){
        for(int i = 0; i < keywords.length; i++)
            if(keywords[i] == kw)
                return true;
        return false;
    }

    public void addStock(int change){
        stock += change;
    }

    //Changes the daily demand based on how many were purchased in a day
    public boolean purchasedInDay(int purchased){
        if(stock-purchased >= 0){
            demand = (demand * 2)/3 + (purchased+1)/3;
            stock -= purchased;
            return true;
        }else
            return false;
    }


    //Calculates amount of stock to buy to maintain enough for deliveryTime days of customers
    public int predict(){
        return ((demand * 4 +1)/3)*deliveryTime - stock;
    }
}
