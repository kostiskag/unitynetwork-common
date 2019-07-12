package org.kostiskag.unitynetwork.common.utilities;

import java.util.Date;

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
}
