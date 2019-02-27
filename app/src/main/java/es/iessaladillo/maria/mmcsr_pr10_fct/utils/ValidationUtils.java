package es.iessaladillo.maria.mmcsr_pr10_fct.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class ValidationUtils {

    private ValidationUtils() {
    }


    public static boolean isEmptyText(String text){
        return !TextUtils.isEmpty(text);
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidSpanishPhoneNumber(@NonNull String phone) {
        return (phone.length() <= 0 || phone.length() >= 9) && (phone.startsWith("6")
                || phone.startsWith("7") || phone.startsWith("8") || phone.startsWith("9"))
                && Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidUrl(String url) {
        return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }

}