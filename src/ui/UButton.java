package ui;

import core.Easings;
import core.Mixer;
import scenes.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UButton extends JButton {
    public boolean active;
    public String srcId;

    public int baseFont;
    public String baseFontName;
    public int baseFontStyle;

    public Dimension baseSize;
    public Point baseLocation;

    public ULabel hoverPrompt;

    public UButton(String text) {
        super(text);
        this.active = false;

        setOpaque(true);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                onHoverEnter();
            }

            public void mouseExited(MouseEvent evt) {
                onHoverExit();
            }
        });
    }

    public void set() {
        this.baseFontStyle = this.getFont().getStyle();
        this.baseFontName = this.getFont().getName();
        this.baseFont = this.getFont().getSize();

        this.baseLocation = this.getLocation();
        this.baseSize = this.getSize();
    }

    public void update() {
        this.setSize(this.baseSize);
        this.setLocation(this.baseLocation);
        this.setFont(new Font(this.baseFontName, this.baseFontStyle, this.baseFont));
    }

    public void easeFontSize(int size, int endTime, Method easing, Scene.Method callback) {
        int baseFont = this.baseFont;

        new Timer(1, new AbstractAction() {
            int time = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tweenOffset = (double) easing.invoke(null, (double) time / endTime);
                    int sizeTween = (int) ((size - baseFont) * tweenOffset);

                    setFont((new Font(baseFontName, baseFontStyle, baseFont + sizeTween)));
                    setSize(getPreferredSize());

                    setLocation(
                            baseLocation.x - (getSize().width - baseSize.width) / 2,
                            baseLocation.y - (getSize().height - baseSize.height) / 2
                    );

                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();

                }

                time += 1;
                if (time >= endTime) {
                    callback.method();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public void easePositionX(int xLocation, int endTime, Method method, Scene.Method callback) {
        final int baseX = this.baseLocation.x;
        final int goalX = xLocation;

        new Timer(1, new AbstractAction() {
            int time = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tweenOffset = (double) method.invoke(null, (double) time / endTime);
                    double xPos = baseX + ((goalX - baseX) * tweenOffset);
                    setLocation((int) xPos, baseLocation.y);

                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();

                }

                time += 1;
                if (time >= endTime) {
                    setLocation(goalX, baseLocation.y);
                    callback.method();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public void easePositionY(int yLocation, int endTime, Method method, Scene.Method callback) {
        final int baseY = this.baseLocation.y;
        final int goalY = yLocation;

        new Timer(1, new AbstractAction() {
            int time = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tweenOffset = (double) method.invoke(null, (double) time / endTime);
                    double yPos = baseY + ((goalY - baseY) * tweenOffset);
                    setLocation(baseLocation.x, (int) yPos);

                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();

                }

                time += 1;
                if (time >= endTime) {
                    setLocation(baseLocation.x, goalY);
                    callback.method();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public void onHoverEnter() {
        if (!this.active) return;

        int baseFont = this.baseFont;
        int newFont = (int) (baseFont * 1.2);

        Mixer.playHoverSFX();
        new Timer(1, new AbstractAction() {
            int time = 0;
            final int endTime = 10;

            final Method method = Easings.getEasing("easeOutQuint");
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tweenOffset = (double) method.invoke(null, (double) time / endTime);
                    int sizeTween = (int) ((newFont - baseFont) * tweenOffset);

                    setFont((new Font(baseFontName, baseFontStyle, baseFont + sizeTween)));
                    setSize(getPreferredSize());

                    setLocation(
                            baseLocation.x - (getSize().width - baseSize.width) / 2,
                            baseLocation.y - (getSize().height - baseSize.height) / 2
                    );

                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();

                }

                time += 1;
                if (time >= endTime) {
                    ((Timer) e.getSource()).stop();
                }

                if (getMousePosition() == null) {
                    onHoverExit();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public void onHoverEnter(boolean debouncer) {
        if (!this.active) return;

        int baseFont = this.baseFont;
        int newFont = (int) (baseFont * 1.2);

        new Timer(1, new AbstractAction() {
            int time = 0;
            final int endTime = 10;

            final Method method = Easings.getEasing("easeOutQuint");
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tweenOffset = (double) method.invoke(null, (double) time / endTime);
                    int sizeTween = (int) ((newFont - baseFont) * tweenOffset);

                    setFont((new Font(baseFontName, baseFontStyle, baseFont + sizeTween)));
                    setSize(getPreferredSize());

                    setLocation(
                            baseLocation.x - (getSize().width - baseSize.width) / 2,
                            baseLocation.y - (getSize().height - baseSize.height) / 2
                    );

                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();

                }

                time += 1;
                if (time >= endTime) {
                    ((Timer) e.getSource()).stop();
                }

                if (getMousePosition() == null) {
                    onHoverExit();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public void onHoverExit() {
        if (!this.active) return;

        this.setFont(new Font(baseFontName, baseFontStyle, baseFont));
        this.setSize(baseSize);
        this.setLocation(baseLocation);
    }
}
