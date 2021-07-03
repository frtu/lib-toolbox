package com.github.frtu.dot.utils;

import java.util.regex.Pattern;

/**
 * ID Util based on the DOT Grammar
 *
 * @author frtu
 * @see <a href="https://graphviz.gitlab.io/_pages/doc/info/lang.html">DOT Grammar</a>
 * @since 0.3.6
 */
public class IdUtil {
    public static final String ID_PATTERN_STR = "[_a-zA-Z\\\\200-\\\\377][0-9_a-zA-Z\\\\200-\\\\377]*";
    private static Pattern idPattern = Pattern.compile(ID_PATTERN_STR);

    public static String formatId(String unfilteredId) {
        return unfilteredId.replace('.', '_');
    }

    public static boolean assertFormatId(String id) {
        if (!idPattern.matcher(id).matches()) {
            throw new IllegalStateException("IDs MUST match pattern " + ID_PATTERN_STR + " parameter passed " + id);
        }
        return true;
    }
}
