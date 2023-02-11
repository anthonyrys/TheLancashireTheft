package ui;

import javax.swing.*;
import java.awt.*;

public class Title extends ULabel {
    public Timer easeThread;
    public int easeTimer = 0;

    public Title(String text) {
        super(text);

        easeThread = new Timer(1, e -> {
            double wave = (Math.sin(easeTimer * .02)) * 12;
            setLocation(new Point(baseLocation.x, baseLocation.y + (int) wave));

            easeTimer += 1;
        });
    }
}
