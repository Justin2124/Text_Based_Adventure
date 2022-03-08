package robahouse.game;

public class Game {
	private Parser parser;
    private Room currentRoom;
    private Player player;
    private CLS cls_var;
    private int counter = 0;
    private Room garage;
    private Room hallway;
    private Room frontYard;
    private Room kitchen;
    private Room livingRoom;
    private Room bedroom;
    private Item keyNumber1;
    private Item keyNumber2;
    private Item keyNumber3;
    private Item keyNumber4;
    private String commandWord;
    
    public Game() {
        parser = new Parser();
        player = new Player();
    }
    
    public static void main(String[] args) {
    	System.out.println("__________      ___.                ___ ___                             ");
    	System.out.println("\\______   \\ ____\\_ |__   _____     /   |   \\  ____  __ __  ______ ____  ");
    	System.out.println(" |       _//  _ \\| __ \\  \\__  \\   /    ~    \\/  _ \\|  |  \\/  ___// __ \\");
    	System.out.println(" |    |   (  <_> ) \\_\\ \\  / __ \\_ \\    Y    (  <_> )  |  /\\___ \\\\  ___/");
    	System.out.println(" |____|_  /\\____/|___  / (____  /  \\___|_  / \\____/|____//____  >\\___  >");
    	System.out.println("       \\/           \\/       \\/         \\/                   \\/     \\/ ");
        Game game = new Game();
        game.setupGame();
        game.play();
        
    }
    
    public void printInformation() {
        System.out.println(currentRoom.getShortDescription());
        System.out.println(currentRoom.getExitString());
        System.out.println(currentRoom.getInventoryString());
        System.out.println(player.getInventoryString());
    }
    
    public void setupGame() {
        //environment of the game
        frontYard = new Room("Front Yard", sFrontYard, lFrontYard);
        hallway = new Room("Hallway", sHallway, lHallway);
        livingRoom = new Room("Living Room", sLivingRoom, lLivingRoom);
        kitchen = new Room("Kitchen", sKitchen, lKitchen); 
        bedroom = new Room("Bedroom", sBedroom, lBedroom);
        garage = new Room("Garage", sGarage, lGarage);
        
        keyNumber1 = new Item("Piece #1", "long description");
        keyNumber2 = new Item("Piece #2", "long description");
        keyNumber3 = new Item("Piece #3", "long description");
        keyNumber4 = new Item("Piece #4", "long description");
        
        frontYard.setExit("hallway", hallway);
        hallway.setExit("living room", livingRoom);
        hallway.setExit("kitchen", kitchen);
        hallway.setExit("bedroom", bedroom);
        hallway.setExit("front yard", frontYard);
        kitchen.setExit("garage", garage);
        kitchen.setExit("hallway", hallway);
        livingRoom.setExit("hallway", hallway);
        bedroom.setExit("hallway", hallway);
        garage.setExit("kitchen", kitchen);
        
        garage.setItem("piece1", keyNumber1);
        kitchen.setItem("piece2", keyNumber2);
        livingRoom.setItem("piece3", keyNumber3);
        frontYard.setItem("piece4", keyNumber4);
        
        currentRoom = frontYard;
        
        try {
            cls_var.main(); 
        }
        catch(Exception e) {
            System.out.println(e); 
        }
        
        printInformation();
    }
    
    public void play() {
        while(checkWin()==true) {            
            Command command = parser.getCommand(); 
            try {
                cls_var.main(); 
            }
            catch(Exception e) {
                System.out.println(e); 
            }
            processCommand(command);
            printInformation();   
        }
        System.out.println("You got caught!!! You Lost!!!");
    }
    
    private boolean checkWin() {
        if (counter < 30){
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean checkItems() {
    	
    	if(garage.hasItem().equals(keyNumber1) && livingRoom.hasItem().equals(keyNumber2) && hallway.hasItem().equals(keyNumber3) && kitchen.hasItem().equals(keyNumber4)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public void processCommand(Command command) {
        commandWord = command.getCommandWord().toLowerCase();
        
        switch(commandWord) {
            case "go":
                goRoom(command);
                break;
            case "room":
                System.out.println(currentRoom.getLongDescription());
                break;
            case "grab":
                grab(command);
                break;
            case "drop":
                drop(command);
                break;
            case "inspect":
                inspect(command);
                break;
            case "help":
                help(command);
                break;
        }
        
    }

    
    public void help(Command command) {
    	if(!command.hasSecondWord()) {
    		System.out.println("List of commands:" + " go, room, grab, drop, inspect");
    	}
    	else {
        	commandWord = command.getSecondWord().toLowerCase();
    		switch(commandWord) {
    			case "go":
    				System.out.println("This command allows you to go to different rooms. When you type go, type the name of the room after.");
    				break;
    			case "room":
    				System.out.println("This command simply gives you a longer discription of the room");
    				break;
    			case "grab":
    				System.out.println("This command allows you to grab different items. When you type grab, type the name of the item after.");
    				break;
    			case "drop":
    				System.out.println("This command allows you to drop different items. When you type drop, type the name of the item after.");
    				break;
    			case "inspect":
    				System.out.println("This command allows you to inspect different items. When you type inspect, type the name of the item after.");
    				break;
    				
    		}
    	}
    }
    
    public void inspect(Command command) {
        String printString = "Looking at ";
        if(!command.hasSecondWord()) {
            System.out.println("What are you inspecting?");
            return;
        }
        String thingToInspect = command.getSecondWord();
        if(thingToInspect.equals(currentRoom.getName())){
            printString += "the room: " + currentRoom.getName() + "\n" +currentRoom.getLongDescription();
        }
        else if(currentRoom.getItem(thingToInspect)!= null){
            printString += "the item: " + currentRoom.getItem(thingToInspect).getName() + "\n" + currentRoom.getItem(thingToInspect).getDescription();
        }
        else if(player.getItem(thingToInspect)!= null){
            printString += "the item: " + player.getItem(thingToInspect).getName() + "\n" + player.getItem(thingToInspect).getDescription();
        }
        else {
            printString += "\nYou can't inspect that!";
        }
        
        System.out.println(printString);
    }
    
    public void grab(Command command) {
        String item = command.getSecondWord();
        Item itemToGrab = currentRoom.removeItem(item);
        if(!command.hasSecondWord()) {
            System.out.println("Grab what?");
            return;
        }
        else if(itemToGrab == null) {
            System.out.println("You can't grab that");
            return;
        }
        else if(checkItems() == false){
             player.setItem(item, itemToGrab);
             counter++;
             System.out.println("Move(s) left to unlock bedroom: " + (25 - counter));
        }
    }
    
    public void drop(Command command) {
        String playerItem = command.getSecondWord();
        Item itemToDrop = player.removeItem(playerItem);
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
        else if(itemToDrop == null) {
            System.out.println("You can't grab that");
            return;
        }
        else if(checkItems() == false){
             currentRoom.setItem(playerItem, itemToDrop);
             counter++;
             System.out.println("Move(s) left to unlock bedroom: " + (25 - counter));
        }
    }
    
    public void goRoom(Command command) {
        String direction = "";
        if(!command.hasSecondWord()) {
            System.out.println("Where do you want to go?");
            return;
        }
        if(!command.hasLine()) {
            direction = command.getSecondWord();            
        }
        else if(command.hasLine()) {
            direction = command.getSecondWord()+command.getLine();
        }
        Room nextRoom = currentRoom.getExit(direction);
        if(nextRoom == null) {
            System.out.println("You can't go there");
            return;
        }
        else if(checkItems() == false){
            currentRoom = nextRoom;
            counter++;
            System.out.println("Move(s) left to unlock bedroom: " + (25 - counter));
        }
        else {
        	
        }
    }
    
    private String sFrontYard = "You are in the the front yard.";
    private String lFrontYard = "You are in the front yard. There is a piece of a key hidden terribly in the bush.";
    private String sHallway = "You are in the hallway.";
    private String lHallway = "You just entered the house and are now in the hallway leading to 3 rooms. However entering the bedroom will set off an trap which will result in your death. There was a picture frame with the number 3.";
    private String sLivingRoom = "You are in the living room";
    private String lLivingRoom = "You entered the living room. There was a picture frame with the number 2 and a piece of the key right on the couch.";
    private String sKitchen = "You are in the kitchen.";
    private String lKitchen = "You entered the kitchen. There was a picture frame with the number 4 and a piece of a key.";
    private String sBedroom = "You are in the bedroom";
    private String lBedroom = "You are in the bedroom and got rid of the traps. However there is a safe in the room with the words all engraved on it. ";
    private String sGarage = "You are in the garage.";
    private String lGarage = "You entered the garage. There was a picture frame with the number 1 and a piece of a key right by the entrance to the garage.";
}
