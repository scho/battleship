package pw.scho.model;

import java.util.ArrayList;

public class BoardMessageQueue {

    private final ArrayList<String> messages;

    public BoardMessageQueue(){
        messages = new ArrayList();
    }

    public Iterable<String> getAllMessages(){
        return messages;
    }

    public int size(){
        return messages.size();
    }

    public void addMissMessage(Position position){
        messages.add("Your shot at " + position.toString() + " was a miss.");
    }

    public void addHitMessage(Position position, Ship ship){
        String message = "Your shot at " + position.toString() + " was a hit.";
        if(ship.sunk()){
            message += " Ship sunk!";
        }
        messages.add(message);
    }

}
