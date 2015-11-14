package carloslobo.com.finalproject.Modules;

/**
 * Created by camilo on 11/13/15.
 */
public class ClassLetter {
    private String Letter;
    private int Entries;
    private boolean Locked;


    public ClassLetter(String letter, int entries, boolean locked) {
        Letter = letter;
        Entries = entries;
        Locked = locked;
    }

    public String getLetter() {
        return Letter;
    }

    public void setLetter(String letter) {
        Letter = letter;
    }

    public int getEntries() {
        return Entries;
    }

    public void setEntries(int entries) {
        Entries = entries;
    }

    public boolean isLocked() {
        return Locked;
    }

    public void setLocked(boolean locked) {
        Locked = locked;
    }
}
