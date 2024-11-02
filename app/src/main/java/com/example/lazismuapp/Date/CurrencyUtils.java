package com.example.lazismuapp.Date;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatRupiah(int amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return format.format(amount);
    }
}

