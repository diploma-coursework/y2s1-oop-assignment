package org.assignment.base;

import org.assignment.util.Staff;
import org.assignment.util.enums.LogActivityType;
import java.time.Instant;

class BaseActivityLog {
    private Instant logTimeId;
    private Staff staff;
    private LogActivityType logActivityType;

    public BaseActivityLog() {
        this.id = null;
        this.staff = null;
        this.logActivityType = null;
    }

    // setters
    public void setLogTimeId(Instant logTimeId) {
        this.logTimeId = logTimeId;
    }
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public void setLogActivityType(LogActivityType logActivityType) {
        this.logActivityType = logActivityType;
    }

    // getters
    public Instant getLogTimeId() {
        return this.logTimeId;
    }
    public Staff getStaff() {
        return this.staff;
    }
    public LogActivityType getLogActivityType() {
        return this.activityType;
    }
}
