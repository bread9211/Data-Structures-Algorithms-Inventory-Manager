import java.time.LocalDate;

public class DemoTester {
    public static void main(String[] args) {
        // Create WarehouseManager
        WarehouseManager manager = new WarehouseManager();
        
        // Add 5 warehouses
        manager.addWarehouse("East Coast Hub");
        manager.addWarehouse("Central Distribution");
        manager.addWarehouse("West Coast Center");
        manager.addWarehouse("Southern Logistics");
        manager.addWarehouse("Northern Storage");
        
        // Get warehouse references
        Warehouse east = manager.getWarehouse("East Coast Hub");
        Warehouse central = manager.getWarehouse("Central Distribution");
        Warehouse west = manager.getWarehouse("West Coast Center");
        Warehouse south = manager.getWarehouse("Southern Logistics");
        Warehouse north = manager.getWarehouse("Northern Storage");
        
        // Create the GUI with WarehouseManager
        UserGUI gui = new UserGUI(manager);
        
        // Add warehouses to the dropdown
        gui.addWarehouse("East Coast Hub", east);
        gui.addWarehouse("Central Distribution", central);
        gui.addWarehouse("West Coast Center", west);
        gui.addWarehouse("Southern Logistics", south);
        gui.addWarehouse("Northern Storage", north);
        
        // Populate each warehouse with ~100 items
        populateEastCoast(manager, east);
        populateCentral(manager, central);
        populateWest(manager, west);
        populateSouth(manager, south);
        populateNorth(manager, north);
        
        // Refresh the inventory display
        gui.refreshInventory();
    }
    
    private static void populateEastCoast(WarehouseManager manager, Warehouse warehouse) {
        manager.setCurrent("East Coast Hub");
        
        // Electronics (20 items)
        addItem(manager, warehouse, "Laptop Computer", 1000, 800, 500, new String[]{"electronics", "computer"}, 45, null);
        addItem(manager, warehouse, "USB-C Cable", 1001, 15, 8, new String[]{"electronics", "cable"}, 100, null);
        addItem(manager, warehouse, "Wireless Mouse", 1002, 25, 15, new String[]{"electronics", "computer"}, 80, null);
        addItem(manager, warehouse, "Mechanical Keyboard", 1003, 85, 50, new String[]{"electronics", "keyboard"}, 35, null);
        addItem(manager, warehouse, "Monitor 27in", 1004, 250, 180, new String[]{"electronics", "monitor"}, 25, null);
        addItem(manager, warehouse, "Webcam 1080p", 1005, 60, 35, new String[]{"electronics", "camera"}, 40, null);
        addItem(manager, warehouse, "USB Hub 7-Port", 1006, 40, 22, new String[]{"electronics", "hub"}, 60, null);
        addItem(manager, warehouse, "Phone Charger", 1007, 18, 8, new String[]{"electronics", "charger"}, 150, null);
        addItem(manager, warehouse, "Headphones Wireless", 1008, 120, 75, new String[]{"electronics", "audio"}, 30, null);
        addItem(manager, warehouse, "External SSD 1TB", 1009, 130, 90, new String[]{"electronics", "storage"}, 35, null);
        addItem(manager, warehouse, "Desk Lamp LED", 1010, 35, 20, new String[]{"electronics", "lighting"}, 55, null);
        addItem(manager, warehouse, "Power Strip", 1011, 22, 12, new String[]{"electronics", "power"}, 85, null);
        addItem(manager, warehouse, "HDMI Cable", 1012, 12, 6, new String[]{"electronics", "cable"}, 200, null);
        addItem(manager, warehouse, "Tablet Stand", 1013, 28, 15, new String[]{"electronics", "stand"}, 50, null);
        addItem(manager, warehouse, "Bluetooth Speaker", 1014, 65, 40, new String[]{"electronics", "audio"}, 40, null);
        addItem(manager, warehouse, "Phone Case Protective", 1015, 18, 8, new String[]{"electronics", "protection"}, 120, null);
        addItem(manager, warehouse, "Screen Protector Glass", 1016, 8, 3, new String[]{"electronics", "protection"}, 180, null);
        addItem(manager, warehouse, "USB Flash Drive 64GB", 1017, 22, 12, new String[]{"electronics", "storage"}, 65, null);
        addItem(manager, warehouse, "Laptop Cooling Pad", 1018, 35, 20, new String[]{"electronics", "cooling"}, 45, null);
        addItem(manager, warehouse, "Cable Organizer Kit", 1019, 15, 8, new String[]{"electronics", "organization"}, 70, null);
        
        // Furniture (20 items)
        addItem(manager, warehouse, "Office Chair Ergonomic", 2000, 250, 150, new String[]{"furniture", "chair"}, 10, null);
        addItem(manager, warehouse, "Standing Desk 60in", 2001, 350, 220, new String[]{"furniture", "desk"}, 6, null);
        addItem(manager, warehouse, "Desk Organizer Wooden", 2002, 45, 25, new String[]{"furniture", "organization"}, 30, null);
        addItem(manager, warehouse, "Bookshelf 5-Shelf", 2003, 120, 70, new String[]{"furniture", "storage"}, 15, null);
        addItem(manager, warehouse, "File Cabinet Metal", 2004, 180, 110, new String[]{"furniture", "storage"}, 9, null);
        addItem(manager, warehouse, "Conference Table", 2005, 600, 400, new String[]{"furniture", "table"}, 2, null);
        addItem(manager, warehouse, "Visitor Chair", 2006, 150, 90, new String[]{"furniture", "chair"}, 15, null);
        addItem(manager, warehouse, "Desk Lamp Metal", 2007, 55, 30, new String[]{"furniture", "lighting"}, 25, null);
        addItem(manager, warehouse, "Monitor Arm Mount", 2008, 65, 40, new String[]{"furniture", "mounting"}, 35, null);
        addItem(manager, warehouse, "Desk Pad Large", 2009, 35, 18, new String[]{"furniture", "accessories"}, 40, null);
        addItem(manager, warehouse, "Shelving Unit Metal", 2010, 280, 180, new String[]{"furniture", "storage"}, 8, null);
        addItem(manager, warehouse, "Keyboard Tray", 2011, 45, 25, new String[]{"furniture", "accessories"}, 32, null);
        addItem(manager, warehouse, "Under Desk Storage", 2012, 95, 55, new String[]{"furniture", "storage"}, 18, null);
        addItem(manager, warehouse, "Desk Hutch Organizer", 2013, 85, 50, new String[]{"furniture", "organization"}, 22, null);
        addItem(manager, warehouse, "Chair Mat Floor Protector", 2014, 40, 22, new String[]{"furniture", "protection"}, 35, null);
        addItem(manager, warehouse, "Trash Bin Stainless", 2015, 35, 18, new String[]{"furniture", "accessories"}, 35, null);
        addItem(manager, warehouse, "Coat Rack Metal", 2016, 55, 32, new String[]{"furniture", "storage"}, 18, null);
        addItem(manager, warehouse, "Wall Shelf Floating", 2017, 45, 25, new String[]{"furniture", "storage"}, 28, null);
        addItem(manager, warehouse, "Cable Management Box", 2018, 28, 15, new String[]{"furniture", "organization"}, 50, null);
        addItem(manager, warehouse, "Monitor Riser Stand", 2019, 50, 28, new String[]{"furniture", "mounting"}, 40, null);
        
        // Office Supplies (20 items)
        addItem(manager, warehouse, "A4 Paper Ream 500 sheets", 3000, 6, 3, new String[]{"office", "paper"}, 200, null);
        addItem(manager, warehouse, "Ballpoint Pen Box 50", 3001, 8, 4, new String[]{"office", "pen"}, 100, null);
        addItem(manager, warehouse, "Pencil HB Box 12", 3002, 4, 2, new String[]{"office", "pencil"}, 140, null);
        addItem(manager, warehouse, "Notebook Lined 100 sheet", 3003, 5, 2, new String[]{"office", "notebook"}, 125, null);
        addItem(manager, warehouse, "Marker Set 24 Colors", 3004, 12, 6, new String[]{"office", "markers"}, 55, null);
        addItem(manager, warehouse, "Highlighter Pack 5", 3005, 4, 2, new String[]{"office", "highlighter"}, 85, null);
        addItem(manager, warehouse, "Sticky Notes Pad Pack 6", 3006, 5, 2, new String[]{"office", "stickers"}, 70, null);
        addItem(manager, warehouse, "Paper Clips Box 100", 3007, 2, 1, new String[]{"office", "fasteners"}, 175, null);
        addItem(manager, warehouse, "Stapler Metal Heavy Duty", 3008, 18, 10, new String[]{"office", "stapler"}, 35, null);
        addItem(manager, warehouse, "Staples Box 5000", 3009, 3, 1, new String[]{"office", "staples"}, 140, null);
        addItem(manager, warehouse, "Scotch Tape 2 Pack", 3010, 6, 3, new String[]{"office", "tape"}, 70, null);
        addItem(manager, warehouse, "Envelope Set 100pcs", 3011, 7, 3, new String[]{"office", "envelope"}, 55, null);
        addItem(manager, warehouse, "Ruler Plastic 12in", 3012, 2, 1, new String[]{"office", "ruler"}, 210, null);
        addItem(manager, warehouse, "Scissors Stainless", 3013, 8, 4, new String[]{"office", "scissors"}, 63, null);
        addItem(manager, warehouse, "Hole Punch 2 Hole", 3014, 12, 6, new String[]{"office", "punch"}, 42, null);
        addItem(manager, warehouse, "Binder Clip Set", 3015, 5, 2, new String[]{"office", "fasteners"}, 84, null);
        addItem(manager, warehouse, "Index Cards 100 pack", 3016, 3, 1, new String[]{"office", "cards"}, 105, null);
        addItem(manager, warehouse, "Legal Pad Yellow", 3017, 5, 2, new String[]{"office", "pad"}, 70, null);
        addItem(manager, warehouse, "Desk Calendar 2026", 3018, 12, 6, new String[]{"office", "calendar"}, 35, null);
        addItem(manager, warehouse, "Post-it Notes 3x3", 3019, 3, 1, new String[]{"office", "stickers"}, 140, null);
        
        // Clothing (20 items)
        addItem(manager, warehouse, "T-Shirt Cotton White M", 4000, 18, 8, new String[]{"clothing", "shirt"}, 85, null);
        addItem(manager, warehouse, "T-Shirt Cotton Black M", 4001, 18, 8, new String[]{"clothing", "shirt"}, 77, null);
        addItem(manager, warehouse, "Polo Shirt Blue L", 4002, 35, 18, new String[]{"clothing", "shirt"}, 42, null);
        addItem(manager, warehouse, "Jeans Denim Blue 32", 4003, 55, 30, new String[]{"clothing", "pants"}, 28, null);
        addItem(manager, warehouse, "Khaki Pants Beige 34", 4004, 45, 24, new String[]{"clothing", "pants"}, 35, null);
        addItem(manager, warehouse, "Hoodie Gray M", 4005, 48, 25, new String[]{"clothing", "hoodie"}, 31, null);
        addItem(manager, warehouse, "Sweatshirt Blue M", 4006, 42, 22, new String[]{"clothing", "sweatshirt"}, 38, null);
        addItem(manager, warehouse, "Jacket Windbreaker L", 4007, 75, 42, new String[]{"clothing", "jacket"}, 21, null);
        addItem(manager, warehouse, "Socks Cotton Pack 6", 4008, 12, 6, new String[]{"clothing", "socks"}, 105, null);
        addItem(manager, warehouse, "Underwear Pack 3", 4009, 20, 10, new String[]{"clothing", "underwear"}, 56, null);
        addItem(manager, warehouse, "Cap Baseball Black", 4010, 15, 8, new String[]{"clothing", "hat"}, 70, null);
        addItem(manager, warehouse, "Beanie Knit Winter", 4011, 18, 9, new String[]{"clothing", "hat"}, 63, null);
        addItem(manager, warehouse, "Gloves Wool Thermal", 4012, 22, 11, new String[]{"clothing", "gloves"}, 52, null);
        addItem(manager, warehouse, "Scarf Cotton Navy", 4013, 25, 13, new String[]{"clothing", "scarf"}, 42, null);
        addItem(manager, warehouse, "Belt Leather Black", 4014, 35, 18, new String[]{"clothing", "belt"}, 35, null);
        addItem(manager, warehouse, "Sneakers Running White", 4015, 85, 50, new String[]{"clothing", "shoes"}, 24, null);
        addItem(manager, warehouse, "Loafers Casual Brown", 4016, 75, 42, new String[]{"clothing", "shoes"}, 28, null);
        addItem(manager, warehouse, "Sandals Sport Black", 4017, 35, 18, new String[]{"clothing", "shoes"}, 56, null);
        addItem(manager, warehouse, "Rain Jacket Yellow", 4018, 65, 38, new String[]{"clothing", "jacket"}, 31, null);
        addItem(manager, warehouse, "Thermal Underwear S", 4019, 28, 15, new String[]{"clothing", "underwear"}, 49, null);
        
        // Books (20 items)
        addItem(manager, warehouse, "Java Programming Guide", 5000, 45, 25, new String[]{"books", "programming"}, 24, null);
        addItem(manager, warehouse, "Data Structures Advanced", 5001, 55, 32, new String[]{"books", "programming"}, 17, null);
        addItem(manager, warehouse, "Business Leadership 101", 5002, 35, 20, new String[]{"books", "business"}, 31, null);
        addItem(manager, warehouse, "Marketing Strategies", 5003, 38, 22, new String[]{"books", "marketing"}, 28, null);
        addItem(manager, warehouse, "Financial Planning", 5004, 42, 25, new String[]{"books", "finance"}, 24, null);
        addItem(manager, warehouse, "Python for Beginners", 5005, 40, 22, new String[]{"books", "programming"}, 35, null);
        addItem(manager, warehouse, "Web Development Guide", 5006, 48, 28, new String[]{"books", "programming"}, 24, null);
        addItem(manager, warehouse, "Cloud Computing Basics", 5007, 52, 30, new String[]{"books", "technology"}, 21, null);
        addItem(manager, warehouse, "Graphic Design Principles", 5008, 45, 26, new String[]{"books", "design"}, 28, null);
        addItem(manager, warehouse, "Public Speaking Skills", 5009, 32, 18, new String[]{"books", "skills"}, 35, null);
        addItem(manager, warehouse, "Novel Mystery Fiction", 5010, 18, 10, new String[]{"books", "fiction"}, 56, null);
        addItem(manager, warehouse, "Science Non-Fiction", 5011, 25, 14, new String[]{"books", "nonfiction"}, 42, null);
        addItem(manager, warehouse, "Biography Historical", 5012, 28, 16, new String[]{"books", "biography"}, 38, null);
        addItem(manager, warehouse, "Cookbook Healthy Eating", 5013, 32, 18, new String[]{"books", "cooking"}, 31, null);
        addItem(manager, warehouse, "Travel Guide Europe", 5014, 35, 20, new String[]{"books", "travel"}, 35, null);
        addItem(manager, warehouse, "History Ancient Rome", 5015, 38, 22, new String[]{"books", "history"}, 31, null);
        addItem(manager, warehouse, "Philosophy Timeless", 5016, 42, 25, new String[]{"books", "philosophy"}, 24, null);
        addItem(manager, warehouse, "Art History Modern", 5017, 48, 28, new String[]{"books", "art"}, 24, null);
        addItem(manager, warehouse, "Photography Guide", 5018, 45, 26, new String[]{"books", "photography"}, 28, null);
        addItem(manager, warehouse, "Self-Help Development", 5019, 28, 16, new String[]{"books", "selfhelp"}, 45, null);
        
        // Beverages (Perishable, 5 items)
        addPerishableItem(manager, warehouse, "Coffee Ground Premium", 6000, 15, 8, 200, new String[]{"beverage", "coffee"}, "2026-09-15");
        addPerishableItem(manager, warehouse, "Green Tea Organic", 6001, 12, 6, 250, new String[]{"beverage", "tea"}, "2026-11-20");
        addPerishableItem(manager, warehouse, "Orange Juice 1L", 6002, 4, 2, 300, new String[]{"beverage", "juice"}, "2026-03-15");
        addPerishableItem(manager, warehouse, "Energy Drink Blue", 6003, 2, 1, 500, new String[]{"beverage", "energy"}, "2026-06-30");
        addPerishableItem(manager, warehouse, "Water Bottle Pack 12", 6004, 8, 4, 400, new String[]{"beverage", "water"}, "2027-02-05");
    }
    
    private static void populateCentral(WarehouseManager manager, Warehouse warehouse) {
        manager.setCurrent("Central Distribution");
        
        // Tools & Hardware (25 items)
        addItem(manager, warehouse, "Hammer Claw 16oz", 7000, 22, 12, new String[]{"tools", "hammer"}, 70, null);
        addItem(manager, warehouse, "Screwdriver Set 12pc", 7001, 35, 20, new String[]{"tools", "screwdriver"}, 42, null);
        addItem(manager, warehouse, "Wrench Set Metric", 7002, 45, 26, new String[]{"tools", "wrench"}, 35, null);
        addItem(manager, warehouse, "Drill Bit Set 21pc", 7003, 28, 15, new String[]{"tools", "drill"}, 56, null);
        addItem(manager, warehouse, "Saw Hand Crosscut", 7004, 35, 20, new String[]{"tools", "saw"}, 31, null);
        addItem(manager, warehouse, "Measuring Tape 25ft", 7005, 12, 6, new String[]{"tools", "measuring"}, 105, null);
        addItem(manager, warehouse, "Level Spirit 24in", 7006, 32, 18, new String[]{"tools", "level"}, 45, null);
        addItem(manager, warehouse, "Pliers Set 4 Piece", 7007, 28, 15, new String[]{"tools", "pliers"}, 52, null);
        addItem(manager, warehouse, "Flashlight LED 500lm", 7008, 25, 13, new String[]{"tools", "light"}, 59, null);
        addItem(manager, warehouse, "Tool Belt Apron", 7009, 35, 18, new String[]{"tools", "storage"}, 38, null);
        addItem(manager, warehouse, "Nail Assortment Box", 7010, 8, 4, new String[]{"hardware", "nail"}, 140, null);
        addItem(manager, warehouse, "Screw Assortment Kit", 7011, 12, 6, new String[]{"hardware", "screw"}, 105, null);
        addItem(manager, warehouse, "Bolt Nut Set", 7012, 15, 8, new String[]{"hardware", "bolt"}, 84, null);
        addItem(manager, warehouse, "Washer Pack 100", 7013, 5, 2, new String[]{"hardware", "washer"}, 175, null);
        addItem(manager, warehouse, "Anchor Toggle Pack", 7014, 4, 2, new String[]{"hardware", "anchor"}, 210, null);
        addItem(manager, warehouse, "Sandpaper Assort 80-220", 7015, 8, 4, new String[]{"hardware", "sandpaper"}, 126, null);
        addItem(manager, warehouse, "Steel Paint 1qt", 7016, 18, 10, new String[]{"hardware", "paint"}, 63, null);
        addItem(manager, warehouse, "Wood Stain Walnut", 7017, 22, 12, new String[]{"hardware", "stain"}, 52, null);
        addItem(manager, warehouse, "Caulk White Silicone", 7018, 6, 3, new String[]{"hardware", "caulk"}, 84, null);
        addItem(manager, warehouse, "Duct Tape Silver", 7019, 5, 2, new String[]{"hardware", "tape"}, 140, null);
        addItem(manager, warehouse, "Zip Ties Black Pack 100", 7020, 4, 2, new String[]{"hardware", "ties"}, 175, null);
        addItem(manager, warehouse, "Safety Goggles Clear", 7021, 12, 6, new String[]{"hardware", "safety"}, 70, null);
        addItem(manager, warehouse, "Work Gloves Leather", 7022, 15, 8, new String[]{"hardware", "gloves"}, 56, null);
        addItem(manager, warehouse, "Tool Box Metal 22in", 7023, 65, 38, new String[]{"hardware", "storage"}, 24, null);
        addItem(manager, warehouse, "Extension Cord 50ft", 7024, 28, 15, new String[]{"hardware", "cord"}, 49, null);
        
        // Garden & Outdoor (25 items)
        addItem(manager, warehouse, "Shovel Garden Square", 8000, 35, 20, new String[]{"garden", "shovel"}, 35, null);
        addItem(manager, warehouse, "Rake Metal 16 Tooth", 8001, 28, 15, new String[]{"garden", "rake"}, 42, null);
        addItem(manager, warehouse, "Hoe Garden Tool", 8002, 25, 13, new String[]{"garden", "hoe"}, 49, null);
        addItem(manager, warehouse, "Pruning Shears Bypass", 8003, 22, 12, new String[]{"garden", "pruning"}, 59, null);
        addItem(manager, warehouse, "Garden Hose 50ft", 8004, 32, 18, new String[]{"garden", "hose"}, 45, null);
        addItem(manager, warehouse, "Sprinkler Oscillating", 8005, 40, 23, new String[]{"garden", "watering"}, 35, null);
        addItem(manager, warehouse, "Watering Can 2 Gallon", 8006, 18, 9, new String[]{"garden", "watering"}, 70, null);
        addItem(manager, warehouse, "Soil Potting 5 Cubic Ft", 8007, 12, 6, new String[]{"garden", "soil"}, 105, null);
        addItem(manager, warehouse, "Mulch Bark 2 Cubic Ft", 8008, 8, 4, new String[]{"garden", "mulch"}, 140, null);
        addItem(manager, warehouse, "Fertilizer Bag 10lb", 8009, 15, 8, new String[]{"garden", "fertilizer"}, 84, null);
        addItem(manager, warehouse, "Plant Pots Clay 6in", 8010, 3, 1, new String[]{"garden", "pots"}, 280, null);
        addItem(manager, warehouse, "Garden Gloves Women", 8011, 14, 7, new String[]{"garden", "gloves"}, 77, null);
        addItem(manager, warehouse, "Garden Kneeler Pad", 8012, 35, 20, new String[]{"garden", "accessories"}, 38, null);
        addItem(manager, warehouse, "Wheelbarrow Metal", 8013, 95, 55, new String[]{"garden", "transport"}, 17, null);
        addItem(manager, warehouse, "Garden Tool Set 8pc", 8014, 55, 32, new String[]{"garden", "tools"}, 31, null);
        addItem(manager, warehouse, "Plant Support Stake", 8015, 2, 1, new String[]{"garden", "support"}, 350, null);
        addItem(manager, warehouse, "Garden Twine Jute", 8016, 5, 2, new String[]{"garden", "twine"}, 140, null);
        addItem(manager, warehouse, "Grass Seed Premium", 8017, 35, 20, new String[]{"garden", "seed"}, 42, null);
        addItem(manager, warehouse, "Flower Seed Collection", 8018, 12, 6, new String[]{"garden", "seed"}, 98, null);
        addItem(manager, warehouse, "Landscape Fabric", 8019, 25, 13, new String[]{"garden", "fabric"}, 56, null);
        addItem(manager, warehouse, "Garden Arbor Metal", 8020, 120, 70, new String[]{"garden", "structure"}, 10, null);
        addItem(manager, warehouse, "Raised Bed Planter", 8021, 85, 50, new String[]{"garden", "planter"}, 21, null);
        addItem(manager, warehouse, "Compost Bin Tumbler", 8022, 110, 65, new String[]{"garden", "compost"}, 14, null);
        addItem(manager, warehouse, "Bird Feeder Wooden", 8023, 32, 18, new String[]{"garden", "bird"}, 49, null);
        addItem(manager, warehouse, "Garden Hose Reel", 8024, 55, 32, new String[]{"garden", "storage"}, 28, null);
        
        // Health & Wellness (25 items)
        addItem(manager, warehouse, "Yoga Mat Non-Slip", 9000, 35, 20, new String[]{"health", "yoga"}, 42, null);
        addItem(manager, warehouse, "Dumbbells Set 5-25lb", 9001, 95, 55, new String[]{"health", "weights"}, 17, null);
        addItem(manager, warehouse, "Resistance Bands Set", 9002, 28, 15, new String[]{"health", "exercise"}, 56, null);
        addItem(manager, warehouse, "Jump Rope Speed", 9003, 18, 10, new String[]{"health", "cardio"}, 70, null);
        addItem(manager, warehouse, "Exercise Ball 65cm", 9004, 32, 18, new String[]{"health", "balance"}, 49, null);
        addItem(manager, warehouse, "Foam Roller 36in", 9005, 25, 13, new String[]{"health", "massage"}, 59, null);
        addItem(manager, warehouse, "Meditation Cushion", 9006, 40, 23, new String[]{"health", "meditation"}, 35, null);
        addItem(manager, warehouse, "Fitness Tracker Watch", 9007, 150, 90, new String[]{"health", "wearable"}, 21, null);
        addItem(manager, warehouse, "Scale Digital Bathroom", 9008, 35, 20, new String[]{"health", "scale"}, 42, null);
        addItem(manager, warehouse, "Blood Pressure Monitor", 9009, 55, 32, new String[]{"health", "monitor"}, 28, null);
        addItem(manager, warehouse, "Thermometer Digital", 9010, 18, 10, new String[]{"health", "thermometer"}, 70, null);
        addItem(manager, warehouse, "Massage Gun Portable", 9011, 85, 50, new String[]{"health", "massage"}, 24, null);
        addItem(manager, warehouse, "Protein Powder 2lb", 9012, 32, 18, new String[]{"health", "nutrition"}, 52, null);
        addItem(manager, warehouse, "Vitamin B Complex", 9013, 15, 8, new String[]{"health", "vitamins"}, 84, null);
        addItem(manager, warehouse, "Omega 3 Supplement", 9014, 22, 12, new String[]{"health", "vitamins"}, 63, null);
        addItem(manager, warehouse, "Multivitamin Daily", 9015, 18, 10, new String[]{"health", "vitamins"}, 77, null);
        addItem(manager, warehouse, "Pain Relief Tablets", 9016, 8, 4, new String[]{"health", "medicine"}, 140, null);
        addItem(manager, warehouse, "First Aid Kit Complete", 9017, 35, 20, new String[]{"health", "safety"}, 38, null);
        addItem(manager, warehouse, "Hand Sanitizer 8oz", 9018, 6, 3, new String[]{"health", "hygiene"}, 175, null);
        addItem(manager, warehouse, "Face Mask N95 Pack 50", 9019, 12, 6, new String[]{"health", "protection"}, 105, null);
        addItem(manager, warehouse, "Sports Water Bottle", 9020, 22, 12, new String[]{"health", "hydration"}, 63, null);
        addItem(manager, warehouse, "Electrolyte Drink Mix", 9021, 10, 5, new String[]{"health", "drink"}, 126, null);
        addItem(manager, warehouse, "Gym Towel Microfiber", 9022, 15, 8, new String[]{"health", "towel"}, 91, null);
        addItem(manager, warehouse, "Sweat Headband Pack", 9023, 8, 4, new String[]{"health", "accessories"}, 140, null);
        addItem(manager, warehouse, "Shoe Inserts Gel", 9024, 25, 13, new String[]{"health", "comfort"}, 59, null);
    }
    
    private static void populateWest(WarehouseManager manager, Warehouse warehouse) {
        manager.setCurrent("West Coast Center");
        
        // Sports & Outdoors (30 items)
        addItem(manager, warehouse, "Soccer Ball Pro", 10000, 35, 20, new String[]{"sports", "soccer"}, 42, null);
        addItem(manager, warehouse, "Basketball Regulation", 10001, 45, 26, new String[]{"sports", "basketball"}, 35, null);
        addItem(manager, warehouse, "Baseball Leather", 10002, 15, 8, new String[]{"sports", "baseball"}, 105, null);
        addItem(manager, warehouse, "Tennis Ball Can", 10003, 4, 2, new String[]{"sports", "tennis"}, 210, null);
        addItem(manager, warehouse, "Football Official", 10004, 55, 32, new String[]{"sports", "football"}, 28, null);
        addItem(manager, warehouse, "Golf Ball Dozen", 10005, 25, 13, new String[]{"sports", "golf"}, 70, null);
        addItem(manager, warehouse, "Golf Club Irons Set", 10006, 350, 200, new String[]{"sports", "golf"}, 8, null);
        addItem(manager, warehouse, "Tennis Racket Graphite", 10007, 120, 70, new String[]{"sports", "tennis"}, 21, null);
        addItem(manager, warehouse, "Badminton Racket Pair", 10008, 45, 25, new String[]{"sports", "badminton"}, 35, null);
        addItem(manager, warehouse, "Ping Pong Paddle Set", 10009, 35, 20, new String[]{"sports", "pingpong"}, 42, null);
        addItem(manager, warehouse, "Skateboard Complete", 10010, 85, 50, new String[]{"sports", "skateboard"}, 24, null);
        addItem(manager, warehouse, "Roller Skates Inline", 10011, 95, 55, new String[]{"sports", "skating"}, 21, null);
        addItem(manager, warehouse, "Bicycle Mountain 21 speed", 10012, 450, 250, new String[]{"sports", "bike"}, 5, null);
        addItem(manager, warehouse, "Bicycle Road Flat Bar", 10013, 350, 200, new String[]{"sports", "bike"}, 8, null);
        addItem(manager, warehouse, "Bicycle Helmet", 10014, 65, 38, new String[]{"sports", "protection"}, 28, null);
        addItem(manager, warehouse, "Camping Tent 4 Person", 10015, 180, 105, new String[]{"sports", "camping"}, 14, null);
        addItem(manager, warehouse, "Sleeping Bag Thermal", 10016, 95, 55, new String[]{"sports", "camping"}, 24, null);
        addItem(manager, warehouse, "Backpack Hiking 50L", 10017, 135, 80, new String[]{"sports", "hiking"}, 17, null);
        addItem(manager, warehouse, "Hiking Boots Waterproof", 10018, 125, 75, new String[]{"sports", "hiking"}, 19, null);
        addItem(manager, warehouse, "Water Bottle Hydration", 10019, 32, 18, new String[]{"sports", "hydration"}, 49, null);
        addItem(manager, warehouse, "Kayak Single Seat", 10020, 550, 320, new String[]{"sports", "water"}, 3, null);
        addItem(manager, warehouse, "Life Jacket Adult", 10021, 65, 38, new String[]{"sports", "safety"}, 31, null);
        addItem(manager, warehouse, "Fishing Rod Spinning", 10022, 85, 50, new String[]{"sports", "fishing"}, 24, null);
        addItem(manager, warehouse, "Fishing Reel Baitcast", 10023, 75, 42, new String[]{"sports", "fishing"}, 28, null);
        addItem(manager, warehouse, "Fish Hook Assortment", 10024, 8, 4, new String[]{"sports", "fishing"}, 140, null);
        addItem(manager, warehouse, "Fishing Line 500yd", 10025, 12, 6, new String[]{"sports", "fishing"}, 105, null);
        addItem(manager, warehouse, "Cooler Ice Chest 70qt", 10026, 120, 70, new String[]{"sports", "cooler"}, 14, null);
        addItem(manager, warehouse, "Flashlight Headlamp", 10027, 45, 26, new String[]{"sports", "light"}, 35, null);
        addItem(manager, warehouse, "Compass Hiking Map", 10028, 18, 10, new String[]{"sports", "navigation"}, 70, null);
        addItem(manager, warehouse, "Multi-tool Pocket", 10029, 42, 24, new String[]{"sports", "tools"}, 38, null);
        
        // Kitchen & Dining (25 items)
        addItem(manager, warehouse, "Chef Knife 8in Stainless", 11000, 45, 26, new String[]{"kitchen", "knife"}, 35, null);
        addItem(manager, warehouse, "Knife Set 5 Piece", 11001, 85, 50, new String[]{"kitchen", "knife"}, 24, null);
        addItem(manager, warehouse, "Cutting Board Wooden", 11002, 28, 15, new String[]{"kitchen", "board"}, 52, null);
        addItem(manager, warehouse, "Mixing Bowl Set 3", 11003, 22, 12, new String[]{"kitchen", "bowl"}, 63, null);
        addItem(manager, warehouse, "Measuring Cups Metal", 11004, 12, 6, new String[]{"kitchen", "measuring"}, 105, null);
        addItem(manager, warehouse, "Measuring Spoons Set", 11005, 8, 4, new String[]{"kitchen", "measuring"}, 140, null);
        addItem(manager, warehouse, "Whisk Set Stainless", 11006, 15, 8, new String[]{"kitchen", "whisk"}, 84, null);
        addItem(manager, warehouse, "Spatula Rubber Set 3", 11007, 12, 6, new String[]{"kitchen", "utensil"}, 91, null);
        addItem(manager, warehouse, "Tongs Serving Stainless", 11008, 14, 7, new String[]{"kitchen", "utensil"}, 77, null);
        addItem(manager, warehouse, "Ladle Large Serving", 11009, 10, 5, new String[]{"kitchen", "utensil"}, 112, null);
        addItem(manager, warehouse, "Colander Stainless Steel", 11010, 22, 12, new String[]{"kitchen", "colander"}, 63, null);
        addItem(manager, warehouse, "Strainer Fine Mesh", 11011, 12, 6, new String[]{"kitchen", "strainer"}, 98, null);
        addItem(manager, warehouse, "Pot Stainless 5 qt", 11012, 35, 20, new String[]{"kitchen", "pot"}, 42, null);
        addItem(manager, warehouse, "Pan Non-stick 10in", 11013, 32, 18, new String[]{"kitchen", "pan"}, 49, null);
        addItem(manager, warehouse, "Baking Pan Set 3", 11014, 28, 15, new String[]{"kitchen", "baking"}, 52, null);
        addItem(manager, warehouse, "Cookie Sheet Aluminum", 11015, 15, 8, new String[]{"kitchen", "baking"}, 84, null);
        addItem(manager, warehouse, "Muffin Tin 12 Cup", 11016, 12, 6, new String[]{"kitchen", "baking"}, 98, null);
        addItem(manager, warehouse, "Bread Knife Serrated", 11017, 28, 15, new String[]{"kitchen", "knife"}, 52, null);
        addItem(manager, warehouse, "Vegetable Peeler", 11018, 6, 3, new String[]{"kitchen", "peeler"}, 175, null);
        addItem(manager, warehouse, "Garlic Press Stainless", 11019, 10, 5, new String[]{"kitchen", "press"}, 119, null);
        addItem(manager, warehouse, "Can Opener Manual", 11020, 8, 4, new String[]{"kitchen", "opener"}, 140, null);
        addItem(manager, warehouse, "Bottle Opener Set", 11021, 6, 3, new String[]{"kitchen", "opener"}, 196, null);
        addItem(manager, warehouse, "Rolling Pin Wooden", 11022, 12, 6, new String[]{"kitchen", "baking"}, 98, null);
        addItem(manager, warehouse, "Pasta Maker Machine", 11023, 85, 50, new String[]{"kitchen", "pasta"}, 24, null);
        addItem(manager, warehouse, "Food Processor 10 Cup", 11024, 120, 70, new String[]{"kitchen", "processor"}, 15, null);
        
        // Home Decor (25 items)
        addItem(manager, warehouse, "Wall Clock Round", 12000, 32, 18, new String[]{"decor", "clock"}, 49, null);
        addItem(manager, warehouse, "Picture Frame 5x7", 12001, 12, 6, new String[]{"decor", "frame"}, 105, null);
        addItem(manager, warehouse, "Mirror Wall 24x36in", 12002, 55, 32, new String[]{"decor", "mirror"}, 28, null);
        addItem(manager, warehouse, "Throw Pillow Accent", 12003, 35, 20, new String[]{"decor", "pillow"}, 42, null);
        addItem(manager, warehouse, "Area Rug 5x8", 12004, 120, 70, new String[]{"decor", "rug"}, 14, null);
        addItem(manager, warehouse, "Curtain Rod Tension", 12005, 28, 15, new String[]{"decor", "curtain"}, 52, null);
        addItem(manager, warehouse, "Curtain Panels Pair", 12006, 45, 26, new String[]{"decor", "curtain"}, 35, null);
        addItem(manager, warehouse, "Ceiling Light Fixture", 12007, 85, 50, new String[]{"decor", "lighting"}, 24, null);
        addItem(manager, warehouse, "Wall Sconce Light", 12008, 65, 38, new String[]{"decor", "lighting"}, 31, null);
        addItem(manager, warehouse, "Table Lamp Base", 12009, 45, 26, new String[]{"decor", "lighting"}, 35, null);
        addItem(manager, warehouse, "Lamp Shade Fabric", 12010, 22, 12, new String[]{"decor", "lamp"}, 63, null);
        addItem(manager, warehouse, "Candle Pillar 3x3", 12011, 8, 4, new String[]{"decor", "candle"}, 140, null);
        addItem(manager, warehouse, "Vase Glass Clear", 12012, 18, 10, new String[]{"decor", "vase"}, 70, null);
        addItem(manager, warehouse, "Plant Pot Ceramic 8in", 12013, 22, 12, new String[]{"decor", "pot"}, 63, null);
        addItem(manager, warehouse, "Wall Art Canvas Print", 12014, 42, 24, new String[]{"decor", "art"}, 38, null);
        addItem(manager, warehouse, "Throw Blanket Knit", 12015, 42, 24, new String[]{"decor", "blanket"}, 38, null);
        addItem(manager, warehouse, "Bed Sheets Queen", 12016, 48, 28, new String[]{"decor", "bedding"}, 35, null);
        addItem(manager, warehouse, "Comforter Queen Size", 12017, 85, 50, new String[]{"decor", "bedding"}, 24, null);
        addItem(manager, warehouse, "Pillow Down Feather", 12018, 55, 32, new String[]{"decor", "pillow"}, 31, null);
        addItem(manager, warehouse, "Decorative Bowl Ceramic", 12019, 25, 13, new String[]{"decor", "bowl"}, 56, null);
        addItem(manager, warehouse, "Wall Hooks Metal", 12020, 8, 4, new String[]{"decor", "hooks"}, 140, null);
        addItem(manager, warehouse, "Shelf Floating Wood", 12021, 42, 24, new String[]{"decor", "shelf"}, 38, null);
        addItem(manager, warehouse, "Bookends Marble", 12022, 35, 20, new String[]{"decor", "bookend"}, 42, null);
        addItem(manager, warehouse, "Wall Decal Sticker", 12023, 12, 6, new String[]{"decor", "sticker"}, 98, null);
        addItem(manager, warehouse, "Door Mat Entrance", 12024, 22, 12, new String[]{"decor", "mat"}, 63, null);
    }
    
    private static void populateSouth(WarehouseManager manager, Warehouse warehouse) {
        manager.setCurrent("Southern Logistics");
        
        // Toys & Games (30 items)
        addItem(manager, warehouse, "Board Game Strategy", 13000, 35, 20, new String[]{"toys", "board"}, 42, null);
        addItem(manager, warehouse, "Card Game Classic Deck", 13001, 8, 4, new String[]{"toys", "cards"}, 140, null);
        addItem(manager, warehouse, "Puzzle 1000 Piece", 13002, 18, 10, new String[]{"toys", "puzzle"}, 70, null);
        addItem(manager, warehouse, "Building Blocks Set", 13003, 45, 26, new String[]{"toys", "blocks"}, 35, null);
        addItem(manager, warehouse, "Action Figure Set 6", 13004, 25, 13, new String[]{"toys", "figure"}, 56, null);
        addItem(manager, warehouse, "Toy Vehicle Set", 13005, 22, 12, new String[]{"toys", "vehicle"}, 63, null);
        addItem(manager, warehouse, "Doll Fashion Collection", 13006, 35, 20, new String[]{"toys", "doll"}, 42, null);
        addItem(manager, warehouse, "Dollhouse Furniture Set", 13007, 42, 24, new String[]{"toys", "dollhouse"}, 38, null);
        addItem(manager, warehouse, "Remote Control Car", 13008, 55, 32, new String[]{"toys", "rc"}, 28, null);
        addItem(manager, warehouse, "Drone Quadcopter", 13009, 150, 90, new String[]{"toys", "drone"}, 17, null);
        addItem(manager, warehouse, "Kite Flying Outdoor", 13010, 15, 8, new String[]{"toys", "kite"}, 84, null);
        addItem(manager, warehouse, "Yo-Yo Metal Trick", 13011, 12, 6, new String[]{"toys", "yoyo"}, 98, null);
        addItem(manager, warehouse, "Spinning Top Set", 13012, 8, 4, new String[]{"toys", "top"}, 140, null);
        addItem(manager, warehouse, "Magic Trick Kit", 13013, 28, 15, new String[]{"toys", "magic"}, 52, null);
        addItem(manager, warehouse, "Science Kit STEM", 13014, 42, 24, new String[]{"toys", "stem"}, 38, null);
        addItem(manager, warehouse, "Crystal Growing Kit", 13015, 25, 13, new String[]{"toys", "science"}, 56, null);
        addItem(manager, warehouse, "Telescope Beginner", 13016, 85, 50, new String[]{"toys", "science"}, 24, null);
        addItem(manager, warehouse, "Microscope 40x-640x", 13017, 95, 55, new String[]{"toys", "science"}, 21, null);
        addItem(manager, warehouse, "Robot Building Kit", 13018, 65, 38, new String[]{"toys", "robot"}, 31, null);
        addItem(manager, warehouse, "Coding Robot Toy", 13019, 75, 42, new String[]{"toys", "robot"}, 28, null);
        addItem(manager, warehouse, "Bike Helmet Kids", 13020, 42, 24, new String[]{"toys", "safety"}, 38, null);
        addItem(manager, warehouse, "Roller Skates Kids", 13021, 65, 38, new String[]{"toys", "skating"}, 31, null);
        addItem(manager, warehouse, "Skateboard Beginner", 13022, 55, 32, new String[]{"toys", "skateboard"}, 28, null);
        addItem(manager, warehouse, "Scooter 3 Wheel", 13023, 45, 26, new String[]{"toys", "scooter"}, 35, null);
        addItem(manager, warehouse, "Sled Snow Plastic", 13024, 32, 18, new String[]{"toys", "sled"}, 49, null);
        addItem(manager, warehouse, "Sandbox with Toys", 13025, 55, 32, new String[]{"toys", "sandbox"}, 28, null);
        addItem(manager, warehouse, "Water Gun Large", 13026, 18, 10, new String[]{"toys", "water"}, 70, null);
        addItem(manager, warehouse, "Water Balloon Pack 500", 13027, 6, 3, new String[]{"toys", "water"}, 175, null);
        addItem(manager, warehouse, "Bubbles Solution 1L", 13028, 5, 2, new String[]{"toys", "bubbles"}, 210, null);
        addItem(manager, warehouse, "Frisbee Disc Set", 13029, 12, 6, new String[]{"toys", "frisbee"}, 98, null);
        
        // Cleaning Supplies (20 items)
        addItem(manager, warehouse, "Dish Soap Liquid", 14000, 4, 2, new String[]{"cleaning", "soap"}, 210, null);
        addItem(manager, warehouse, "Laundry Detergent Liquid", 14001, 12, 6, new String[]{"cleaning", "detergent"}, 105, null);
        addItem(manager, warehouse, "All Purpose Cleaner", 14002, 5, 2, new String[]{"cleaning", "cleaner"}, 175, null);
        addItem(manager, warehouse, "Bathroom Cleaner Spray", 14003, 6, 3, new String[]{"cleaning", "cleaner"}, 140, null);
        addItem(manager, warehouse, "Glass Cleaner Window", 14004, 5, 2, new String[]{"cleaning", "cleaner"}, 154, null);
        addItem(manager, warehouse, "Floor Cleaner Concentrate", 14005, 8, 4, new String[]{"cleaning", "cleaner"}, 126, null);
        addItem(manager, warehouse, "Oven Cleaner Spray", 14006, 7, 3, new String[]{"cleaning", "cleaner"}, 112, null);
        addItem(manager, warehouse, "Furniture Polish", 14007, 9, 4, new String[]{"cleaning", "polish"}, 98, null);
        addItem(manager, warehouse, "Stainless Steel Polish", 14008, 10, 5, new String[]{"cleaning", "polish"}, 91, null);
        addItem(manager, warehouse, "Paper Towel Roll 6pk", 14009, 8, 4, new String[]{"cleaning", "towel"}, 126, null);
        addItem(manager, warehouse, "Sponge Dish Pack 3", 14010, 3, 1, new String[]{"cleaning", "sponge"}, 245, null);
        addItem(manager, warehouse, "Scrub Brush Heavy Duty", 14011, 5, 2, new String[]{"cleaning", "brush"}, 168, null);
        addItem(manager, warehouse, "Toilet Brush Set", 14012, 12, 6, new String[]{"cleaning", "brush"}, 105, null);
        addItem(manager, warehouse, "Broom Bristle", 14013, 15, 8, new String[]{"cleaning", "broom"}, 84, null);
        addItem(manager, warehouse, "Dust Pan Metal", 14014, 8, 4, new String[]{"cleaning", "pan"}, 133, null);
        addItem(manager, warehouse, "Mop Bucket Wringer", 14015, 25, 13, new String[]{"cleaning", "mop"}, 59, null);
        addItem(manager, warehouse, "Trash Bags 13 Gallon", 14016, 5, 2, new String[]{"cleaning", "bags"}, 175, null);
        addItem(manager, warehouse, "Plastic Gloves Latex", 14017, 4, 2, new String[]{"cleaning", "gloves"}, 210, null);
        addItem(manager, warehouse, "Air Freshener Spray", 14018, 6, 3, new String[]{"cleaning", "freshener"}, 140, null);
        addItem(manager, warehouse, "Bleach Disinfectant", 14019, 7, 3, new String[]{"cleaning", "bleach"}, 119, null);
        
        // Personal Care (25 items)
        addItem(manager, warehouse, "Shampoo 16oz", 15000, 8, 4, new String[]{"personal", "shampoo"}, 140, null);
        addItem(manager, warehouse, "Conditioner 16oz", 15001, 8, 4, new String[]{"personal", "conditioner"}, 133, null);
        addItem(manager, warehouse, "Body Wash Soap", 15002, 7, 3, new String[]{"personal", "wash"}, 147, null);
        addItem(manager, warehouse, "Face Wash Cleanser", 15003, 10, 5, new String[]{"personal", "face"}, 119, null);
        addItem(manager, warehouse, "Moisturizer Lotion", 15004, 12, 6, new String[]{"personal", "lotion"}, 105, null);
        addItem(manager, warehouse, "Sunscreen SPF 50", 15005, 12, 6, new String[]{"personal", "sunscreen"}, 112, null);
        addItem(manager, warehouse, "Lip Balm Chapstick", 15006, 3, 1, new String[]{"personal", "lip"}, 245, null);
        addItem(manager, warehouse, "Hand Cream Premium", 15007, 10, 5, new String[]{"personal", "hand"}, 126, null);
        addItem(manager, warehouse, "Toothpaste 6oz", 15008, 5, 2, new String[]{"personal", "toothpaste"}, 175, null);
        addItem(manager, warehouse, "Toothbrush Manual", 15009, 4, 2, new String[]{"personal", "toothbrush"}, 210, null);
        addItem(manager, warehouse, "Dental Floss Mint", 15010, 3, 1, new String[]{"personal", "floss"}, 245, null);
        addItem(manager, warehouse, "Mouthwash 16oz", 15011, 7, 3, new String[]{"personal", "mouthwash"}, 154, null);
        addItem(manager, warehouse, "Deodorant Stick", 15012, 5, 2, new String[]{"personal", "deodorant"}, 182, null);
        addItem(manager, warehouse, "Antiperspirant Roll On", 15013, 6, 3, new String[]{"personal", "antiperspirant"}, 168, null);
        addItem(manager, warehouse, "Razor Blade Cartridge", 15014, 8, 4, new String[]{"personal", "razor"}, 140, null);
        addItem(manager, warehouse, "Shaving Cream", 15015, 7, 3, new String[]{"personal", "shaving"}, 147, null);
        addItem(manager, warehouse, "Aftershave Cologne", 15016, 15, 8, new String[]{"personal", "cologne"}, 84, null);
        addItem(manager, warehouse, "Perfume Women 1.7oz", 15017, 35, 20, new String[]{"personal", "perfume"}, 42, null);
        addItem(manager, warehouse, "Cologne Men 3.4oz", 15018, 32, 18, new String[]{"personal", "cologne"}, 49, null);
        addItem(manager, warehouse, "Body Lotion 13oz", 15019, 10, 5, new String[]{"personal", "lotion"}, 126, null);
        addItem(manager, warehouse, "Face Mask Sheet 10pk", 15020, 12, 6, new String[]{"personal", "mask"}, 105, null);
        addItem(manager, warehouse, "Eye Cream Anti-aging", 15021, 18, 10, new String[]{"personal", "eye"}, 70, null);
        addItem(manager, warehouse, "Nail Polish Lacquer", 15022, 6, 3, new String[]{"personal", "nails"}, 168, null);
        addItem(manager, warehouse, "Nail File Set", 15023, 8, 4, new String[]{"personal", "nails"}, 140, null);
        addItem(manager, warehouse, "Makeup Remover Wipes", 15024, 8, 4, new String[]{"personal", "makeup"}, 147, null);
    }
    
    private static void populateNorth(WarehouseManager manager, Warehouse warehouse) {
        manager.setCurrent("Northern Storage");
        
        // Pet Supplies (30 items) + Mixed items to reach ~100
        addItem(manager, warehouse, "Dog Food Premium 30lb", 16000, 35, 20, new String[]{"pets", "dog food"}, 42, null);
        addItem(manager, warehouse, "Cat Food Dry 20lb", 16001, 28, 15, new String[]{"pets", "cat food"}, 52, null);
        addItem(manager, warehouse, "Dog Treats Biscuits", 16002, 8, 4, new String[]{"pets", "treats"}, 140, null);
        addItem(manager, warehouse, "Cat Treats Salmon", 16003, 7, 3, new String[]{"pets", "treats"}, 154, null);
        addItem(manager, warehouse, "Dog Bowl Stainless", 16004, 12, 6, new String[]{"pets", "bowl"}, 105, null);
        addItem(manager, warehouse, "Cat Bowl Ceramic", 16005, 10, 5, new String[]{"pets", "bowl"}, 119, null);
        addItem(manager, warehouse, "Dog Collar Adjustable", 16006, 15, 8, new String[]{"pets", "collar"}, 84, null);
        addItem(manager, warehouse, "Cat Collar Bell", 16007, 8, 4, new String[]{"pets", "collar"}, 140, null);
        addItem(manager, warehouse, "Dog Leash Nylon", 16008, 18, 10, new String[]{"pets", "leash"}, 70, null);
        addItem(manager, warehouse, "Retractable Leash 26ft", 16009, 22, 12, new String[]{"pets", "leash"}, 63, null);
        addItem(manager, warehouse, "Dog Bed Cushion", 16010, 35, 20, new String[]{"pets", "bed"}, 42, null);
        addItem(manager, warehouse, "Cat Bed Warm", 16011, 28, 15, new String[]{"pets", "bed"}, 52, null);
        addItem(manager, warehouse, "Dog Crate Metal", 16012, 65, 38, new String[]{"pets", "crate"}, 28, null);
        addItem(manager, warehouse, "Cat Litter Box", 16013, 32, 18, new String[]{"pets", "litter"}, 49, null);
        addItem(manager, warehouse, "Cat Litter Clumping", 16014, 10, 5, new String[]{"pets", "litter"}, 126, null);
        addItem(manager, warehouse, "Dog Shampoo Medicated", 16015, 12, 6, new String[]{"pets", "shampoo"}, 105, null);
        addItem(manager, warehouse, "Cat Shampoo Dry", 16016, 10, 5, new String[]{"pets", "shampoo"}, 119, null);
        addItem(manager, warehouse, "Dog Brush Grooming", 16017, 15, 8, new String[]{"pets", "grooming"}, 84, null);
        addItem(manager, warehouse, "Cat Brush Metal", 16018, 12, 6, new String[]{"pets", "grooming"}, 98, null);
        addItem(manager, warehouse, "Dog Nail Clippers", 16019, 14, 7, new String[]{"pets", "grooming"}, 91, null);
        addItem(manager, warehouse, "Cat Nail Clippers", 16020, 12, 6, new String[]{"pets", "grooming"}, 105, null);
        addItem(manager, warehouse, "Pet ID Tag", 16021, 5, 2, new String[]{"pets", "tag"}, 210, null);
        addItem(manager, warehouse, "Dog Toy Ball Squeaky", 16022, 6, 3, new String[]{"pets", "toy"}, 175, null);
        addItem(manager, warehouse, "Cat Toy Mouse", 16023, 4, 2, new String[]{"pets", "toy"}, 245, null);
        addItem(manager, warehouse, "Dog Toy Rope Tug", 16024, 8, 4, new String[]{"pets", "toy"}, 140, null);
        addItem(manager, warehouse, "Cat Scratching Post", 16025, 45, 26, new String[]{"pets", "furniture"}, 35, null);
        addItem(manager, warehouse, "Bird Cage Metal", 16026, 85, 50, new String[]{"pets", "cage"}, 24, null);
        addItem(manager, warehouse, "Bird Food Mix", 16027, 8, 4, new String[]{"pets", "bird food"}, 140, null);
        addItem(manager, warehouse, "Fish Tank 20 Gallon", 16028, 65, 38, new String[]{"pets", "tank"}, 28, null);
        addItem(manager, warehouse, "Fish Food Flakes", 16029, 5, 2, new String[]{"pets", "fish food"}, 196, null);
        
        // Miscellaneous items to fill to ~100 items
        addItem(manager, warehouse, "Storage Container Set 5", 17000, 35, 20, new String[]{"storage", "container"}, 42, null);
        addItem(manager, warehouse, "Vacuum Seal Bags", 17001, 12, 6, new String[]{"storage", "bags"}, 105, null);
        addItem(manager, warehouse, "Label Maker Thermal", 17002, 45, 26, new String[]{"office", "label"}, 35, null);
        addItem(manager, warehouse, "Shipping Box Mailer", 17003, 2, 1, new String[]{"shipping", "box"}, 350, null);
        addItem(manager, warehouse, "Bubble Wrap Roll", 17004, 18, 10, new String[]{"shipping", "wrap"}, 70, null);
        addItem(manager, warehouse, "Packing Tape Brown", 17005, 5, 2, new String[]{"shipping", "tape"}, 175, null);
        addItem(manager, warehouse, "Foam Peanuts 5 lbs", 17006, 8, 4, new String[]{"shipping", "foam"}, 140, null);
        addItem(manager, warehouse, "Kraft Paper Roll", 17007, 12, 6, new String[]{"shipping", "paper"}, 105, null);
        addItem(manager, warehouse, "Tissue Paper Ream", 17008, 6, 3, new String[]{"packaging", "tissue"}, 175, null);
        addItem(manager, warehouse, "Gift Box Assorted", 17009, 15, 8, new String[]{"packaging", "box"}, 84, null);
        addItem(manager, warehouse, "Ribbon Spool", 17010, 8, 4, new String[]{"packaging", "ribbon"}, 140, null);
        addItem(manager, warehouse, "Gift Bag Assorted", 17011, 10, 5, new String[]{"packaging", "bag"}, 126, null);
        addItem(manager, warehouse, "Birthday Decoration Set", 17012, 18, 10, new String[]{"party", "decoration"}, 70, null);
        addItem(manager, warehouse, "Party Balloon Pack", 17013, 6, 3, new String[]{"party", "balloon"}, 175, null);
        addItem(manager, warehouse, "Party Cup Plastic 50", 17014, 5, 2, new String[]{"party", "cup"}, 210, null);
        addItem(manager, warehouse, "Party Plate Disposable", 17015, 5, 2, new String[]{"party", "plate"}, 196, null);
        addItem(manager, warehouse, "Napkin Luncheon 100", 17016, 3, 1, new String[]{"party", "napkin"}, 245, null);
        addItem(manager, warehouse, "Streamer Paper Roll", 17017, 3, 1, new String[]{"party", "streamer"}, 280, null);
        addItem(manager, warehouse, "Confetti Pack", 17018, 4, 2, new String[]{"party", "confetti"}, 224, null);
        addItem(manager, warehouse, "Battery AA Pack 8", 17019, 8, 4, new String[]{"battery", "aa"}, 140, null);
        addItem(manager, warehouse, "Battery AAA Pack 8", 17020, 8, 4, new String[]{"battery", "aaa"}, 147, null);
        addItem(manager, warehouse, "Battery 9V Pack 4", 17021, 12, 6, new String[]{"battery", "9v"}, 105, null);
        addItem(manager, warehouse, "Rechargeable Battery Pack", 17022, 25, 13, new String[]{"battery", "rechargeable"}, 59, null);
        addItem(manager, warehouse, "Battery Charger 4 Slot", 17023, 35, 20, new String[]{"battery", "charger"}, 42, null);
        addItem(manager, warehouse, "Flashlight Batteries D", 17024, 6, 3, new String[]{"battery", "d"}, 182, null);
        addItem(manager, warehouse, "Watch Battery CR2032", 17025, 4, 2, new String[]{"battery", "button"}, 245, null);
        addItem(manager, warehouse, "Umbrella Compact", 17026, 25, 13, new String[]{"weather", "umbrella"}, 59, null);
        addItem(manager, warehouse, "Rain Poncho Plastic", 17027, 8, 4, new String[]{"weather", "poncho"}, 140, null);
        addItem(manager, warehouse, "Waterproof Bag Dry", 17028, 35, 20, new String[]{"weather", "bag"}, 42, null);
        addItem(manager, warehouse, "Sunglasses UV Protection", 17029, 22, 12, new String[]{"weather", "sunglasses"}, 63, null);
        addItem(manager, warehouse, "Hat Sun Protection", 17030, 18, 10, new String[]{"weather", "hat"}, 70, null);
    }
    
    private static void addItem(WarehouseManager manager, Warehouse warehouse, String name, int sku,
                                int price, int cost, String[] keywords, int stock, String expr) {
        ItemID itemID = new ItemID(name, sku, price, cost, keywords);
        warehouse.addItem(new Item(sku, stock, null), itemID);
    }
    
    private static void addPerishableItem(WarehouseManager manager, Warehouse warehouse, String name, int sku,
                                         int price, int cost, int quantity, String[] keywords, String exprDate) {
        ItemID itemID = new ItemID(name, sku, price, cost, keywords);
        warehouse.addItem(new PerishableItem(sku, quantity, null, LocalDate.parse(exprDate)), itemID);
    }
}
