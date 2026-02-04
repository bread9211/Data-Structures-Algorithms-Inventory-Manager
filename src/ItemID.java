public class ItemID {
    private String itemName;
    private int SKU;
    private int buyPrice;
    private int sellPrice;
    private String[] keywords;
    
    public ItemID(String name, int ID, int price, int cost, String[] keywords){
        itemName = name;
        SKU = ID;
        buyPrice = cost;
        sellPrice = price;
        this.keywords = keywords;
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
    public boolean checkKeyword(String kw){
        for(int i = 0; i < keywords.length; i++)
            if(keywords[i].equals(kw))
                return true;
        return false;
    }
}
