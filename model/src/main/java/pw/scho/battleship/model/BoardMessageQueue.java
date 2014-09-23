package pw.scho.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class BoardMessageQueue {

    private final List<String> messages;

    public BoardMessageQueue() {
        messages = new ArrayList();
    }

    public List<String> getAllMessages() {
        return messages;
    }

    public int size() {
        return messages.size();
    }

    public void addMissMessage(Position position) {
        messages.add(position.toString() + " was a miss.");
    }

    public void addHitMessage(Position position, Ship ship) {
        String message = position.toString() + " was a hit.";
        if (ship.sunk()) {
            message += " Ship sunk!";
        }
        messages.add(message);
    }

}
