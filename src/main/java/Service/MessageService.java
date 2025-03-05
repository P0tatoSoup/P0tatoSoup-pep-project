package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message createMessage(Message message){
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, Message newMessage){
        return messageDAO.updateMessage(messageId, newMessage);
    }
}
