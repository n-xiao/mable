package code.frontend.misc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *  no, i haven't heard about CSS before... and neither have you.
 */
public class Vals {
    public final class Colour {
        public static final Color BACKGROUND = Color.rgb(20, 20, 30);
        public static final Color SIDE_BAR = Color.rgb(67, 67, 75);
        public static final Color VIEW_PICKER = Color.rgb(44, 44, 53);
        public static final Color FEEDBACK = Color.rgb(75, 148, 174);
        public static final Color TXT_GHOST = Color.rgb(200, 200, 230);
        public static final Color SELECTED = Color.rgb(136, 175, 252);
        public static final Color ERROR = Color.rgb(250, 11, 11);
        public static final Color DISABLED = Color.rgb(100, 100, 100);
        public static final Color BTTN_CREATE = Color.rgb(196, 252, 247);
        public static final Color BTTN_EDIT = Color.rgb(172, 127, 196);
        public static final Color BTTN_ADD_TO_FOLDER = Color.rgb(137, 127, 196);
        public static final Color BTTN_REMOVE = Color.rgb(252, 103, 105);
        public static final Color BTTN_DESELECT = Color.rgb(206, 182, 173);
        public static final Color BTTN_MARK_COMPLETE = Color.rgb(67, 161, 237);

        public static Background createBG(Color c, double radius, double ins) {
            BackgroundFill bgFill = new BackgroundFill(c, new CornerRadii(radius), new Insets(ins));
            return new Background(bgFill);
        }
    }

    public final class GraphicalUI {
        public static final double DRAW_THICKNESS = 2.5;
        public static final double BTTN_THICKNESS = 2.8;
        public static final double INPUT_BORDER_WIDTH = 2;
        public static final int PREF_WIDTH = 1120;
        public static final int PREF_HEIGHT = 730;
        public static final int MIN_WIDTH = 620;
        public static final int MIN_HEIGHT = 430;
        public static final int INPUT_MIN_HEIGHT = 50;
        // CORNER_OFFSET > CORNER_DEVIATION
        public static final double DEVIATION = 0.018;
        public static final double CORNER_DEVIATION = 0.045;
        public static final double CORNER_OFFSET = 0.28;

        public static String intToString(int input) {
            NumberFormat formatter =
                NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            return formatter.format(input);
        }
    }

    public class UtilityUI {
        public static final int WIDTH = 550;
        public static final int HEIGHT = 370;
        public static Font getFont() {
            return Font.font(Vals.FontTools.FONT_FAM + " Medium", 15);
        }
    }

    public class FontTools {
        public static final String FONT_FAM = "Shantell Sans";

        public static Font getButtonFont() {
            return new Font(FONT_FAM + " Medium", 16);
        }

        public static void initFonts() {
            try {
                InputStream manifestStream =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "manifest.txt");
                InputStreamReader manifestStreamReader = new InputStreamReader(manifestStream);
                BufferedReader manifestReader = new BufferedReader(manifestStreamReader);

                String fileName;
                ArrayList<String> fontFilePaths = new ArrayList<>();

                while ((fileName = manifestReader.readLine()) != null) fontFilePaths.add(fileName);

                manifestStream.close();

                for (String string : fontFilePaths) loadFont(string);

            } catch (Exception e) {
                System.err.println("Failed to init fonts");
                System.err.println(e.getStackTrace());
            }
        }

        private static void loadFont(String fileName) throws Exception {
            InputStream stream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            Font.loadFont(stream, 12);
            stream.close();
        }
    }

    private Vals() {}
}
