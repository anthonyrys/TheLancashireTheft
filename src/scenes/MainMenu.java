package scenes;

import core.Data;
import core.Mixer;
import core.Easings;
import game.Player;
import ui.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MainMenu extends Scene {
    private final Title title;
    private final UButton[] options;
    private final HashMap<String, Boolean> optionDebouncers;
    private final HashMap<String, Boolean> settingOptionDebouncers;
    private final UButton[] settingOptions;

    private final int transitionSpeed = 10;

    public MainMenu(JFrame window, Player plr) {
        super(window, plr);

        this.title = new Title("The Lancashire Theft");
        this.title.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 96));
        this.title.setForeground(new Color(225, 225, 225));
        this.title.setSize(this.title.getPreferredSize());
        this.title.setLocation(
                (this.window.getWidth() / 2) - this.title.getWidth() / 2,
                (this.window.getHeight() / 2) - this.title.getHeight()
        );

        this.title.set();
        this.title.update();

        this.options = new UButton[3];
        this.optionDebouncers = new HashMap<>();
        String[] names = {"play", "settings", "quit"};
        for (int i = 0; i < options.length; i++) {
            optionDebouncers.put(names[i], false);

            UButton button = new UButton(names[i]);
            button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 0));
            button.setForeground(new Color(225, 225, 225));
            button.setSize(button.getPreferredSize());

            button.set();

            button.addActionListener(e -> {
                if (!button.active) return;
                Mixer.playSelectSFX();

                try {
                    java.lang.reflect.Method method = this.getClass().getMethod(button.getText());
                    method.invoke(this);

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();

                }
            });

            options[i] = button;
        }

        this.settingOptions = new UButton[3];
        this.settingOptionDebouncers = new HashMap<>();
        names = new String[]{"music", "sound", "back"};
        for (int i = 0; i < settingOptions.length; i++) {
            settingOptionDebouncers.put(names[i], false);

            UButton button = new UButton(names[i]);

            if (button.getText().equals("back")) {
                button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 32));

            } else {
                button.setFont(new Font("FANTASY MAGIST", Font.PLAIN, 64));

            }

            button.setForeground(new Color(225, 225, 225));
            button.setSize(button.getPreferredSize());

            if (button.getText().equals("music") && !Data.settings.get("music_enabled")) {
                button.setForeground(new Color(75, 75, 75));
            }

            if (button.getText().equals("sound") && !Data.settings.get("sound_enabled")) {
                button.setForeground(new Color(75, 75, 75));
            }

            button.set();

            button.addActionListener(e -> {
                if (!button.active) return;
                Mixer.playSelectSFX();

                try {
                    java.lang.reflect.Method method = this.getClass().getMethod(button.getText());
                    method.invoke(this);

                    if (button.getText().equals("music")) {
                        if (Data.settings.get("music_enabled")) {
                            button.setForeground(new Color(225, 225, 225));

                        } else {
                            button.setForeground(new Color(75, 75, 75));

                        }
                    }

                    if (button.getText().equals("sound")) {
                        if (Data.settings.get("sound_enabled")) {
                            button.setForeground(new Color(225, 225, 225));

                        } else {
                            button.setForeground(new Color(75, 75, 75));

                        }
                    }

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();

                }
            });

            settingOptions[i] = button;
        }

        this.surface.add(this.title, 1);
        this.surface.revalidate();
        this.surface.repaint();

        this.run();
    }

    public void play() {
        Mixer.endMenu();
        for (UButton button : this.options) {
            button.active = false;
            button.easeFontSize(0, 35, Easings.getEasing("easeInQuint"), () -> {});
        }

        this.title.easeThread.stop();
        this.title.active = false;
        this.title.easeFontSize(0, 35, Easings.getEasing("easeInQuint"), () -> {
            this.surface.remove(title);
            for (UButton button : this.options) this.surface.remove(button);
            for (UButton button : this.options) this.surface.remove(button);
            this.surface.revalidate();
            this.surface.repaint();

            this.fadeIn(() -> new Game(window, plr), .005f);
        });
    }

    public void settings() {
        if (this.optionDebouncers.get("settings")) return;
        this.optionDebouncers.put("settings", true);

        title.easeThread.stop();
        title.easeTimer = 0;
        this.title.easePositionX(
                (this.window.getWidth() / 2) - this.title.getWidth() / 2 - this.window.getWidth(),
                transitionSpeed,
                Easings.getEasing("easeOutQuint"),
                () -> {
                    this.title.baseLocation = title.getLocation();
                    this.title.easeThread.start();
                    this.optionDebouncers.put("settings", false);
                }
        );

        for (UButton button : this.options) {
            button.active = false;
            button.update();
            button.easePositionX(
                (this.window.getWidth() / 2) - button.getWidth() / 2 - this.window.getWidth(),
                    transitionSpeed,
                    Easings.getEasing("easeOutQuint"),
                    () -> button.baseLocation = button.getLocation()
            );
        }

        for (UButton button : this.settingOptions) {
            button.update();
            button.easePositionX(
                    (this.window.getWidth() / 2) - button.getWidth() / 2,
                    transitionSpeed,
                    Easings.getEasing("easeOutQuint"),
                    () -> {
                        button.baseLocation = button.getLocation();
                        button.active = true;
                    }
            );
        }
    }

    public void quit() {
        System.exit(0);
    }

    public void music() {
        Data.settings.put("music_enabled", !Data.settings.get("music_enabled"));
        Mixer.onSettingChange();
    }

    public void sound() {
        Data.settings.put("sound_enabled", !Data.settings.get("sound_enabled"));
    }

    public void back() {
        if (this.settingOptionDebouncers.get("back")) return;
        this.settingOptionDebouncers.put("back", true);

        title.easeThread.stop();
        title.easeTimer = 0;
        this.title.easePositionX(
                (this.window.getWidth() / 2) - this.title.getWidth() / 2,
                transitionSpeed,
                Easings.getEasing("easeOutQuint"),
                () -> {
                    this.title.baseLocation = title.getLocation();
                    title.easeThread.start();
                    this.settingOptionDebouncers.put("back", false);
                }
        );

        for (UButton button : this.options) {
            button.update();
            button.easePositionX(
                    (this.window.getWidth() / 2) - button.getWidth() / 2,
                    transitionSpeed,
                    Easings.getEasing("easeOutQuint"),
                    () -> {
                        button.baseLocation = button.getLocation();
                        button.active = true;
                    }
            );
        }

        for (UButton button : this.settingOptions) {
            button.active = false;
            button.update();
            button.easePositionX(
                    (this.window.getWidth() / 2) - button.getWidth() / 2 + window.getWidth(),
                    transitionSpeed,
                    Easings.getEasing("easeOutQuint"),
                    () -> button.baseLocation = button.getLocation()
            );
        }
    }

    private void run() {
        Mixer.playMenu();

        this.title.typewriter(null, 105, null, () -> {
            this.title.easePositionY(
                    (this.window.getHeight() / 2) - ((this.title.getHeight() * 2)),
                    65,
                    Easings.getEasing("easeOutQuint"),
                    () -> {
                        this.title.baseLocation = this.title.getLocation();
                        title.easeThread.start();
                    }
            );

            this.title.active = true;
            for (UButton button : this.options) {
                button.easeFontSize(64, 70, Easings.getEasing("easeOutQuint"), () -> {
                    button.active = true;

                    button.baseFont = button.getFont().getSize();
                    button.baseSize = button.getPreferredSize();
                    button.baseLocation = button.getLocation();
                });
            }

            Options.displayOptions(
                    0,
                    110,
                    (int) (this.window.getHeight() * .45),
                    this,
                    this.options
            );

            Options.displayOptions(
                    this.window.getWidth(),
                    110,
                    (int) (this.window.getHeight() * .35),
                    this,
                    this.settingOptions
            );
        });
    }
}