package com.toystore.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final Pattern DOUBLE_HYPHEN = Pattern.compile("[-]+");

    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // 1. Convert to lowercase
        String normalized = input.toLowerCase(Locale.ROOT);

        // 2. Handle Vietnamese specific characters
        normalized = normalized
                .replace("đ", "d")
                .replace("đ́", "d")
                .replace("đ̣", "d")
                .replace("đ̉", "d")
                .replace("đ̃", "d")
                .replace("đ̣", "d");

        // 3. Normalize to remove accents
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");

        // 4. Replace whitespace with hyphens
        normalized = WHITESPACE.matcher(normalized).replaceAll("-");

        // 5. Remove any character that is not alphanumeric or a hyphen
        normalized = NONLATIN.matcher(normalized).replaceAll("");

        // 6. Remove double/multiple hyphens
        normalized = DOUBLE_HYPHEN.matcher(normalized).replaceAll("-");

        // 7. Trim hyphens from ends
        if (normalized.startsWith("-")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith("-")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }
}
