public class ItemID {
    private String itemName;
    private int SKU;
    private int buyPrice;
    private int sellPrice;
    private String[] keywords;
    private int deliveryTime;
    private int expirationTime;
    
    public ItemID(String name, int ID, int price, int cost, String[] keywords, int deliveryTime){
        this(name,ID,price,cost,keywords,1,0);
    }
    public ItemID(String name, int ID, int price, int cost, String[] keywords, int deliveryTime, int expirationTime){
        itemName = name;
        SKU = ID;
        buyPrice = cost;
        sellPrice = price;
        this.keywords = keywords;
        this.deliveryTime = deliveryTime;
        this.expirationTime = expirationTime;
    }

    public void setSKU(int ID){
        SKU = ID;
    }
    public int getSKU(){
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
    public int getDelTime(){
        return deliveryTime;
    }
    public boolean checkKeyword(String kw){
        for(int i = 0; i < keywords.length; i++)
            if(keywords[i].equals(kw))
                return true;
        return false;
    }
}
