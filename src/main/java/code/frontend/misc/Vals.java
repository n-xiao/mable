package code.frontend.misc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Vals {

    public final class Colour {
        public static final Color BACKGROUND = Color.rgb(20, 20, 30);
        public static final Color TXT_GHOST = Color.rgb(220, 220, 220, 0.8);
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

    public class FontTools {

        public static final String FONT_FAM = "Shantell Sans";

        public static void initFonts() {
            try {
                InputStream manifestStream = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResourceAsStream("manifest.txt");
                InputStreamReader manifestStreamReader = new InputStreamReader(manifestStream);
                BufferedReader manifestReader = new BufferedReader(manifestStreamReader);

                String fileName;
                ArrayList<String> fontFilePaths = new ArrayList<>();

                while ((fileName = manifestReader.readLine()) != null)
                    fontFilePaths.add(fileName);

                manifestStream.close();

                for (String string : fontFilePaths)
                    loadFont(string);

            } catch (Exception e) {
                System.err.println("Failed to init fonts");
                System.err.println(e.getStackTrace());
            }
        }

        private static void loadFont(String fileName) throws Exception {
            InputStream stream = Thread.currentThread()
                                       .getContextClassLoader()
                                       .getResourceAsStream(fileName);
            Font.loadFont(stream, 12);
            // System.out.println(f.getName());
            stream.close();
        }

    }

    private Vals() {}
}
