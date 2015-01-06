package pw.scho.battleship.model;

public class Position {

    private static String[] digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return digits[x] + String.valueOf(y + 1);
    }

    public double distanceTo(Position otherPosition) {
        return Math.sqrt(Math.pow(x - otherPosition.x, 2) + Math.pow(y - otherPosition.y, 2));
    }
}
