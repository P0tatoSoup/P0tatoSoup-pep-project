package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* Account
 * account_id integer primary key auto_increment,
 * username varchar(255) unique,
 * password varchar(255)
 */


public class AccountDAO {

    public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {

            if(account.getUsername().isEmpty() == true ||
               account.getPassword().length() < 4) {
                return null;
            }
        
            String sql = "INSERT INTO Account(username, password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
           
            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), 
                                   account.getPassword());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account verifyLogin(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            if(account.getUsername() == null ||
               account.getPassword() == null) {
                return null;
            }
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
              return new Account( 
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password"));
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAccountMessages(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    );
                messages.add(message);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
}
