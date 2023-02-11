package core;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;;

public class Mixer {
    private static final String menuPath = System.getProperty("user.dir") + "\\res\\music\\MenuMusic.wav";
    private static final String introPath = System.getProperty("user.dir") + "\\res\\music\\IntroMusic.wav";
    private static final String checkPath = System.getProperty("user.dir") + "\\res\\music\\CheckpointMusic.wav";
    private static final String creditsPath = System.getProperty("user.dir") + "\\res\\music\\CreditsMusic.wav";
    private static final String mainPath = System.getProperty("user.dir") + "\\res\\music\\MainMusic.wav";
    private static final String firstPath = System.getProperty("user.dir") + "\\res\\music\\FirstMusic.wav";
    private static final String secondPath = System.getProperty("user.dir") + "\\res\\music\\SecondMusic.wav";

    private static final String hoverPath = System.getProperty("user.dir") + "\\res\\sfx\\OptionHover.wav";
    private static final String selectPath = System.getProperty("user.dir") + "\\res\\sfx\\OptionSelect.wav";
    private static final String monoPath = System.getProperty("user.dir") + "\\res\\sfx\\Monologue.wav";

    private static Clip menu;
    private static Clip intro;
    private static Clip check;
    private static Clip main;
    private static Clip credits;

    private static int counter = 0;
    private static boolean stopped;

    public static void onSettingChange() {
        BooleanControl muteControl;

        if (menu != null) {
            muteControl = (BooleanControl) menu.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(!Data.settings.get("music_enabled"));
        }

        if (intro != null) {
            muteControl = (BooleanControl) intro.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(!Data.settings.get("music_enabled"));
        }

        if (check != null) {
            muteControl = (BooleanControl) check.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(!Data.settings.get("music_enabled"));
        }

        if (main != null) {
            muteControl = (BooleanControl) main.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(!Data.settings.get("music_enabled"));
        }

        if (credits != null) {
            muteControl = (BooleanControl) credits.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(!Data.settings.get("music_enabled"));
        }
    }

    private static void easeMusic(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float value = gainControl.getValue();
                if (value >= -20.0f) {
                    ((Timer) e.getSource()).stop();
                }

                gainControl.setValue(value + 1.0f);
            }
        }).start();
    }

    private static void endMusic(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float value = gainControl.getValue();
                gainControl.setValue(value - 2.0f);

                if (value - 2.0f <= -78.0f) {
                    ((Timer) e.getSource()).stop();
                    clip.close();
                }
            }
        }).start();
    }

    public static void playMenu() {
        new Thread(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(menuPath));
                menu = AudioSystem.getClip();
                menu.open(stream);

                FloatControl gainControl = (FloatControl) menu.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);

                onSettingChange();
                menu.loop(Clip.LOOP_CONTINUOUSLY);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();

            }
        }).start();
    }

    public static void endMenu() {
        endMusic(menu);
    }

    public static void playIntro() {
        new Thread(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(introPath));
                intro = AudioSystem.getClip();
                intro.open(stream);

                FloatControl gainControl = (FloatControl) intro.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-80.0f);

                onSettingChange();
                intro.loop(Clip.LOOP_CONTINUOUSLY);
                easeMusic(intro);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();

            }
        }).start();
    }

    public static void endIntro() {
        endMusic(intro);
    }

    public static void playCheckpoint() {
        counter = 0;
        new Thread(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(checkPath));
                check = AudioSystem.getClip();
                check.open(stream);

                FloatControl gainControl = (FloatControl) check.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-80.0f);

                onSettingChange();
                check.loop(Clip.LOOP_CONTINUOUSLY);
                easeMusic(check);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();

            }
        }).start();
    }

    public static void endCheckpoint() {
        endMusic(check);
    }

    public static void playCredits() {
        new Thread(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(creditsPath));
                credits = AudioSystem.getClip();
                credits.open(stream);

                FloatControl gainControl = (FloatControl) credits.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-80.0f);

                onSettingChange();
                credits.loop(Clip.LOOP_CONTINUOUSLY);
                easeMusic(credits);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();

            }
        }).start();
    }

    public static void playMain(boolean checkpoint) {
        stopped = false;
        new Thread(() -> {
            try {
                AudioInputStream stream;

                if (counter == 0) {
                    if (!checkpoint) {
                        stream = AudioSystem.getAudioInputStream(new File(firstPath));
                    } else {
                        stream = AudioSystem.getAudioInputStream(new File(secondPath));
                    }
                } else {
                    stream = AudioSystem.getAudioInputStream(new File(mainPath));
                }

                counter = (counter == 1) ? 0 : counter + 1;

                main = AudioSystem.getClip();
                main.open(stream);

                FloatControl gainControl = (FloatControl) main.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-80.0f);

                onSettingChange();
                main.start();
                easeMusic(main);

                main.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) {
                        if (!stopped) playMain(checkpoint);
                    }
                });

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();

            }
        }).start();
    }

    public static void stopMain() {
        stopped = true;
        main.close();
    }

    private static void playSFX(File path, float volume) {
        if (!Data.settings.get("sound_enabled")) return;

        Clip clip;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.open(stream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.start();

            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();

        }
    }

    public static void playHoverSFX() {
        playSFX(new File(hoverPath), -15.0f);
    }

    public static void playSelectSFX() {
        playSFX(new File(selectPath), -5.0f);
    }

    public static void playMonoSFX() {
        playSFX(new File(monoPath), -8.5f);
    }
}
