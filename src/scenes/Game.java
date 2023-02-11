package scenes;

import core.Easings;
import core.Mixer;
import game.Player;
import game.Room;
import game.Suspect;
import ui.Options;
import ui.UButton;
import ui.ULabel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public class Game extends Scene {
    private final ULabel typer;
    private final ULabel dialogue;

    private final ULabel inventoryFrame;

    private final UButton rosterName;
    private final UButton rosterInvolvement;

    private final ULabel rosterTestimony;

    private final Suspect[] suspects;
    private final UButton[] suspectButtons;
    private final String[] suspectTestimonies;
    private final String[] suspectNames;
    private final String[] suspectInvolves;

    private UButton[] inventoryItems;
    private int suspectIndex = 0;
    private int nameCount = 0;

    public Game(JFrame window, Player plr) {
        super(window, plr);

        this.inRoster = false;
        this.inInventory = false;

        this.typer = new ULabel("1907, Lancashire England");
        this.typer.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 24));
        this.typer.setForeground(new Color(175, 175, 175));

        this.typer.setSize(this.typer.getPreferredSize());
        this.typer.setLocation(
                (this.window.getWidth() / 2) - this.typer.getWidth() / 2,
                (this.window.getHeight() / 2) - this.typer.getHeight()
        );

        this.typer.set();
        this.typer.update();

        this.dialogue = new ULabel("");
        this.dialogue.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 32));
        this.dialogue.setForeground(new Color(225, 225, 225));

        this.dialogue.setSize(this.dialogue.getPreferredSize());
        this.dialogue.setLocation(
                (this.window.getWidth() / 2) - this.dialogue.getWidth() / 2,
                (this.window.getHeight() / 2) - this.dialogue.getHeight()
        );

        this.dialogue.set();
        this.dialogue.update();

        this.suspects = new Suspect[5];
        this.suspects[0] = new Suspect("John Wilkes", "guilty");
        this.suspects[1] = new Suspect("Abigail Wilkes", "innocent");
        this.suspects[2] = new Suspect("Officer Williams", "innocent");
        this.suspects[3] = new Suspect("Arthur Taylor", "guilty");
        this.suspects[4] = new Suspect("Olivia Evans", "innocent");

        this.suspectNames = new String[5];
        for (int i = 0; i < suspects.length; i++) {
            suspectNames[i] = suspects[i].getName();
        }

        this.suspectInvolves = new String[]{"innocent", "guilty"};

        this.suspectTestimonies = new String[5];
        this.suspectTestimonies[0] = "<html>"+
                "\" I have no clue what you are on about, if<br>"+
                "anything you should be questioning the officer.<br>"+
                "I was just visiting this museum with my sister<br>"+
                "and a buddy of mine. He's an expert jewel crafter<br>"+
                "so he fancies valuable jewels. I accidentally tipped<br>"+
                "over a vase and it broke, went to the bathroom to<br>"+
                "go clean, came back and the jewel was gone. \""+
                "</html>";

        this.suspectTestimonies[1] = "<html>"+
                "\" No, I didn't steal the jewel. I was with my<br>"+
                "brother the entire time. The only time otherwise<br>"+
                "was when he broke that vase and rushed off to<br>"+
                "the bathroom. Unusual though, he had no reason<br>"+
                "to, he seemed perfectly fine. However, I never<br>"+
                "did see his friend, I believe he was in the room<br>"+
                "where the jewel was. \""+
                "</html>";


        this.suspectTestimonies[2] = "<html>" +
                "\" There wasn't anything strange, no. I was just<br>"+
                "on my regular duties patrolling the premises. There<br>"+
                "was nobody else here except for the four patrons that<br>"+
                "are here now. Around the time I believe the jewel<br>"+
                "was taken, I heard a loud crash from a vase. I went to<br>"+
                "go check it out. Saw one of them flustered, and<br>"+
                "another heading to the bathroom with something in<br>"+
                "his hand. Sure enough, when I came back, the jewel<br>"+
                "jewel was gone. \""+
                "</html>";


        this.suspectTestimonies[3] = "<html>" +
                "\" Of course not, why would I steal a jewel? Yes<br>"+
                "I am an experienced jewel crafter, but I am not a<br>"+
                "thief. I was with the officer and the other<br>"+
                "patron until the officer ran off to check a noise<br>"+
                "and the other went to examine the other exhibits.<br>"+
                "Though I was too busy to care as I was looking for<br>"+
                "my monocle I misplaced not too long ago. \""+
                "</html>";


        this.suspectTestimonies[4] = "<html>" +
                "\" I couldn't have possibly stole the jewel, I was<br>"+
                "no where near the jewel at the time you say it was <br>"+
                "stolen. The only two people near the jewel was the <br>"+
                "officer and a patron who was supposedly looking for his<br>"+
                "monocle, mumbling something about needing tools from<br>"+
                "the storage room. Another antique wasn't in its stand as<br>"+
                "well. Would be surprised if that got stolen earlier<br>"+
                "in the day. \""+
                "</html>";

        this.suspectButtons = new UButton[this.suspects.length];
        for (int i = 0; i < this.suspectButtons.length; i++) {
            UButton button = new UButton("?????");
            button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 28));
            button.setForeground(new Color(225, 225, 225));
            button.setSize(button.getPreferredSize());

            button.set();
            button.update();

            int finalI = i;
            button.addActionListener(e -> {
                if (!button.active) return;
                Mixer.playSelectSFX();
                button.onHoverExit();

                this.nameCount = 0;

                try {
                    java.lang.reflect.Method method = this.getClass().getMethod("suspect");
                    suspectIndex = finalI;
                    method.invoke(this);

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();

                }
            });

            this.suspectButtons[i] = button;
        }

        this.rosterButton = new UButton("roster ");
        this.rosterButton.srcId = "roster";
        this.rosterButton.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 32));
        this.rosterButton.setForeground(new Color(225, 225, 225));
        this.rosterButton.setSize(this.rosterButton.getPreferredSize());
        this.rosterButton.setLocation(
                (int) (this.window.getWidth() * .9),
                (int) (this.window.getHeight() * .8)
        );

        this.rosterButton.set();
        this.rosterButton.update();

        this.rosterButton.addActionListener(e -> {
            if (!this.rosterButton.active) return;
            Mixer.playSelectSFX();
            this.rosterButton.onHoverExit();

            try {
                java.lang.reflect.Method method = this.getClass().getMethod(this.rosterButton.srcId);
                method.invoke(this);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();

            }
        });

        this.inventoryButton = new UButton("inventory ");
        this.inventoryButton.srcId = "inventory";
        this.inventoryButton.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 32));
        this.inventoryButton.setForeground(new Color(225, 225, 225));
        this.inventoryButton.setSize(this.inventoryButton.getPreferredSize());
        this.inventoryButton.setLocation(
                (int) (this.window.getWidth() * .865),
                (int) (this.window.getHeight() * .875)
        );

        this.inventoryButton.set();
        this.inventoryButton.update();

        this.inventoryFrame = new ULabel("");
        this.inventoryFrame.setOpaque(true);
        this.inventoryFrame.setBackground(new Color(36, 36, 36));
        this.inventoryFrame.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 3));
        this.inventoryFrame.setLocation(-this.window.getWidth(), -3);
        this.inventoryFrame.setSize(
                (int) (this.window.getWidth() * .4),
                (int) (this.window.getHeight() * .6)
        );

        this.inventoryFrame.set();
        this.inventoryFrame.update();

        this.inventoryButton.addActionListener(e -> {
            if (!this.inventoryButton.active) return;
            Mixer.playSelectSFX();
            this.inventoryButton.onHoverExit();

            try {
                java.lang.reflect.Method method = this.getClass().getMethod(this.inventoryButton.srcId);
                method.invoke(this);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();

            }
        });

        this.rosterName = new UButton("?????");
        if (this.plr.reachedCheckpoint) {
            this.rosterName.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 32));

        } else {
            this.rosterName.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 32));

        }

        this.rosterName.setForeground(new Color(225, 225, 225));
        this.rosterName.setSize(this.rosterName.getPreferredSize());
        this.rosterName.setLocation(
                (int) (this.window.getWidth() * .35),
                (-this.window.getHeight()) + (int) (this.window.getHeight() * .2)
        );
        this.rosterName.srcId = "name";

        this.rosterName.set();
        this.rosterName.update();

        this.rosterName.addActionListener(e -> {
            if (!this.rosterName.active) return;
            this.rosterName.onHoverExit();
            if (plr.reachedCheckpoint) return;

            Mixer.playSelectSFX();

            try {
                java.lang.reflect.Method method = this.getClass().getMethod(this.rosterName.srcId);
                method.invoke(this);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();

            }
        });

        this.rosterInvolvement = new UButton("?????");
        this.rosterInvolvement.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 32));
        this.rosterInvolvement.setForeground(new Color(225, 225, 225));
        this.rosterInvolvement.setSize(this.rosterInvolvement.getPreferredSize());
        this.rosterInvolvement.setLocation(
                (int) (this.window.getWidth() * .6),
                (-this.window.getHeight()) + (int) (this.window.getHeight() * .2)
        );
        this.rosterInvolvement.srcId = "involvement";

        this.rosterInvolvement.set();
        this.rosterInvolvement.update();

        this.rosterInvolvement.addActionListener(e -> {
            if (!this.rosterInvolvement.active) return;
            Mixer.playSelectSFX();
            this.rosterInvolvement.onHoverExit();

            try {
                java.lang.reflect.Method method = this.getClass().getMethod(this.rosterInvolvement.srcId);
                method.invoke(this);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();

            }
        });

        this.rosterTestimony = new ULabel(null);
        this.rosterTestimony.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 24));
        this.rosterTestimony.setForeground(new Color(155, 155, 155));
        this.rosterTestimony.setSize(this.rosterTestimony.getPreferredSize());
        this.rosterTestimony.setLocation(
                (int) (this.window.getWidth() * .35) - this.rosterTestimony.getWidth() / 2,
                (-this.window.getHeight()) + (int) (this.window.getHeight() * .3)
        );

        this.rosterTestimony.set();
        this.rosterTestimony.update();

        this.run();
    }

    public void suspect() {
        this.rosterTestimony.setText(this.suspectTestimonies[this.suspectIndex]);
        this.rosterTestimony.setSize(this.rosterTestimony.getPreferredSize());
        this.rosterTestimony.set();
        this.rosterTestimony.update();

        this.rosterName.setText(this.plr.getSuspects()[this.suspectIndex].getName());
        this.rosterName.setSize(this.rosterName.getPreferredSize());
        this.rosterName.set();
        this.rosterName.update();

        this.rosterInvolvement.setText(this.plr.getSuspects()[this.suspectIndex].getInvolvement());
        if (this.rosterInvolvement.getText().equals("innocent")) {
            this.rosterInvolvement.setForeground(new Color(175, 255, 175));

        } else if (this.rosterInvolvement.getText().equals("guilty")) {
            this.rosterInvolvement.setForeground(new Color(255, 175, 175));

        } else {
            this.rosterInvolvement.setForeground(new Color(225, 225, 225));

        }

        this.rosterInvolvement.setSize(this.rosterInvolvement.getPreferredSize());
        this.rosterInvolvement.set();
        this.rosterInvolvement.update();

        for (int i = 0; i < suspectButtons.length; i++) {
            suspectButtons[i].setText(this.plr.getSuspects()[i].getName());
            suspectButtons[i].setSize(this.suspectButtons[i].getPreferredSize());
            suspectButtons[i].set();
            suspectButtons[i].update();
        }

        Options.displayOptions(
                (int) (-this.window.getWidth() * .35),
                60,
                this.window.getHeight() + (int) (this.window.getHeight() * .3),
                this,
                this.suspectButtons,
                false
        );

        if (!plr.reachedCheckpoint) {
            boolean gotNames = true;
            for (int i = 0; i < this.plr.getSuspects().length; i++) {
                if (!this.plr.getSuspects()[i].getName().equals(suspects[i].getName())) gotNames = false;
            }

            if (gotNames) {
                this.startCheckpoint();
            }
        } else {
            boolean gotInvolv = true;
            for (int i = 0; i < this.plr.getSuspects().length; i++) {
                if (!this.plr.getSuspects()[i].getInvolvement().equals(suspects[i].getInvolvement())) gotInvolv = false;
            }

            if (gotInvolv) {
                this.startCredits();
            }
        }
    }

    public void name() {
        this.plr.getSuspects()[this.suspectIndex].setName(this.suspectNames[this.nameCount]);
        this.nameCount = (this.nameCount == 4) ? 0 : this.nameCount + 1;

        this.suspect();
    }

    public void involvement() {
        int i = 0;
        i = (this.plr.getSuspects()[this.suspectIndex].getInvolvement().equals("innocent")) ? 1 : 0;
        this.plr.getSuspects()[this.suspectIndex].setInvolvement(this.suspectInvolves[i]);

        this.suspect();
    }

    public void roster() {
        int transitionSpeed = 10;
        this.rosterButton.active = false;

        if (!this.inRoster) {
            this.inRoster = true;
            for (UButton button : this.currentRoom.getButtons()) {
                button.active = false;
                button.easePositionY(
                        button.baseLocation.y + this.window.getHeight(),
                        transitionSpeed,
                        Easings.getEasing("easeOutCubic"),
                        () -> {
                            button.set();
                            button.update();
                            button.active = true;
                        }
                );
            }

            for (UButton button : this.suspectButtons) {
                button.active = false;
                button.easePositionY(
                        button.baseLocation.y + this.window.getHeight(),
                        transitionSpeed,
                        Easings.getEasing("easeOutCubic"),
                        () -> {
                            button.set();
                            button.update();
                            button.active = true;
                        }
                );
            }

            this.currentRoom.getPrompt().easePositionY(
                    this.currentRoom.getPrompt().baseLocation.y + this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.currentRoom.getPrompt().set();
                        this.currentRoom.getPrompt().update();
                        this.currentRoom.getPrompt().active = true;
                    }
            );

            rosterName.active = false;
            rosterName.easePositionY(
                    rosterName.baseLocation.y + this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterName.set();
                        this.rosterName.update();
                        rosterName.active = true;
                    }
            );

            rosterInvolvement.active = false;
            rosterInvolvement.easePositionY(
                    rosterInvolvement.baseLocation.y + this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterInvolvement.set();
                        this.rosterInvolvement.update();
                        rosterInvolvement.active = true;
                    }

            );

            rosterTestimony.easePositionY(
                    rosterTestimony.baseLocation.y + this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterTestimony.set();
                        this.rosterTestimony.update();

                        this.rosterButton.active = true;
                    }

            );
        } else {
            this.inRoster = false;

            for (UButton button : this.currentRoom.getButtons()) {
                button.active = false;
                button.easePositionY(
                        button.baseLocation.y - this.window.getHeight(),
                        transitionSpeed,
                        Easings.getEasing("easeOutCubic"),
                        () -> {
                            button.set();
                            button.update();
                            button.active = true;
                        }
                );
            }

            for (UButton button : this.suspectButtons) {
                button.active = false;
                button.easePositionY(
                        button.baseLocation.y - this.window.getHeight(),
                        transitionSpeed,
                        Easings.getEasing("easeOutCubic"),
                        () -> {
                            button.set();
                            button.update();
                            button.active = true;
                        }
                );
            }

            this.currentRoom.getPrompt().easePositionY(
                    this.currentRoom.getPrompt().baseLocation.y - this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.currentRoom.getPrompt().set();
                        this.currentRoom.getPrompt().update();
                        this.currentRoom.getPrompt().active = true;
                    }
            );

            rosterName.active = false;
            rosterName.easePositionY(
                    rosterName.baseLocation.y - this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterName.set();
                        this.rosterName.update();
                        rosterName.active = true;
                    }
            );

            rosterInvolvement.active = false;
            rosterInvolvement.easePositionY(
                    rosterInvolvement.baseLocation.y - this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterInvolvement.set();
                        this.rosterInvolvement.update();
                        rosterInvolvement.active = true;
                    }

            );

            rosterTestimony.easePositionY(
                    rosterTestimony.baseLocation.y - this.window.getHeight(),
                    transitionSpeed,
                    Easings.getEasing("easeOutCubic"),
                    () -> {
                        this.rosterTestimony.set();
                        this.rosterTestimony.update();

                        this.rosterButton.active = true;
                    }

            );
        }
    }

    public void inventory() {
        this.inventoryButton.active = false;
        if (!this.inInventory) {
            this.inInventory = true;

            this.inventoryItems = Options.displayItems(
                    (int) -(this.window.getSize().width * .4),
                    55,
                    30,
                    this,
                    this.plr.getItems()
            );

            this.inventoryFrame.easePositionX(-3, 50, Easings.getEasing("easeOutQuint"), () -> {
                this.inventoryButton.active = true;
                this.inventoryFrame.set();
                this.inventoryFrame.update();

                for (UButton button : this.inventoryItems) {
                    this.surface.add(button, 0);
                    button.active = true;

                    button.onHoverEnter(false);
                    button.onHoverExit();
                }
            });

        } else {
            this.inInventory = false;

            for (UButton button : this.inventoryItems) {
                button.active = false;
                button.setVisible(false);

                this.surface.remove(button);
            }

            this.surface.revalidate();
            this.surface.repaint();

            this.inventoryFrame.easePositionX(
                    -this.window.getWidth(),
                    50,
                    Easings.getEasing("easeOutSine"),
                    () -> {
                        this.inventoryButton.active = true;
                        this.inventoryFrame.set();
                        this.inventoryFrame.update();
                    }
            );
        }
    }

    private void startCheckpoint() {
        for (UButton button : this.currentRoom.getButtons()) {
            button.active = false;
            button.setVisible(false);
        }

        for (UButton button : this.suspectButtons) {
            button.active = false;
            button.setVisible(false);
        }

        this.currentRoom.getPrompt().setVisible(false);

        rosterName.active = false;
        rosterName.setVisible(false);
        rosterInvolvement.active = false;
        rosterInvolvement.setVisible(false);
        rosterTestimony.setVisible(false);

        rosterButton.active = false;
        rosterButton.setVisible(false);

        inventoryButton.active = false;
        inventoryButton.setVisible(false);

        LinkedHashMap<String, Integer> checkpointText = new LinkedHashMap<>();
        checkpointText.put("well done.", 100);
        checkpointText.put("you have deduced their identities.", 60);
        checkpointText.put("all that is left is to determine their involvement in the crime.", 55);
        checkpointText.put("use their testimonies and the evidence you gather to your advantage.", 55);
        checkpointText.put("keep in mind that there may be more than one culprit.", 60);
        checkpointText.put("good luck.", 125);

        Mixer.stopMain();
        Mixer.playCheckpoint();
        this.fadeIn(
                () -> fadeOut(2, () -> {
                    this.surface.add(dialogue);
                    Options.iterateText(this, this.dialogue, checkpointText, 2, () -> {
                        Mixer.endCheckpoint();
                        surface.remove(this.dialogue);
                        surface.revalidate();
                        surface.repaint();

                        this.plr.reachedCheckpoint = true;
                        this.rosterName.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 32));
                        this.rosterName.set();
                        this.rosterName.update();

                        fadeIn(() -> fadeOut(3, () -> {
                            for (UButton button : this.currentRoom.getButtons()) {
                                button.active = true;
                                button.setVisible(true);
                            }

                            for (UButton button : this.suspectButtons) {
                                button.active = true;
                                button.setVisible(true);
                            }

                            this.currentRoom.getPrompt().setVisible(true);
                            rosterName.active = true;
                            rosterName.setVisible(true);
                            rosterInvolvement.active = true;
                            rosterInvolvement.setVisible(true);
                            rosterTestimony.setVisible(true);

                            rosterButton.active = true;
                            rosterButton.setVisible(true);

                            inventoryButton.active = true;
                            inventoryButton.setVisible(true);

                            Mixer.playMain(plr.reachedCheckpoint);
                        }), .003f);
                    });
        }), .005f);
    }

    private void startCredits() {
        for (UButton button : this.currentRoom.getButtons()) {
            button.active = false;
            button.setVisible(false);
        }

        for (UButton button : this.suspectButtons) {
            button.active = false;
            button.setVisible(false);
        }

        this.currentRoom.getPrompt().setVisible(false);

        rosterName.active = false;
        rosterName.setVisible(false);
        rosterInvolvement.active = false;
        rosterInvolvement.setVisible(false);
        rosterTestimony.setVisible(false);

        rosterButton.active = false;
        rosterButton.setVisible(false);

        inventoryButton.active = false;
        inventoryButton.setVisible(false);

        LinkedHashMap<String, Integer> creditsText = new LinkedHashMap<>();
        creditsText.put("excellent work detective.", 75);
        creditsText.put("john wilkes and arthur taylor were the culprits.", 55);
        creditsText.put("you have done a great service today.", 60);
        creditsText.put("head back to the office so you can receive your payment.", 50);
        creditsText.put("well done.", 125);

        Mixer.stopMain();
        Mixer.playCredits();
        this.fadeIn(
                () -> fadeOut(2, () -> {
                    this.surface.add(dialogue);
                    Options.iterateText(this, this.dialogue, creditsText, 2, () -> {
                        surface.remove(this.dialogue);
                        surface.revalidate();
                        surface.repaint();

                        typer.setText("the end.");
                        typer.setSize(typer.getPreferredSize());
                        this.typer.setLocation(
                                (this.window.getWidth() / 2) - this.typer.getWidth() / 2,
                                (this.window.getHeight() / 2) - this.typer.getHeight()
                        );

                        typer.set();
                        typer.update();

                        surface.add(typer);
                        typer.typewriter(null, 200, "mono", plr::wipe);
                    });
                }), .005f);
    }

    private void intro() {
        LinkedHashMap<String, Integer> introText = new LinkedHashMap<>();
        introText.put("detective.", 100);
        introText.put("we have received a report of a theft in downtown lancashire.", 60);
        introText.put("a valuable jewel from the museum has been stolen.", 70);
        introText.put("all of the potential suspects have been apprehended.", 70);
        introText.put("we need you to deduce their identities and involvement in the crime.", 50);
        introText.put("both their testimonies and access to the crime scene will be given to you.", 50);
        introText.put("good luck.", 125);

        Mixer.playIntro();
        this.typer.typewriter(null, 150, "mono", true, 2, () ->
                        Options.iterateText(this, this.dialogue, introText, 2, () -> {
                            Mixer.endIntro();
                            surface.remove(this.dialogue);
                            surface.revalidate();
                            surface.repaint();

                            this.plr.doneIntroduction = true;
                            fadeIn(this::play, .003f);
                        }));

        this.surface.add(typer, 0);
        this.surface.add(dialogue, 0);
    }

    private void play() {
        Mixer.playMain(plr.reachedCheckpoint);
        if (this.plr.getRoom() == null) this.plr.setRoom("Entrance");
        Room r = Room.getRoom(plr.getRoom(), this.plr, this);
        assert r != null;

        this.fadeOut(2, () -> {
            r.update(true);
            this.surface.add(this.rosterTestimony, 2);
            this.surface.add(this.inventoryFrame, 1);

            this.rosterInvolvement.active = true;
            this.surface.add(this.rosterInvolvement, 2);

            this.rosterName.active = true;
            this.surface.add(this.rosterName, 2);
            this.suspect();

            Options.displayOptions(
                    (int) (-this.window.getWidth() * .35),
                    60,
                    -this.window.getHeight() + (int) (this.window.getHeight() * .3),
                    this,
                    this.suspectButtons
            );
        });
    }

    private void run() {
        if (!plr.doneIntroduction) {
            this.fadeOut(1, this::intro);

        } else {
            play();

        }
    }
}
