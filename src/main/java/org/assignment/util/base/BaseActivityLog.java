package org.assignment.util.base;

import org.assignment.util.extended.Staff;
import org.assignment.util.enums.LogActivityType;
import java.time.Instant;

public class BaseActivityLog {
    private Instant logTimeId;
    private Staff staff;
    private LogActivityType logActivityType;

    public BaseActivityLog() {
        this.logTimeId = null;
        this.staff = null;
        this.logActivityType = null;
    }

    public BaseActivityLog(Instant logTimeId, Staff staff, LogActivityType logActivityType) {
        this.logTimeId = logTimeId;
        this.staff = staff;
        this.logActivityType = logActivityType;
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
        return this.logActivityType;
    }
}
