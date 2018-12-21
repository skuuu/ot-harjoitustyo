
package saastopossuapp.domain;

import java.sql.Date;
import saastopossuapp.logic.Converter;

/**
 * Class defines the expenses added by user
 */
public class Activity implements Comparable<Activity> {
    private int activityId;
    private String activitysUser; 
    private int cents; 
    private Date date;
    private String category;
    private String description;


    public Activity(String activitysUser, int cents, Date date, String category, String description) {
        this.activitysUser = activitysUser;
        this.cents = cents;
        this.date = date;
        this.category = category;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    /**
     * Method compares two Activitys by their dates
     * @param  otherActivity - Activity being compared to
     * @return -1 if otherActivity is before this.Activity, else 1
     */
    @Override
    public int compareTo(Activity otherActivity) {
        if (this.date.before(otherActivity.date)) {
            return -1;
        }
        return 1;
    }
    
    /**
     * Method defines equality for two Activities. 
     * Activities are equal if they have same date and same category.
     * @param o - Object to which Activity is being compared to.
     * @return true if Object equals Activity, else false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        Activity activity = (Activity) o;
        return activity.date.equals(date) && activity.category.equals(category);
    }

    /**
     * Method overrides Activity's hashcode for comparing Activities
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + date.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
    
    /**
     * Method overrides Activity's toString-method.
     * @return String implementation of Activity
     */
    @Override
    public String toString() {
        Converter conv = new Converter();
        return conv.toEuros(cents) + "€\t" + this.description;
    }
}
