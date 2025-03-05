package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.javalin.Javalin;


/* Message
 * message_id integer primary key auto_increment,
 * posted_by integer,
 * message_text varchar(255),
 * time_posted_epoch long,
 * foreign key (posted_by) references Account(account_id)
 */

 

public class MessageDAO {

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
                
            if (message.getMessage_text() == null || 
                message.getMessage_text().isEmpty() || 
                message.getMessage_text().length() > 255 ||
                message.getPosted_by() <= 0) {

                    return null;
            }
        
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
           
            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), 
                                   message.getMessage_text(), message.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = connection.prepareStatement(sql);
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

    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
               Message newMessage = new Message( 
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                 );
                 return newMessage;
            }
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
        
    }

    public Message deleteMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            ps.executeUpdate();
            return getMessageById(messageId);
        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    public Message updateMessage(int messageId, Message newMessage){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newMessage.message_text);
            ps.setInt(2, messageId);
            ps.executeUpdate();
            return getMessageById(messageId);
            
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
