package pw.scho.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class BoardMessageQueue {

    private final List<String> messages;

    BoardMessageQueue() {
        messages = new ArrayList<>();
    }

    List<String> getAllMessages() {
        return messages;
    }

    public int size() {
        return messages.size();
    }

    void addMissMessage(Position position) {
        messages.add(position.toString() + " was a miss.");
    }

    void addHitMessage(Position position, Ship ship) {
        String message = position.toString() + " was a hit.";
        if (ship.sunk()) {
            message += " Ship sunk!";
        }
        messages.add(message);
    }

}
