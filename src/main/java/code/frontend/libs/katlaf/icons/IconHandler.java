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

package code.frontend.libs.katlaf.icons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javafx.scene.image.Image;

public final class IconHandler {
    /*
     * this is a utility class, so the constructor doesnt do anything
     * and is private
     */
    private IconHandler(){};

    private final static HashMap<String, Image> IMAGES = new HashMap<String, Image>();

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public static void init() {
        try {
            final InputStream stream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "icon_manifest.txt");
            final InputStreamReader streamReader = new InputStreamReader(stream);
            final BufferedReader reader = new BufferedReader(streamReader);

            String filePath;
            while ((filePath = reader.readLine()) != null) {
                // does this even work i have no idea
                System.out.println("attempting to load icon: " + filePath);
                final InputStream fileStream =
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
                IMAGES.put(filePath, new Image(fileStream));
                fileStream.close();
            }
            reader.close();
            streamReader.close();
            stream.close();
        } catch (Exception e) {
            System.err.println("Failed to init icons");
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element.toString());
            }
        }
    }

    /**
     * Retrieves an icon as an Image. The filename of the image needs to be specified.
     * For example, to retrieve the calendar icon, call: getIconAsImage("calendar.png")
     *
     * @param fileName      the name of the file, as found in the resource directory
     *                      under the subdirectory "icons"
     * @return Image        the requested icon as a JavaFX Image. May return null.
     *
     * @see Image
     * @see HashMap
     */
    public static Image getIconAsImage(final String fileName) {
        final String key = "icons/" + fileName;
        return IMAGES.get(key);
    }
}
