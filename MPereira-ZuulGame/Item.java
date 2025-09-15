
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private String itemDescription;
    private boolean canTake;
    private int weight;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String itemDescription, boolean canTake,int weight)
    {
        // initialise instance variables
        this.name= name;
        this.itemDescription=itemDescription;
        this.canTake=canTake;
        this.weight=weight;
    }

    /**
     * return the name of the item.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * return the descirption of the item
     */
    public String getItemDescription()
    {
    return itemDescription;
    }
    
    /**
     *see if the item is grabbable or not
     */
    public boolean getCanTake()
    {
    return canTake;
    }
    
    /**
     * get the weight of the item
     */
    public int getWeight(){
        return weight;
    }
}
