package saastopossuapp.domain;

/**
 * Class defines users
 */
public class UserAccount {

    private int userId;
    private String username;
    private int userBudget;
    
    public UserAccount(String username) {
        this.username = username;
        this.userBudget = 1000;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserBudget() {
        return userBudget;
    }

    public void setUserBudget(int budget) {
        this.userBudget = budget;
        
    }
}
