package carloslobo.com.finalproject.Modules;

/**
 * Created by camilo on 11/13/15.
 */
public class Letter {
    private String LetterId;
    private String Letter;
    private int Entries;
    private boolean Locked;


    public Letter(String letterId, String letter, int entries, boolean locked) {
        LetterId = letterId;
        Letter = letter;
        Entries = entries;
        Locked = locked;
    }

    public String getLetter() {
        return Letter;
    }

    public int getEntries() {
        return Entries;
    }

    public boolean isLocked() {
        return Locked;
    }

    public String getLetterId() {
        return LetterId;
    }
}
