package be.kuleuven.chi.app;


import android.text.InputFilter;
import android.text.Spanned;
/**
 * Created by Lies on 21/04/14.
 */

class InputFilterMinMax implements InputFilter {

    private double min, max;

    public InputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String newString = dest.toString().substring(0, dstart) + source.toString().substring(start, end) + dest.toString().substring(dend);

            Double input;
            // If we only have one char and it is a minus sign, test against -1:
            if (newString.length() == 1 && newString.charAt(0) == '-')
                input = -1d;
            else
                input = Double.parseDouble(newString);

            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}