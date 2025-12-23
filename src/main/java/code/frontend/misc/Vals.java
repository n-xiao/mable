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

    public enum FontVar {
        REGULAR,
        BOLD,
        BOLD_ITALIC,
        MEDIUM_ITALIC
    }

    public class FontTools {

        public static void initFonts() {
            try {
                loadFont("ShantellSans-Regular.ttf");
                loadFont("ShantellSans-Bold.ttf");
                loadFont("ShantellSans-MediumItalic.ttf");
                loadFont("ShantellSans-BoldItalic.ttf");
            } catch (Exception e) {
                System.err.println("Failed to init fonts");
                System.err.println(e.getStackTrace());
            }
        }

        public static void loadFont(String fileName) throws Exception {
            InputStream stream = Thread.currentThread()
                                       .getContextClassLoader()
                                       .getResourceAsStream(fileName);
            Font.loadFont(stream, 12);
            stream.close();
        }

        public static String getFontName(FontVar variant) {
            String font = "Shantell Sans ";

            switch (variant) {
            case REGULAR:
                return font + "Regular";
            case BOLD:
                return font + "Bold";
            case BOLD_ITALIC:
                return font + "Bold Italic";
            case MEDIUM_ITALIC:
                return font + "Medium Italic";
            default:
                return font + "Regular";
            }

        }

    }



    private Vals() {}
}
