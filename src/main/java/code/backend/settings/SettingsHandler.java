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

package code.backend.settings;

import java.util.HashMap;

public final class SettingsHandler {
    private static final HashMap<String, String> MAP = new HashMap<String, String>();

    private SettingsHandler(){};

    public static HashMap<String, String> get() {
        return MAP;
    }

    public static void resetToDefault() {
        MAP.clear();
        MAP.put(Key.LIGHT_MODE, "false");
        MAP.put(Key.ALT_DATE, "false");
    }

    public static boolean getBooleanValue(final String key) {
        return Boolean.parseBoolean(MAP.get(key));
    }

    public static void setBooleanValue(final String key, final boolean bool) {
        MAP.put(key, Boolean.toString(bool));
    }

    public static final class Key {
        public static final String LIGHT_MODE = "use_light_mode";
        public static final String ALT_DATE = "use_alternate_date";
        private Key() {}
    }
}
