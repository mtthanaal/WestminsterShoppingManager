// Represents a user with a username and password
class User {
    private String username; // User's username
    private String password; // User's password

    // Constructor to initialize a new user with a specified username and password
    public User(String username, String password) {
        this.username  =  username;
        this.password  =  password;
    }

    // Method to retrieve the username of the user
    public String getUsername() {
        return username;
    }

    // Method to retrieve the password of the user
    public String getPassword() {
        return password;
    }
}
