package pw.scho.battleship.core;

public class Transaction {

    public static Transaction getInstance() {
        return Singleton.INSTANCE.transaction;
    }

    private enum Singleton {

        INSTANCE;

        private final Transaction transaction;

        private Singleton() {
            transaction = new Transaction();
        }
    }
}