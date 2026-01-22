/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.ricing;

import java.io.InputStream;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class RiceHandler {
    private static final ObjectMapper MAPPER =
        JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();
    private static HashMap<String, String> palette;

    private RiceHandler() {}

    public static void init() {}

    public static void updatePalette(final String THEME_NAME) {
        InputStream stream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("Themes.json");
        final JsonNode JSON_ROOT = MAPPER.readTree(stream);
        TypeReference<HashMap<String, String>> typeRef =
            new TypeReference<HashMap<String, String>>() {};

        JsonNode jsonTheme = JSON_ROOT.get(THEME_NAME);

        if (jsonTheme != null)
            palette = MAPPER.convertValue(JSON_ROOT.get(THEME_NAME), typeRef);
        // else, continue with a custom theme
    }

    public static String getColourString(String name) {
        if (palette == null || palette.get(name) == null) {
            System.err.println("WARNING: COULD NOT FIND COLOUR: " + name);
            System.err.println(Thread.currentThread().getStackTrace());
            return "rgb(255,255,255)";
        }

        return palette.get(name);
    }

    public static Color getColour(String name) {
        return Color.web(getColourString(name));
    }

    public static Color getColour() {
        return getColour("primary");
    }

    public static Background createBG(Color c, double radius, double ins) {
        BackgroundFill bgFill = new BackgroundFill(c, new CornerRadii(radius), new Insets(ins));
        return new Background(bgFill);
    }

    public static Color adjustOpacity(Color color, double opacity) {
        Color adjusted = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
        return adjusted;
    }
}
