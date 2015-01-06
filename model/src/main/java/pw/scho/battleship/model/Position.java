package pw.scho.battleship.model;

public class Position {

    private static String[] digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
    private int x;
    private int y;

    public Position() {
    }

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
        return Math.sqrt(Math.pow(Math.abs(x - otherPosition.x), 2) + Math.pow(Math.abs(y - otherPosition.y), 2));
    }
}
