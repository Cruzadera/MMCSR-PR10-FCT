package es.iessaladillo.maria.mmcsr_pr10_fct.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextWatcherUtils {
    private TextWatcherUtils() {
    }

    public interface AfterTextChangedListener{
        void afterTextChanged(Editable s);
    }

    public static void setAfterTextChangeListener(TextView textView, AfterTextChangedListener action){
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                action.afterTextChanged(s);
            }
        });
    }
}