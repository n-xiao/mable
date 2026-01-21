package code.frontend.gui.ricing;

import java.io.InputStream;
import java.util.HashMap;
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

        System.out.println(palette);
    }

    public static String getColourString(String name) {
        if (palette == null || palette.get(name) == null)
            return "rgb(255,255,255)";

        return palette.get(name);
    }
}
