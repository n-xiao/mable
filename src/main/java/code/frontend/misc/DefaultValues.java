package code.frontend.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class DefaultValues {

    // colours
    public static final Color BACKGROUND_COLOUR = new Color(20,20,30);

    // LaF
    public static final double DEVIATION = 0.02;
    public static final double CORNER_DEVIATION = 0.02;
    public static final double CORNER_OFFSET = 0.2;

    // fonts
    private static String font = null;

    public static void initFonts() {
        if (font != null) return;
        try {
            loadFont("ShantellSans-Regular.ttf");
            loadFont("ShantellSans-Bold.ttf");
            loadFont("ShantellSans-MediumItalic.ttf");
            font = "Shantell Sans";
        } catch (Exception e) {
            System.err.println("Failed to init fonts");
            System.err.println(e.getStackTrace());
        }
        finally {
            font = "Serif"; // uses fallback font (ugly! but doesnt stop program)
        }
    }

    public static void loadFont(String fileName) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        InputStream stream = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream(fileName);
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
        ge.registerFont(font);
    }

    public static String getFontFamily() {
        return font;
    }

    private DefaultValues() {}
}
