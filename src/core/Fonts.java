package core;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Fonts {
    public static final File mainFontPath = new File(System.getProperty("user.dir") + "\\res\\fonts\\Main.otf");

    public static void init() {
        try {
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT, mainFontPath);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(titleFont);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();

        }
    }
}
