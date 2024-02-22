package util;

import java.util.Date;

public class TimeProvider {
    /**
     * renvoie l’année courante
     * @return l’année courante
     */
    public static int currentYearValue() {
        return new Date().getYear() + 1900;
    }
}
