package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TwitterUser {

    public static final String TABLE_NAME = "twitter_user";

    long userID;
    String accessToken;
    String accessTokenSecret;
    String userName;

    public TwitterUser(long userID, String username, String accessToken, String accessTokenSecret) {
        this.userID = userID;
        this.userName = username;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    private TwitterUser() {
    }

    public static List<TwitterUser> getAllUsers(Connection conn) throws SQLException {
        List<TwitterUser> users = new ArrayList<>();
        String query = "SELECT user_id, user_name, access_token, access_token_secret from " + TABLE_NAME;
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            TwitterUser u = new TwitterUser();
            u.userID = results.getLong("user_id");
            u.accessToken = results.getString("access_token");
            u.accessTokenSecret = results.getString("access_token_secret");
            u.userName = results.getString("user_name");
            users.add(u);
        }
        return users;
    }

    public static TwitterUser findUserById(Connection conn, long userID) throws SQLException {
        TwitterUser user = null;
        String query = "SELECT user_id, user_name, access_token, access_token_secret from " + TABLE_NAME
                + " WHERE user_id=" + userID;
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(query);
        if (results.next()) {
            user = new TwitterUser();
            user.userID = userID;
            user.accessToken = results.getString("access_token");
            user.accessTokenSecret = results.getString("access_token_secret");
            user.userName = results.getString("user_name");
        }
        return user;
    }

    public static boolean addUser(Connection conn, TwitterUser user) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " values (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setLong(1, user.userID);
        stmt.setString(2, user.userName);
        stmt.setString(3, user.accessToken);
        stmt.setString(4, user.accessTokenSecret);
        int rows = stmt.executeUpdate();
        return rows == 1;
    }

    public static boolean update(Connection conn, TwitterUser user) throws SQLException {
        String query = "UPDATE " + TABLE_NAME + " set access_token = ?, access_token_secret = ?"
                + " WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, user.accessToken);
        stmt.setString(2, user.accessTokenSecret);
        stmt.setLong(3, user.userID);
        int rows = stmt.executeUpdate();
        return rows == 1;
        
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public long getUserID() {
        return userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public String getUserName() {
        return userName;
    }
}
