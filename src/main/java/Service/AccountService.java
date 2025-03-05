package Service;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account createAccount(Account account){
        return accountDAO.createAccount(account);
    }

    public Account verifyLogin(Account account){
        return accountDAO.verifyLogin(account);
    }

    public List<Message> getAccountMessages(int accountId){
        return accountDAO.getAccountMessages(accountId);
    }

}
