/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontHandler {
    public static final String FONT_FAM = "Shantell Sans";
    public static enum DedicatedFont {
        COUNTDOWN_NAME,
        COUNTDOWN_NUM,
        COUNTDOWN_INFO,
        USER_INPUT,
        BORDER,
        SYMBOL_IT,
        SYMBOL
    }

    @Deprecated
    public static Font getButtonFont() {
        return new Font(FONT_FAM + " Medium", 14);
    }

    public static Font getTitle() {
        return Font.font(FONT_FAM, FontWeight.BOLD, 17);
    }

    /**
     * Returns a Font that represents one of three headings. The
     * parameter accepts an integer from 1 to 4 (inclusive),
     * with the largest heading starting at 1. Any other values
     * will be ignored and heading 4 will be returned.
     *
     * @param type
     * @return a Font representing a heading from 1 of 4 types
     */
    public static Font getHeading(int type) {
        switch (type) {
            case 1:
                return Font.font(FONT_FAM, FontWeight.BOLD, 16);
            case 2:
                return Font.font(FONT_FAM, FontWeight.BOLD, 15);
            case 3:
                return Font.font(FONT_FAM, FontWeight.BOLD, 14);
            default:
                return Font.font(FONT_FAM, FontWeight.BOLD, 12);
        }
    }

    public static Font getNormal() {
        return Font.font(FONT_FAM + " Medium", 12.5);
    }

    public static Font getItalic() {
        return Font.font(FONT_FAM + " Medium", FontPosture.ITALIC, 12.5);
    }

    public static Font getSubtitle() {
        return Font.font(FONT_FAM, FontPosture.ITALIC, 12);
    }

    /**
     * This returns fonts for specific user interface components
     * that require unique fonts for aesthetic reasons. Invalid arguments,
     * such as null, will be ignored and normal font will be returned.
     *
     * @param FONT
     * @return an appropriate Font
     */
    public static Font getDedicated(final DedicatedFont FONT) {
        switch (FONT) {
            case COUNTDOWN_NAME:
                return Font.font(FONT_FAM, FontWeight.SEMI_BOLD, 15);
            case COUNTDOWN_NUM:
                return Font.font(FONT_FAM, 11);
            case COUNTDOWN_INFO:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 12);
            case USER_INPUT:
                return Font.font(FONT_FAM + " Medium", 14);
            case BORDER:
                return Font.font(FONT_FAM, FontWeight.BLACK, FontPosture.ITALIC, 12);
            case SYMBOL_IT:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 8);
            case SYMBOL:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.REGULAR, 8);
            default:
                return getNormal();
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
