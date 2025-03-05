package Controller;

import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);

        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        
        app.post("/register", this::createAccountHandler);
        app.post("/login", this:: verifyLoginHandler);
        app.get("accounts/{account_id}/messages", this::getAccountMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    
    private void getAllMessagesHandler(Context ctx) {
      List<Message> message = messageService.getAllMessages();
      ctx.json(message);
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if(newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
           
          if(message != null){
              ctx.json(message);
           } else {
              ctx.status(200);
           }
    }

    private void deleteMessageByIdHandler(Context ctx){
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message != null){
            ctx.json(message);
         } else {
            ctx.status(200);
         }
    }

    private void updateMessageHandler(Context ctx){
        // get the message to alter by the Id
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
    
        // get the date to alter the message with
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage;
        try{
            newMessage = mapper.readValue(ctx.body(), Message.class);
        } catch (JsonProcessingException e) {
            ctx.status(400);
            return;
        }
        // alter the message with this data
        Message updatedMessage = messageService.updateMessage(messageId, newMessage);
        //show the new message
        if(updatedMessage != null){
            if (updatedMessage.getMessage_text() != null && 
                updatedMessage.getMessage_text().isEmpty() == false &&
                updatedMessage.getMessage_text().length() <= 255) {
                    ctx.json(updatedMessage);
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400);
        }

    }

    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.createAccount(account);

        if(newAccount != null){
            ctx.json(mapper.writeValueAsString(newAccount));
        }else {
            ctx.status(400);
        }
    }

    private void verifyLoginHandler(Context ctx){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Account accountToCheck = mapper.readValue(ctx.body(), Account.class);
            Account account = accountService.verifyLogin(accountToCheck);
            if(account != null){
                ctx.json(account);
            }else{
                ctx.status(401);
            }
        } catch (JsonProcessingException e) {
            ctx.status(400);
        }
    }

    public void getAccountMessagesHandler(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = accountService.getAccountMessages(accountId);
        ctx.json(messages);
    }
}