package com.util.number;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

public class NumberUtils {

    private static final ThreadLocal<DecimalFormat> FORMAT_LONG =
            ThreadLocal.withInitial(() -> new DecimalFormat("#,###"));

    private static final ThreadLocal<DecimalFormat> FORMAT_DOUBLE =
            ThreadLocal.withInitial(() -> new DecimalFormat("#,##0.00"));


    private static final FieldPosition UNUSED_FIELD_POSITION = new java.text.FieldPosition(0);

    /**
     * Formats a number.
     *
     * <P>This method formats a double separating thousands and printing just two fractional digits.
     *
     * @param number a number.
     * @return a string containing a pretty print of the number.
     */
    public static String format(final double number) {
        return FORMAT_DOUBLE.get().format(number, new StringBuffer(), UNUSED_FIELD_POSITION).toString();
    }

    /**
     * Formats a number.
     *
     * <P>This method formats a long separating thousands.
     *
     * @param number a number.
     * @return a string containing a pretty print of the number.
     */
    public static String format(final long number) {
        return FORMAT_LONG.get().format(number, new StringBuffer(), UNUSED_FIELD_POSITION).toString();
    }


    /**
     * Formats a size.
     *
     * <P>This method formats a long using suitable unit multipliers (e.g., <code>K</code>, <code>M</code>, <code>G</code>, and <code>T</code>)
     * and printing just two fractional digits.
     *
     * @param number a number, representing a size (e.g., memory).
     * @return a string containing a pretty print of the number using unit multipliers.
     */
    public static String formatSize(final long number) {
        if (number >= 1000000000000L) return format(number / 1000000000000.0) + "T";
        if (number >= 1000000000L) return format(number / 1000000000.0) + "G";
        if (number >= 1000000L) return format(number / 1000000.0) + "M";
        if (number >= 1000L) return format(number / 1000.0) + "K";
        return Long.toString(number);
    }

    /**
     * Formats a binary size.
     *
     * <P>This method formats a long using suitable unit binary multipliers (e.g., <code>Ki</code>, <code>Mi</code>, <code>Gi</code>, and <code>Ti</code>)
     * and printing <em>no</em> fractional digits. The argument must be a power of 2.
     * <p>Note that the method is synchronized, as it uses a static {@link NumberFormat}.
     *
     * @param number a number, representing a binary size (e.g., memory); must be a power of 2.
     * @return a string containing a pretty print of the number using binary unit multipliers.
     */
    public static String formatBinarySize(final long number) {
        if ((number & -number) != number) throw new IllegalArgumentException("Not a power of 2: " + number);
        if (number >= (1L << 40)) return format(number >> 40) + "Ti";
        if (number >= (1L << 30)) return format(number >> 30) + "Gi";
        if (number >= (1L << 20)) return format(number >> 20) + "Mi";
        if (number >= (1L << 10)) return format(number >> 10) + "Ki";
        return Long.toString(number);
    }
}
