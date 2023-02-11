package scenes;

import game.Player;
import game.Room;
import ui.UButton;
import ui.ULabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Scene {
    public interface Method {
        void method();
    }

    protected final Player plr;
    protected final JFrame window;
    protected final JLayeredPane surface;

    public Room currentRoom;

    public UButton rosterButton;
    public UButton inventoryButton;

    public boolean inRoster;
    public boolean inInventory;

    private boolean displayingNotification = false;

    public Scene(JFrame window, Player plr) {
        this.rosterButton = null;
        this.inventoryButton = null;

        this.window = window;
        this.surface = window.getLayeredPane();
        this.plr = plr;
    }

    public JFrame getWindow() {
        return this.window;
    }

    public JLayeredPane getSurface() {
        return this.surface;
    }

    public void displayNotification(String text) {
        if (displayingNotification) return;

        displayingNotification = true;
        ULabel notif = new ULabel(text);;
        notif.setFont(new Font("FANTASY MAGIST", Font.ITALIC, 24));
        notif.setForeground(new Color(125, 125, 255));
        notif.setSize(notif.getPreferredSize());
        notif.setLocation(
                (this.window.getWidth() / 2) - (notif.getWidth() / 2),
                (this.window.getHeight()) - (int) (this.window.getHeight() * .15)
        );

        notif.set();
        notif.update();
        this.surface.add(notif, 1);

        new Thread(() ->
                notif.typewriter(
                        null,
                        25,
                        null,
                        true,
                        2,
                        () -> {
                            surface.remove(notif);
                            surface.revalidate();
                            surface.repaint();
                            displayingNotification = false;
                        }
        )).start();
    }

    protected void fadeIn(Scene.Method method, float interval) {
        JLabel fade = new JLabel();
        fade.setOpaque(true);
        fade.setBackground(new Color(0, 0, 0, 0f));
        fade.setSize(this.window.getSize());
        fade.setLocation(new Point(0, 0));
        this.surface.add(fade, 0);

        new Timer(1, new ActionListener() {
            float count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                fade.setBackground(new Color(0, 0, 0, count));
                count += interval;

                if (count >= 1.0f) {
                    method.method();
                    surface.remove(fade);
                    surface.revalidate();
                    surface.repaint();

                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    protected void fadeOut(int speed, Scene.Method method) {
        JLabel fade = new JLabel();
        fade.setOpaque(true);
        fade.setBackground(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        fade.setSize(this.window.getSize());
        fade.setLocation(new Point(0, 0));
        this.surface.add(fade, 0);

        new Timer(1, new ActionListener() {
            float count = 1.0f;
            float colorCount = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                fade.setBackground(new Color(colorCount, colorCount, colorCount, count));
                count -= .005f * speed;
                colorCount += .00062f * speed;

                if (count <= 0.0f) {
                    method.method();
                    surface.remove(fade);
                    surface.revalidate();
                    surface.repaint();

                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }
}
