/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.libs.katlaf;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatHandler {
    public static String intToString(int input) {
        NumberFormat formatter =
            NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        return formatter.format(input);
    }
    private FormatHandler() {}
}
