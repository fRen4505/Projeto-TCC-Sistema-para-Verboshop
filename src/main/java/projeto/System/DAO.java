package projeto.System;

import java.sql.Connection;

import projeto.System.Database.DataBase;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public abstract class DAO {
    
    private User currentUser;
    private Connection conneccao;
    private Permissoes nivel;

    public DAO(User insCurrent, Permissoes permissao){ 
        if (insCurrent.getFunção().equals(permissao)) {
            this.currentUser = insCurrent;
            this.conneccao = DataBase.getInstance().getConnection();
            this.nivel = permissao;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Permissoes getNivel() {
        return nivel;
    }

    public Permissoes getCurrentUserPermissiao(){
        return this.currentUser.getFunção();
    }

    public Connection getConneccao() {
        return conneccao;
    }

    public void closeConneccao(){
        DataBase.getInstance().closeConnection();
    }

}
