package game;

public class Suspect {
    private String name;
    private String involvement;

    public Suspect() {
        this.name = null;
        this.involvement = null;
    }

    public Suspect(String name, String involvement) {
        this.name = name;
        this.involvement = involvement;
    }

    public String getName() {
        return this.name;
    }

    public String getInvolvement() {
        return this.involvement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInvolvement(String involvement) {
        this.involvement = involvement;
    }
}
