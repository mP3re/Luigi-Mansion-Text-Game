import java.util.Set;
import java.util.HashMap;
import java.util.Collection;

/**
 * Write a description of class Inventory here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Inventory
{
    // instance variables - replace the example below with your own
    private int totalWeight;
    private int maxWeight;
    private HashMap<String,Item>inventory;

    /**
     * Constructor for objects of class Inventory
     */
    public Inventory()
    {
        // initialise instance variables
        totalWeight=0;
        maxWeight=5;
        inventory = new HashMap<>();
    }

    /**
     * return the total weight of the items in the inventory 
     */
    public void getTotalWeight()
    {
        // put your code here
        System.out.println("Total weight of items in bag: "+ totalWeight);
    }
    
    /**
     * add an item to inventory
     */
    public Item addInventory(Item currentItem){
        if (totalWeight+ currentItem.getWeight()<=maxWeight) {
            totalWeight= totalWeight+ currentItem.getWeight();
            inventory.put(currentItem.getName(),currentItem);
            return currentItem;
        }
        else{
        System.out.println("That's too heavy!");
        return null;
        }
    }
    
    /**
     * print out the current player inventory
     */
    public void displayInventory(){
        System.out.println("You can carry a total of 5kg");
        if (inventory.size()==0){
                System.out.println("No items found.");
        }
        else{
            Collection<Item> values = inventory.values();
            for (Item value : values) {
                System.out.println("Item: "+ value.getName());
                System.out.println(value.getItemDescription());
                System.out.println("Weight: "+ value.getWeight());
            }
        }
    }

    /**
     * check if an item is in inventory
     */
    public Item inInventory(String currentItem){
        if (inventory.containsKey(currentItem)==true) {
                Item store= inventory.get(currentItem);
                return store;
        }
        else{
            System.out.println("Item not found in inventory...");
            return null;
        }
    
    }

    /**
     * remove an item from inventory
     */
    public void removeInventory(Item currentItem){
        totalWeight= totalWeight - currentItem.getWeight();
        inventory.values().remove(currentItem);
    
    }
}