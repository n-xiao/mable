/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.libs.katlaf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FontHandler {
    public static final String FONT_FAM = "Shantell Sans";

    @Deprecated
    public static Font getButtonFont() {
        return new Font(FONT_FAM + " Medium", 14);
    }

    public static Font getTitle() {
        return Font.font(FONT_FAM, FontWeight.BOLD, 17);
    }

    /**
     * Returns a Font that represents one of three headings. The
     * parameter accepts an integer from 1 to 3 (inclusive),
     * with the largest heading starting at 1. Any other values
     * will be ignored and heading 3 will be returned.
     *
     * @param type
     * @return a Font representing a heading from 1 of 3 types
     */
    public static Font getHeading(int type) {
        switch (type) {
            case 1:
                return Font.font(FONT_FAM, FontWeight.BOLD, 15);
            case 2:
                return Font.font(FONT_FAM, FontWeight.BOLD, 13);
            default:
                return Font.font(FONT_FAM, FontWeight.MEDIUM, 12);
        }
    }

    public static void initFonts() {
        try {
            InputStream manifestStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("manifest.txt");
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
        Font.loadFont(stream, 14);
        stream.close();
    }

    private FontHandler() {}
}
