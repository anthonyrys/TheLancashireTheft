package game;

import core.Easings;
import core.Mixer;
import scenes.Scene;
import ui.UButton;
import ui.ULabel;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class Room {
    protected String name;
    protected String promptString;
    protected String[] buttonNames;
    protected final String[] methods = {"a", "b", "c", "d", "e"};

    protected ULabel prompt;
    protected ArrayList<UButton> buttons;

    protected Player player;
    protected Scene scene;

    protected int padding = 60;

    public Room(String name, String prompt) {
        this.name = name;
        this.promptString = prompt;
    }

    public static Room getRoom(String name, Player player, Scene scene) {
        try {
            Room room = (Room) Class.forName(String.format("game.%s", name)).getDeclaredConstructor().newInstance();
            room.player = player;
            room.scene = scene;

            return room;

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;

        }
    }

    protected void transition(Room room) {
        ULabel transition = new ULabel("");
        transition.setOpaque(true);
        transition.setBackground(new Color(0, 0, 0));
        transition.setSize(this.scene.getWindow().getSize());
        transition.setLocation(new Point(-this.scene.getWindow().getWidth(), 0));
        transition.set();
        transition.update();

        this.scene.getSurface().add(transition, 0);
        transition.easePositionX(0, 10, Easings.getEasing("easeOutCubic"), () -> {
            this.scene.getSurface().remove(this.prompt);
            for (UButton button : this.buttons) {
                this.scene.getSurface().remove(button);
            }

            transition.set();
            transition.update();
            room.update(false);

            transition.easePositionX(
                    this.scene.getWindow().getWidth(),
                    10,
                    Easings.getEasing("easeOutCubic"), () -> {
                        this.scene.getSurface().remove(transition);
                        this.scene.getSurface().revalidate();
                        this.scene.getSurface().repaint();

                        room.activateButtons();
                    });
        });
    }

    protected void transitionReverse(Room room) {
        ULabel transition = new ULabel("");
        transition.setOpaque(true);
        transition.setBackground(new Color(0, 0, 0));
        transition.setSize(this.scene.getWindow().getSize());
        transition.setLocation(new Point(this.scene.getWindow().getWidth(), 0));
        transition.set();
        transition.update();

        this.scene.getSurface().add(transition, 0);
        transition.easePositionX(0, 10, Easings.getEasing("easeOutCubic"), () -> {
            this.scene.getSurface().remove(this.prompt);
            for (UButton button : this.buttons) {
                this.scene.getSurface().remove(button);
            }

            transition.set();
            transition.update();
            room.update(false);

            transition.easePositionX(
                    -this.scene.getWindow().getWidth(),
                    10,
                    Easings.getEasing("easeOutCubic"), () -> {
                        this.scene.getSurface().remove(transition);
                        this.scene.getSurface().revalidate();
                        this.scene.getSurface().repaint();

                        room.activateButtons();
                    });
        });
    }

    protected void activateButtons() {
        for (UButton button : this.buttons) {
            String[] str = button.getText().split("]");
            if (str[0].equals("???")) continue;

            str = button.getText().split("-");
            if (str[0].equals("???")) continue;

            button.active = true;
        }
    }

    protected void deactivateButtons() {
        for (UButton button : this.buttons) button.active = false;
    }

    protected void displayButtons() {
        for (int i = 0; i < this.buttonNames.length; i++) {
            UButton button = new UButton(buttonNames[i]);
            button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 36));
            button.setForeground(new Color(225, 225, 225));

            if (button.getText().equals("back")) {
                button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 26));
                button.setForeground(new Color(125, 125, 125));
            }

            String[] str = button.getText().split("]");
            if (str.length != 1) {
                if (!this.player.getFlag(str[2])) {
                    button.setText(str[0]);
                    button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 36));
                    button.setForeground(new Color(125, 125, 125));

                } else {
                    button.setText(str[1]);
                    button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 36));
                    button.setForeground(new Color(175, 175, 255));

                }
            }

            str = button.getText().split("-");
            if (str.length != 1) {
                if (!this.player.getItem(str[2])) {
                    button.setText(str[0]);
                    button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 36));
                    button.setForeground(new Color(125, 125, 125));

                } else {
                    button.setText(str[1]);
                    button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 36));
                    button.setForeground(new Color(175, 175, 255));

                }
            }

            button.setSize(button.getPreferredSize());
            button.set();

            int finalI = i;
            button.addActionListener(e -> {
                if (!button.active) return;
                Mixer.playSelectSFX();

                try {
                    java.lang.reflect.Method method = this.getClass().getMethod(this.methods[finalI]);
                    method.invoke(this);

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();

                }
            });

            this.buttons.add(button);
            this.scene.getSurface().add(button);
        }
    }

    public String getName() {
        return this.name;
    }

    public ULabel getPrompt() {
        return this.prompt;
    }

    public ArrayList<UButton> getButtons() {
        return this.buttons;
    }

    public void update(boolean type) {
        this.scene.currentRoom = this;
        this.player.setRoom(this.name);
    }

    public void a() {}

    public void b() {}

    public void c() {
    }

    public void d() {}

    public void e() {}
}