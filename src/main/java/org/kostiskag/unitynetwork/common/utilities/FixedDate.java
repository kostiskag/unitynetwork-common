package org.kostiskag.unitynetwork.common.utilities;

import java.util.Date;
import java.text.SimpleDateFormat;

public final class FixedDate {

    private final Date date;

    public FixedDate() {
        this.date = new Date();
    }

    public long getTime() {
        return date.getTime();
    }

    public Date asDate() {
        return new Date(date.getTime());
    }

    public static String getFullTimestamp(Date date){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }

    public static String getSmallTimestamp(Date date){
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }
}
