public class ItemID {
    private String itemName;
    private int SKU;
    private int buyPrice;
    private int sellPrice;
    private String[] keywords;
    private int stock;
    private int demand;
//    private int pending;
//    private int deliveryTime;
    
    public ItemID(String name, int ID, int price, int cost, String[] keywords){
        this(name,ID,price,cost,keywords,0,0);
    }
    public ItemID(String name, int ID, int price, int cost, String[] keywords, int stock, int demand){
        itemName = name;
        SKU = ID;
        buyPrice = cost;
        sellPrice = price;
        this.keywords = keywords;
        this.stock = stock;
        this.demand = demand;
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
    public bool checkKeyword(String kw){
        for(int i = 0; i < keywords.length; i++)
            if(keywords[i] == kw)
                return true;
        return false;
    }
    public boolean changeStock(int change){
        if(stock + change >= 0){
            stock += change;
            return true;
        }else{
            stock = 0;
            return false;
        }
    }

    //change to add pending later
    public int predict(){
        return demand - stock;
        //return (demand * deliveryTime) - (stock + pending) + demand/2;
    }
}
