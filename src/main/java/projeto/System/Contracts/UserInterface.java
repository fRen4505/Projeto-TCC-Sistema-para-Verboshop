package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.List;

import projeto.System.Models.User;

public interface UserInterface {
    
    public List<User> getUsers() throws SQLException;

    public void adicionarUser(User insUser) throws SQLException;

}
