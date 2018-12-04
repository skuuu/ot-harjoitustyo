
package saastopossuapp.domain;

import java.sql.Date;


public class Activity implements Comparable<Activity> {
    private int activityId;
    private String activitysUser; 
    private int cents; 
    private Date date;
    private String category;


    public Activity(String activitysUser, int cents, Date date, String category) {
        this.activitysUser = activitysUser;
        this.cents = cents;
        this.date = date;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    
    public String getActivitysUser() {
        return activitysUser;
    }

    public void setActivitysUser(String activitysUser) {
        this.activitysUser = activitysUser;
    }

    public int getCents() {
        return cents;
    }

    public void setCents(int cents) {
        this.cents = cents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public int compareTo(Activity other) {
        if (this.date.before(other.date)) {
            return -1;
        }
        return 1;
    }
    
}
