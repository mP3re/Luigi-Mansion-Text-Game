import java.util.ArrayDeque; //for my stack for the back command!
import java.util.ArrayList; //array list for my random room!
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser; //parser.
    private Room currentRoom; //to help you identify the room you are in
    private Inventory playerInventory;//create a player inventory
    ArrayDeque<String> deque = new ArrayDeque<>(); //initialise stack for the back command
    ArrayList<Room> roomArray = new ArrayList<>();//initialise array list of rooms for random room picker
    
    //here will be the boolean conditions for winning
    
    private boolean necklaceCheck; 
    private boolean watchCheck;
    private boolean keyCheck;
    private boolean dogCheck; //this is used for the dog who blocks certain rooms.
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {   
        //initialize the game features 
        createRooms(); 
        parser = new Parser();
        playerInventory= new Inventory();
        
        //conditions to check to win
        
        keyCheck=false;
        necklaceCheck=false;
        watchCheck=false;
        dogCheck=false;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room foyer, livingRoom, diningRoom, kitchen, stairs, study, bedroom, library, secretRoom;
      
        // create the rooms
        foyer = new Room("The Foyer of the Mansion. It is cold and dusty.");
        
        livingRoom = new Room("The Living Room of the Mansion. Dimly lit by the moonlight leaking through the windows.");
        
        diningRoom = new Room("The Dining Room of the Mansion. Candles along the wall illuminate the long room.");
        
        kitchen = new Room("The Kitchen of the Mansion. It is colder then the other rooms");
        
        stairs = new Room("The Stairway leading to the upstairs.");
        
        study= new Room ("The Study room of the Mansion.");
        
        bedroom= new Room("A Bedroom in the Mansion.");
        
        library=new Room ("A dusty library filled with old books.");
        
        secretRoom= new Room("A secret room hidden behind the bookshelves of the Library.");
        
        //add rooms to roomArray for randomizer
        roomArray.add(foyer);
        roomArray.add(livingRoom);
        roomArray.add(diningRoom);
        roomArray.add(kitchen);
        roomArray.add(stairs);
        ///roomArray.add(study);
        ////roomArray.add(bedroom); cant teleport until dogcheck= true
        roomArray.add(library);
        
        // initialise room exits
        foyer.setExit("north", livingRoom);
        foyer.setExit("east", diningRoom);
        foyer.setExit("west", stairs);

        livingRoom.setExit("south", foyer);
        livingRoom.setExit("north", library);

        library.setExit("south", livingRoom);
        library.setExit("east", secretRoom);

        secretRoom.setExit("west", library);
        
        diningRoom.setExit("west", foyer);
        diningRoom.setExit("north", kitchen);

        kitchen.setExit("south", diningRoom);
        
        stairs.setExit("east", foyer);
        stairs.setExit("west",study);
        stairs.setExit("north",bedroom);
        
        bedroom.setExit("south",stairs);
        
        study.setExit("east", stairs);
        //initalise items
        foyer.setItem("chandelier","A rustic victorian-style chandelier. It creaks to and fro.",false,0);
        foyer.setItem("clock","The low tick-tocking of the grandfather clock echoes through the foyer.",false,0);
        
        diningRoom.setItem("table","A long empty dining table illuminated in candle-light.", false,0 );
        diningRoom.setItem("plate","a clean plate.",true,3);
        
        kitchen.setItem("knives","A set of shiny knives hung on the wall",false,0);
        kitchen.setItem("fridge","A broken down fridge. it creates a low humming sound.",false,0);
        kitchen.setItem("bones","a pile of bones on the counter",true,2);
        
        library.setItem("shelves","a set of bookshelves. very dusty.",false,0);
        library.setItem("key","the key to escape!",true,1);
        
        study.setItem("cookbook","a cookbook. the recipies are in an illegible cipher.",true,3);
        
        bedroom.setItem("window","it is dark and rainy outside.",false,0);
        
        library.setItem("key","the key to escape!",true,1);
        
        livingRoom.setItem("portraits","Eerie portraits stare back at you.",false,0);
        //initalise NPCs and their items
        
        bedroom.setNPC("grandma","Ooh...Come here, child. I have a gift for you. Please take it..*she sets an item on the bedside table*","Visit more often, dear...",false);
        
        livingRoom.setNPC("butler","Beware..noone here is alive...","They have all passed many years ago...",false);
        
        
        kitchen.setNPC("chef","I cannot find my cookbook...","Finally..I can cook. Here, a reward...*he sets an item on the counter*",false);
        
        stairs.setNPC("dog","grr...","woof",false);
        
        library.setNPC("mario","","mamma mia! if you enter the room to the east, you will be teleported randomly! So dizzy...",true);
        
        foyer.setNPC("luigi","oh no! we need a key and two treasures to escape!","good job! this brings us a step closer to getting outta here!",false);
        
        currentRoom = foyer;  // start game foyer
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if(winGame()){
                
                //when the game is won the ending message plays
                System.out.println("You did it! You have successfully found all the treasures and the key!");
                System.out.println("You finally make your exit with Luigi and Mario...Congrats!");
                System.out.println("You gave the treasures to E. Gadd, hoping he wont call you for another quest any time soon...");
                finished=true;
            }
        }
        System.out.println("Fin.");
    }

    /**
     * Print out the opening message for the player.
     * The goal is to give the necklace and key and watch to luigi.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are an explorer for E. Gadd. You have entered a run down, haunted Mansion in the woods.");
        System.out.println("Supposedly, there are cursed treasures inside this Mansion!");
        System.out.println("However, the door locks shut as soon as you, Luigi and Mario enter");
        System.out.println("You must find the treasure, and a way out!");
        System.out.println("Type 'help' if you need help");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        //if the player does not type in a keyword
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        //to display all the keyword options
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        //to move rooms
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        //to exit the program
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        //to examine the room
        else if (commandWord.equals("examine")){
           examineAll(command);
        }
        //to give to an NPC
        else if (commandWord.equals("give")){
            giveItem(command);
        }
        //to take an item in a room
        else if (commandWord.equals("take")){
            takeItem(command);
        }
        //to discard an item in inventory
          else if (commandWord.equals("discard")){
            discardItem(command);
        }
        //to display inventory, with item description and weight
        else if (commandWord.equals("inventory")){
            showInventory(command);
        }
        //to inspect an item in a room
        else if (commandWord.equals("check")){
            checkItem(command);
        }
        //to go back to the previous room
        else if (commandWord.equals("back")){
            goBack(command);
        }
        //to talk to an NPC
        else if (commandWord.equals("talk")){
            talkNPC(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are trapped! Give Luigi two treasures and a key to escape.");
        System.out.println("Be sure to *examine* your surroundings frequently");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        //BLOCKED ROOM CHECK!!!!!!!!!!!!!!!!!
        else if((!(currentRoom.checkNPC("dog")==null))&& currentRoom.getShortDescription().equals("The Stairway leading to the upstairs.")){
                System.out.println("The dog growls at you from the top of the stairs...");
                System.out.println("It seems you cannot exit until the dog is taken care of...");
                System.out.println("Maybe you can find something to distract it...");
                System.out.println("You decide to head back to the foyer...");
                currentRoom=currentRoom.getExit("east");
                System.out.println(currentRoom.getLongDescription());
        }
        ////TELEPORTER ROOM CHECK!!!!!!!!!!!
        else {
            currentRoom = nextRoom;
            deque.push(command.getSecondWord());
            //if the room you are goign to is the magic teleporting room
            if (currentRoom.getShortDescription()=="A secret room hidden behind the bookshelves of the Library."){
                System.out.println("The magical teleporting room behind the bookshelves...");
                System.out.println("Teleporting...");
                Room randomRoomToGo=randomRoom();
                currentRoom=randomRoomToGo;
                deque.clear(); //clear the stack that lets you go back to the previous rooms(as you are teleporting)
            }
            System.out.println(currentRoom.getLongDescription());
            
        }
    }
    
    /** 
     * Display current player inventory
     */
    private void showInventory(Command command){
        if(!command.hasSecondWord()) {
        playerInventory.displayInventory();
        }
    }
    
    /** 
     * Talk to an NPC!
     */
    private void talkNPC(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Talk to whom?");
        }
        else{
            //check that the NPC the player wants to talk to is in the current room
            String currentNPC= command.getSecondWord();
        
            NPC checkingNPC=currentRoom.checkNPC(currentNPC);
            if (checkingNPC!=null){
                checkingNPC.getDialogue();
                if (checkingNPC.getChangeDialogue()==false){ //changes the dialogue when you talk to an NPC twice
                    switch(checkingNPC.getName()){ //depending on the NPC the dialogue will change differently.
                        case("butler"):
                            checkingNPC.setChangeDialogue(true);
                        break;
                        case("grandma"): //talkting to the grandma once makes her drop an item
                            currentRoom.setItem("necklace","an expensive necklace...treasure!",true,1);
                            checkingNPC.setChangeDialogue(true);
                        break;
                    }
                }
            }
        }
    }
    
    /** 
     *Give an item to a NPC
     */
         private void giveItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Give what?");
            System.out.println("Format: (Give (item) (NPC))");
        }
        else if(!command.hasThirdWord()){
            System.out.println("Give to whom?");
            System.out.println("Format: (Give (item) (NPC))");
        }
        else {
            //check that the item is in the players inventory
            String currentItem= command.getSecondWord();
            Item itemToGive=playerInventory.inInventory(currentItem);
            //check that the NPC is in the current room
            String currentNPC=command.getThirdWord();
            NPC NPCToGive=currentRoom.checkNPC(currentNPC);
            //here are the possible giving combinations that are allowed:
            if (itemToGive!=null && NPCToGive!=null){
            if (itemToGive.getName().equals("cookbook") && NPCToGive.getName().equals("chef")){
                //givign the chef a cookbook makes him give you a treasure
                NPCToGive.setChangeDialogue(true);
                NPCToGive.getDialogue();
                playerInventory.removeInventory(itemToGive);
                currentRoom.setItem("watch","a vintage watch...treasure!",true,1);
            }
            else if (itemToGive.getName().equals("bones")&& NPCToGive.getName().equals("dog")){
                playerInventory.removeInventory(itemToGive);
                NPCToGive.setChangeDialogue(true);
                NPCToGive.getDialogue();
                
                //the dog will now move to a random room, and you can proceed to the 2 locked exits
                System.out.println("The dog backs away into the bedroom...");
                System.out.println("It seems to like you now.. you can finally pass!");
                
                //now the teleport room array can include the two previously locked rooms!
                roomArray.add(currentRoom.getExit("north"));
                roomArray.add(currentRoom.getExit("west"));
                
                currentRoom.removeNPC(NPCToGive.getName());
                Room randomRoomForDog= currentRoom;
                while (randomRoomForDog==currentRoom){
                    //this is to ensure the dog is anywhere but the stairs
                randomRoomForDog= randomRoom();
                randomRoomForDog.setNPC("dog","grr...","woof",true);
                dogCheck=true;
                
            }
        }
        else if (itemToGive.getName().equals("necklace")&& NPCToGive.getName().equals("luigi")){
                necklaceCheck=true;
                playerInventory.removeInventory(itemToGive);
                
                NPCToGive.setChangeDialogue(true);
                System.out.println("Mamma mia, what a shiny necklace! E Gadd will want this.");
                NPCToGive.getDialogue();
                NPCToGive.setChangeDialogue(false);
                //check a winning condition.
                winGame();
            }
            else if (itemToGive.getName().equals("watch")&& NPCToGive.getName().equals("luigi")){
                watchCheck=true;
                playerInventory.removeInventory(itemToGive);
                
                NPCToGive.setChangeDialogue(true);
                System.out.println("This watch is an antique model! E Gadd will want this.");
                NPCToGive.getDialogue();
                NPCToGive.setChangeDialogue(false);
                //check a winning condition.
                winGame();
            }
            else if (itemToGive.getName().equals("key")&& NPCToGive.getName().equals("luigi")){
                keyCheck=true;
                playerInventory.removeInventory(itemToGive);
                
                NPCToGive.setChangeDialogue(true);
                System.out.println("Thank heavens, a key to escape this creepy place...Now we can focus on getting the treasures.");
                NPCToGive.getDialogue();
                NPCToGive.setChangeDialogue(false);
                //check a winning condition.
                winGame();
            }
            else{
                //if NPC is invalid
                System.out.println("They will not take it...");
                return;
            }
        }
        else{
            //if Item is invalid
            System.out.println("You cant give that here...");
        }
    }
    }
    
    /** 
     * display the items that are in the current room
     */
    private void examineAll(Command command){     
        currentRoom.getItems();
        currentRoom.getNPCs();
    }
    
    /** 
     * check the description of an item in the room. this will not work on inventory items
     * but they can be checked in the normal inventory command
     */
    private void checkItem(Command command){     
        if(!command.hasSecondWord()) {
            System.out.println("Check what?");
            System.out.println("Format: check (item)");
            System.out.println("This provides details on an item in a room.");
            return;
        }
        String currentItem= command.getSecondWord();
        
        Item checkingItem=currentRoom.checkItem(currentItem);
        if (checkingItem!=null){
        System.out.println(checkingItem.getItemDescription());
        }
        else{
            System.out.println("That isnt there...");
        }
    }
    
    /** 
     * take an item that is in the current room
     */
    private void takeItem(Command command){
      if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            System.out.println("Format: check (item)");
            return;
        }
        String currentItem= command.getSecondWord();
        
        Item pickingUp=currentRoom.takeItem(currentItem);
        if (pickingUp!=null){
            Item checkFull=playerInventory.addInventory(pickingUp);
            if (checkFull==null){
                discardItem(pickingUp);
            
            }
            else{
                System.out.println(pickingUp.getName()+" has been acquired!");
            }
        }
        else{
                return;
            }
        }
        
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * discard an item that is currently in inventory
     * this will also return the item to the room, so it can be picked up once again.
     */
    private void discardItem(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Discard what?");
        }
        
        String itemThrown= command.getSecondWord();
        
        Item currentItem=playerInventory.inInventory(itemThrown);
        
        if(currentItem!=null){
            discardItem(currentItem);
        }
        else{
            return;
        }
    }
    
    /** 
     * to track back the players movements.
     */
    private void goBack(Command command){
        if (!deque.isEmpty() && !command.hasSecondWord()){
            String popStack= deque.pop();
            String exit;
        switch(popStack){
            case ("north"):
                exit=("south");
                break;
            case ("south"):
                exit=("north");
                break;
            case ("east"):
                exit=("west");
                break;
            case ("west"):
                exit=("east");
                break;
            default:
                System.out.println("Nothing to go back to");
                exit="";
        }
        Room nextRoom =currentRoom.getExit(exit);
        currentRoom = nextRoom;
        System.out.println(currentRoom.getLongDescription());
    }
    else{
        System.out.println("Nothing to go back to...");
    }
    }
    //a section for reusable code:
    
    /** 
     * get the current room
     */
    private Room getRoom(){
        return currentRoom;
    }
    
    /** 
     * removing an item from inventory and setting it back in the room you are in.
     */
    private void discardItem(Item currentItem){
        currentRoom.setItem(currentItem.getName(),currentItem.getItemDescription(),currentItem.getCanTake(),currentItem.getWeight());
        playerInventory.removeInventory(currentItem);
    }
    
    /** 
     * returning a random room from the array of rooms
     */
    private Room randomRoom(){            
        int index = (int)(Math.random() * roomArray.size());
        return roomArray.get(index);
    } 
    
    /** 
     * to check that the game is won/ all winning conditions are met
    */
    private boolean winGame(){
        if (keyCheck && necklaceCheck && watchCheck==true){
            return true;
    }
    else{
        return false;
    }
    }
}
