package testapp.game;

public class Arena {

    ArenaParticipant arenaParticipant1;
    ArenaParticipant arenaParticipant2;

    public Arena (ArenaParticipant arenaParticipant1, ArenaParticipant arenaParticipant2) {
        new Thread(() -> {

        });
    }

    public void start() {

    }

    public ArenaParticipant stop() {
        ArenaParticipant winner = new ArenaParticipant(null);
        return winner;
    }
}
