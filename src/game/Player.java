package game;

import java.util.ArrayList;

public class Player {
    public boolean doneIntroduction;
    public boolean reachedCheckpoint;

    private String room;
    private Suspect[] suspects;
    private ArrayList<Item> items;
    private ArrayList<String> flags;

    public Player() {
        this.doneIntroduction = false;
        this.reachedCheckpoint = false;

        this.room = null;

        this.suspects = new Suspect[5];
        this.suspects[0] = new Suspect("?????", "???");
        this.suspects[1] = new Suspect("?????", "???");
        this.suspects[2] = new Suspect("?????", "???");
        this.suspects[3] = new Suspect("?????", "???");
        this.suspects[4] = new Suspect("?????", "???");

        this.items = new ArrayList<>();
        this.flags = new ArrayList<>();
    }

    public void wipe() {
        this.doneIntroduction = false;
        this.reachedCheckpoint = false;

        this.room = null;

        this.suspects = new Suspect[5];
        this.suspects[0] = new Suspect("?????", "???");
        this.suspects[1] = new Suspect("?????", "???");
        this.suspects[2] = new Suspect("?????", "???");
        this.suspects[3] = new Suspect("?????", "???");
        this.suspects[4] = new Suspect("?????", "???");

        this.items = new ArrayList<>();
        this.flags = new ArrayList<>();
    }

    public String getRoom() {
        return this.room;
    }

    public Suspect[] getSuspects() {
        return this.suspects;
    }

    public ArrayList<Item> getItems() {
        return this.items;
    }

    public ArrayList<String> getFlags() {
        return this.flags;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSuspects(Suspect[] suspects) {
        this.suspects = suspects;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setFlags(ArrayList<String> flags) {
        this.flags = flags;
    }

    public void addItem(Item item) {
        if (this.items.contains(item)) return;
        this.items.add(item);
    }

    public void addFlag(String flag) {
        if (this.flags.contains(flag)) return;
        this.flags.add(flag);
    }

    public boolean getItem(String item) {
        for (Item i : this.items) {
            if (i.id().equals(item)) return true;
        }

        return false;
    }

    public boolean getFlag(String flag) {
        return (this.flags.contains(flag));
    }
}
