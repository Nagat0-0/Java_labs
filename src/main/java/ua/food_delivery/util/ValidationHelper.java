package ua.food_delivery.util;

import java.util.List;
import java.util.regex.Pattern;

class ValidationHelper {

    private ValidationHelper() {}

    static boolean isStringLengthBetween(String text, int min, int max) {
        if (text == null) return false;
        int length = text.trim().length();
        return length >= min && length <= max;
    }

    static boolean isNumberBetween(double number, double min, double max) {
        return number >= min && number <= max;
    }

    static boolean isStringMatchPattern(String text, String regex) {
        if (text == null) return false;
        return Pattern.matches(regex, text);
    }

    static boolean isValidList(List<?> list, int minSize) {
        return list != null && list.size() >= minSize;
    }
}
