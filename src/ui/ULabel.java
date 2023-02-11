package ui;

import core.Mixer;
import scenes.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ULabel extends JLabel {
    public boolean active;

    public int baseFont;
    public String baseFontName;
    public int baseFontStyle;

    public Dimension baseSize;
    public Point baseLocation;

    public ULabel(String text) {
        super(text);
        this.active = false;
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

    private void typewriterReverse(String sound, int time, int reverseWait, Scene.Method callback) {
        String text = this.getText();

        new Timer((int) (time * .25), new AbstractAction() {
            int index = text.length() - 1;
            boolean done = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!done) {
                    try {
                        TimeUnit.SECONDS.sleep(reverseWait);
                        done = true;

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();

                    }
                }

                if (sound != null && text.charAt(index) != ' ') {
                    if (sound.equals("mono")) Mixer.playMonoSFX();
                }
                setText(text.substring(0, index));
                index -= 1;

                if (index <= 0) {
                    setText("");
                    callback.method();
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    public Timer typewriter(String str, int time, String sound, Scene.Method callback) {
        String text = (str == null) ? this.getText() : str;
        this.setText("");

        Timer timer = new Timer(time, new AbstractAction() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (sound != null && text.charAt(index) != ' ') {
                    if (sound.equals("mono")) Mixer.playMonoSFX();
                }

                setText(getText() + text.charAt(index));
                index += 1;

                if (index >= text.length()) {
                    callback.method();
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
        return timer;
    }

    public Timer typewriter(String str, int time, String sound, boolean reverse, int reverseWait, Scene.Method callback) {
        String text = (str == null) ? this.getText() : str;
        this.setText("");

        Timer timer = new Timer(time, new AbstractAction() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (sound != null && text.charAt(index) != ' ') {
                    if (sound.equals("mono")) Mixer.playMonoSFX();
                }

                setText(getText() + text.charAt(index));
                index += 1;

                if (index >= text.length()) {
                    if (reverse) typewriterReverse(sound, time, reverseWait, callback);
                    else callback.method();

                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
        return timer;
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
}
