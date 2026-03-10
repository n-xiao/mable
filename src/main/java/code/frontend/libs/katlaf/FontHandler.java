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

public final class FontHandler {
    public static final String FONT_FAM = "Shantell Sans";
    public static final String FONT_MONO = "mononoki";

    public static enum DedicatedFont {
        COUNTDOWN_NAME,
        COUNTDOWN_NUM,
        COUNTDOWN_INFO,
        USER_INPUT,
        BORDER,
        SYMBOL_IT,
        SYMBOL,
        RIGHT_CLICK
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
                return Font.font(FONT_FAM, FontWeight.BOLD, 17);
            case 2:
                return Font.font(FONT_FAM, FontWeight.BOLD, 15);
            case 3:
                return Font.font(FONT_FAM, FontWeight.BOLD, 14);
            default:
                return Font.font(FONT_FAM, FontWeight.BOLD, 12);
        }
    }

    public static Font getNormal() {
        return Font.font(FONT_FAM + " Medium", 11);
    }

    public static Font getItalic() {
        return Font.font(FONT_FAM + " Medium", FontPosture.ITALIC, 11);
    }

    public static Font getSubtitle() {
        return Font.font(FONT_FAM, FontPosture.ITALIC, 11);
    }

    public static Font getMono() {
        return Font.font(FONT_MONO, 12);
    }

    public static Font getHeavyMono() {
        return Font.font(FONT_MONO, FontWeight.EXTRA_BOLD, 12);
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
                return Font.font(FONT_FAM, FontWeight.SEMI_BOLD, 13);
            case COUNTDOWN_NUM:
                return Font.font(FONT_FAM, 11);
            case COUNTDOWN_INFO:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13);
            case USER_INPUT:
                return Font.font(FONT_FAM + " Medium", 13);
            case BORDER:
                return Font.font(FONT_FAM, FontWeight.BLACK, FontPosture.REGULAR, 9.5);
            case SYMBOL_IT:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 8);
            case SYMBOL:
                return Font.font(FONT_FAM, FontWeight.BOLD, FontPosture.REGULAR, 12);
            case RIGHT_CLICK:
                return Font.font(FONT_FAM, 10);
            default:
                return getNormal();
        }
    }

    public static void init() {
        try {
            final InputStream manifestStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "font_manifest.txt");
            final InputStreamReader manifestStreamReader = new InputStreamReader(manifestStream);
            final BufferedReader manifestReader = new BufferedReader(manifestStreamReader);

            String fileName;
            final ArrayList<String> fontFilePaths = new ArrayList<>();

            while ((fileName = manifestReader.readLine()) != null) fontFilePaths.add(fileName);

            manifestReader.close();
            manifestStreamReader.close();
            manifestStream.close();

            for (String string : fontFilePaths) loadFont(string);

        } catch (Exception e) {
            System.err.println("Failed to init fonts");
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element.toString());
            }
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
