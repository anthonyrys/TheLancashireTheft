package ui;

import game.Item;
import scenes.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class Options {
    public static void displayOptions(int offset, int padding, int yOrigin, Scene scene, UButton[] options, boolean off) {
        int i = 0;
        for (UButton button : options) {
            if (scene.inRoster) {
                button.setLocation(
                        (scene.getWindow().getWidth() / 2) - button.getWidth() / 2 + offset,
                        (-scene.getWindow().getHeight()) + yOrigin + padding * i
                );

            } else {
                button.setLocation(
                        (scene.getWindow().getWidth() / 2) - button.getWidth() / 2 + offset,
                        yOrigin + padding * i
                );

            }

            button.baseLocation = button.getLocation();
            i++;
        }
    }

    public static void displayOptions(int offset, int padding, int yOrigin, Scene scene, UButton[] options) {
        int i = 0;
        for (UButton button : options) {
            if (scene.inRoster) {
                button.setLocation(
                        (scene.getWindow().getWidth() / 2) - button.getWidth() / 2 + offset,
                        (-scene.getWindow().getHeight()) + yOrigin + padding * i
                );

            } else {
                button.setLocation(
                        (scene.getWindow().getWidth() / 2) - button.getWidth() / 2 + offset,
                        yOrigin + padding * i
                );

            }

            button.baseLocation = button.getLocation();
            scene.getSurface().add(button, 2);
            scene.getSurface().revalidate();
            scene.getSurface().repaint();

            i += 1;
        }
    }

    public static void iterateText(Scene scene, ULabel label, LinkedHashMap<String, Integer> texts, int waitTime,
                                   Scene.Method method) {
        new Thread(() -> {
            int time = waitTime;
            for (String s : texts.keySet()) {
                label.setText(s);
                label.setSize(label.getPreferredSize());

                label.setLocation(
                        (scene.getWindow().getWidth() / 2) - label.getWidth() / 2,
                        (scene.getWindow().getHeight() / 2) - (int) (label.getHeight() * 1.3)
                );

                label.set();
                label.update();

                Timer t;
                if (s.equals(texts.keySet().toArray()[texts.size() - 1])) {
                    t = label.typewriter(null, texts.get(s), "mono", true, 2, () -> {});
                    time *= 2;

                } else {
                    t = label.typewriter(null, texts.get(s), "mono", () -> {});

                }
                while (t.isRunning()) {}

                try {
                    TimeUnit.SECONDS.sleep(time);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();

                }
            }

            method.method();
        }).start();
    }

    public static UButton[] displayItems(int offset, int padding, int yOrigin, Scene scene, ArrayList<Item> items) {
        ArrayList<UButton> buttons = new ArrayList<>();

        int i = 0;
        for (Item item : items) {
            UButton button = new UButton(item.id());
            button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 32));
            button.setForeground(new Color(225, 225, 225));
            button.setSize(button.getPreferredSize());
            button.setLocation(
                    (scene.getWindow().getWidth() / 2) - button.getWidth() / 2 + offset,
                    yOrigin + padding * i
            );

            button.set();
            button.update();

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    button.hoverPrompt = new ULabel(item.description() + " ");
                    button.hoverPrompt.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 20));
                    button.hoverPrompt.setForeground(new Color(225, 225, 225));
                    button.hoverPrompt.setOpaque(false);
                    button.hoverPrompt.setSize(button.hoverPrompt.getPreferredSize());
                    button.hoverPrompt.setLocation(
                            button.getLocation().x + button.getWidth() + 35,
                            button.getLocation().y + (int) (button.hoverPrompt.getHeight() * .3)
                    );

                    button.hoverPrompt.set();
                    button.hoverPrompt.update();
                    scene.getSurface().add(button.hoverPrompt, 0);
                }

                public void mouseExited(MouseEvent evt) {
                    button.hoverPrompt.setVisible(false);

                    scene.getSurface().remove(button.hoverPrompt);
                    scene.getSurface().revalidate();
                    scene.getSurface().repaint();

                    button.hoverPrompt = null;
                }
            });

            buttons.add(button);
            i++;
        }

        UButton[] finalButtons = new UButton[buttons.size()];
        return buttons.toArray(finalButtons);
    }
}
