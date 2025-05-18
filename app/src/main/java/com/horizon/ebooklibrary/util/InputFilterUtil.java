package com.horizon.ebooklibrary.util;

import android.text.InputFilter;

/**
 * Utility class that provides input filters for EditText fields.
 */
public class InputFilterUtil {

    public static final InputFilter ENGLISH_ONLY_FILTER = (source, start, end, dest, dstart, dend) -> {
        for(int i = start; i < end; i++) {
            char c = source.charAt(i);
            if(!Character.toString(c).matches("[a-zA-Z0-9@._]")) {
                return ""; // Block this character
            }
        }

        return null; // Accept
    };
}
