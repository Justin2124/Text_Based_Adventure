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
    
    public Game() {
        parser = new Parser();
        player = new Player();
    }
    
    public static void main(String[] args) {
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
        frontYard = new Room("Front Yard", sFrontYard, "long description of the starting room");
        hallway = new Room("Hallway", sHallway, "long description of the second room");
        livingRoom = new Room("Living Room", sLivingRoom, "long description of the second room");
        kitchen = new Room("Kitchen", sKitchen, "long description of the second room"); 
        bedroom = new Room("Bedroom", sBedroom, "long description of the second room");
        garage = new Room("Garage", sGarage, "long description of the second room");
        
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
        System.out.println("You lose");
    }
    
    private boolean checkWin() {
        if (counter <= 25){
            return true;
        }
        else {
            return false;
        }
    }
    
    /*public boolean checkItems() {
        if (garage.hasItem().equals(piece4) && 
    }*/
    
    public void processCommand(Command command) {
        String commandWord = command.getCommandWord().toLowerCase();
        
        switch(commandWord) {
            case "speak":
                System.out.println("you wanted me to speak this word, " + command.getSecondWord());
                break;
            case "go":
                goRoom(command);
                break;
            case "long":
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
                //help(command);
                break;
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
        else{
             player.setItem(item, itemToGrab);
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
        else{
             currentRoom.setItem(playerItem, itemToDrop);
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
        else{
            currentRoom = nextRoom;
            counter++;
            System.out.println("Counter: " + counter);
        }
    }
    
    private String sFrontYard = "You are in the the front yard.";
    private String lFrontYard;
    private String sHallway = "You are in the hallway.";
    private String lHallway;
    private String sLivingRoom = "You are in the living room";
    private String lLivingRoom;
    private String sKitchen = "You are in the kitchen.";
    private String lKitchen;
    private String sBedroom = "You are in the bedroom";
    private String lBedroom;
    private String sGarage = "You are in the garage.";
    private String lGarage;
}
