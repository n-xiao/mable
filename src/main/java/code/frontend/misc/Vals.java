package code.frontend.misc;

import java.io.InputStream;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Vals {

    public final class Colour {
        public static final Color BACKGROUND = Color.rgb(20, 20, 30);
    }

    public final class GraphicalUI {
        public static final int PREF_WIDTH = 1120;
        public static final int PREF_HEIGHT = 730;
        public static final int MIN_WIDTH = 620;
        public static final int MIN_HEIGHT = 430;
        public static final double DEVIATION = 0.02;
        public static final double CORNER_DEVIATION = 0.02;
        public static final double CORNER_OFFSET = 0.2;
    }


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
        InputStream stream = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream(fileName);
        Font f = Font.loadFont(stream, 12);
        System.out.println(f.getName());
        stream.close();
    }

    public static String getFontFamily() {
        return font;
    }

    private Vals() {}
}
