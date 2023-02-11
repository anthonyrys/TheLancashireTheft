import core.Data;
import core.Fonts;
import game.Player;
import scenes.Game;
import scenes.MainMenu;

import javax.swing.*;
import java.awt.*;

public class TheTheft {
    public static Player plr;

    public static void exit() {
        Data.save(plr);
        System.out.print("\n\napp_exit");
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(TheTheft::exit));

        EventQueue.invokeLater(() -> {
            plr = Data.load();
            Fonts.init();

            JFrame window = new JFrame("The Lancashire Theft");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            window.setPreferredSize(new Dimension(1280, 720));
            window.getContentPane().setBackground(new Color(24, 24, 24));
            window.setResizable(false);

            window.pack();
            window.setVisible(true);

            new MainMenu(window, plr);
        });
    }
}
