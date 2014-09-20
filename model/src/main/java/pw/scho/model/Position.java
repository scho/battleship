package pw.scho.model;

public class Position {

    private final int x;
    private final int y;
    private static String[] digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

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

    public String toString(){
        return digits[x] + String.valueOf(y + 1);
    }
}
