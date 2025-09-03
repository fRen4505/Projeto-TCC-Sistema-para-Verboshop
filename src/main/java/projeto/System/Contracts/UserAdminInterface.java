package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.UUID;

import projeto.System.Models.User;

public interface UserAdminInterface {
    
    public void alterarUser(UUID insUserID, User insUserAlt) throws SQLException;

    public void deletarUser(UUID insUserID) throws SQLException;

}
