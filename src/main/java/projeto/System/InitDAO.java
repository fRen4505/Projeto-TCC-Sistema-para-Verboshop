package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projeto.System.Contracts.UserInterface;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class InitDAO extends DAO implements UserInterface {

    private static InitDAO Instancia;

    private InitDAO() {}

    public static InitDAO getInstancia(){
        if (Instancia == null) {
            Instancia = new InitDAO();
            return Instancia;
        } else {
            return Instancia;
        }
    }

    @Override
    public List<User> getUsers() throws SQLException {
        
        List<User> usuarios = new ArrayList<User>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet valores = estado.executeQuery("select * from user");

        while (valores.next()) {
            Permissoes permissao = Permissoes.valueOf(valores.getString("permissao"));
            
            User usr = new User(
                valores.getString("nome"), 
                valores.getString("email"),
                valores.getString("id"), 
                permissao
            );

            usuarios.add(usr);
        }

        return usuarios;
    }

    @Override
    public void adicionarUser(User insUser) throws SQLException {
        
        if (this.getUsers().isEmpty()) {
            PreparedStatement estado = super.getConneccao().prepareStatement("insert into user values(? , ? , ? , ? )");

            estado.setString(1, insUser.getNome());
            estado.setString(2, insUser.getEmail());
            estado.setString(3, insUser.getID().toString());
            estado.setString(4, Permissoes.ADMINISTRADOR.toString());
        
            estado.execute();
            estado.close();

        }
    }
    
}
