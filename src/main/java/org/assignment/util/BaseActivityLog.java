package org.assignment.util;

import java.time.LocalDateTime;

public class BaseActivityLog {
    private String id;
    private BaseStaff staff;
    private String activityType;
    private LocalDateTime activityTime;

    public BaseActivityLog( BaseStaff staff, String activityType, LocalDateTime activityTime) {
        // id is auto generated
        this.staff = staff;
        this.activityType = activityType;
        this.activityTime = activityTime;
    }

    // Getters and Setters
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public BaseStaff getStaff() {return staff;}

    public void setStaff(BaseStaff staff) {this.staff = staff;}

    public String getActivityType() {return activityType;}

    public void setActivityType(String activityType) {this.activityType = activityType;}

    public LocalDateTime getActivityTime() {return activityTime;}

    public void setActivityTime(LocalDateTime activityTime) {this.activityTime = activityTime;}
}
