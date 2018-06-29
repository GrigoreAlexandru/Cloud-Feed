package com.ga.cloudfeed;

import android.util.Base64;

public final class Utils {

    public static String toBase64(String input) {
        String encode = Base64.encodeToString(input.getBytes(), Base64.NO_PADDING);
        return encode.replace("\n", "");
    }
}
