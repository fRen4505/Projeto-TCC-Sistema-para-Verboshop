package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projeto.Sessao;
import projeto.System.Models.Livro;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Permissoes;

public class UserDAO extends PerfilDAO {

    private static UserDAO Instancia;

    private UserDAO(User insCurrent,  Permissoes permissaoDAO) {
        super(insCurrent, permissaoDAO);
    }

    public static UserDAO getInstancia(User current){
        if (Instancia == null) {
            Instancia = new UserDAO(current, Permissoes.USUARIO);
            return Instancia;
        }else {
            return Instancia;
        }
    }

    
    private Livro getLivrobyID(CodigoISBN insLivID) throws SQLException{

        PreparedStatement estado = super.getConneccao().prepareStatement("select * from livro where livro.isbn = ?");
        estado.setString(1, insLivID.valorISBN());

        ResultSet valores = estado.executeQuery();

        return new Livro(
            valores.getString("titulo"), 
            valores.getString("autor"), 
            valores.getString("editora"), 
            valores.getDouble("dinheiro"), 
            valores.getInt("quantidade"),
            valores.getString("isbn")
        );

    }

    //==================================== METODOS DE LIVROS ==========================================

    @Override
    public List<Livro> getLivros() throws SQLException {

        List<Livro> livros = new ArrayList<Livro>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet valores = estado.executeQuery("select * from livro");

        while (valores.next()) {
            livros.add(new Livro(
                valores.getString("titulo"), 
                valores.getString("autor"), 
                valores.getString("editora"), 
                valores.getDouble("dinheiro"), 
                valores.getInt("quantidade"),
                valores.getString("isbn")
            ));
        }
        return livros;
    }

    @Override
    public void alterarLivro(CodigoISBN insLivID, Livro insAlt) throws SQLException {
    
        Livro old = getLivrobyID(insLivID);

        log.info(
            "Livro: "+old.getTitulo()+" recebeu novo lote e sua quantidade no estoque foi de: " +
            old.getQuantidade() + " para " + insAlt.getQuantidade() +
            " por: "+Sessao.getUser().getNome()
        );

        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update livro set quantidade = ? where livro.isbn = ? ");

        estado.setInt(1, insAlt.getQuantidade());
        estado.setString(2, insLivID.valorISBN());

        estado.execute();
        estado.close();
    }
    
    //==================================== METODOS DE USUARIOS ==========================================

    @Override
    public List<User> getUsers() throws SQLException {
        
        List<User> usuarios = new ArrayList<User>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet valores = estado.executeQuery("select * from user");

        while (valores.next()) {            
            usuarios.add(new User(
                valores.getString("nome"), 
                valores.getString("email"),
                valores.getString("id"), 
                Permissoes.valueOf(valores.getString("permissao"))
            ));
        }
        return usuarios;
    }

    @Override
    public void adicionarUser(User insUser) throws SQLException {
        //Somente adiciona cliente
        PreparedStatement estado = super.getConneccao().prepareStatement("insert into user values(? , ? , ? , ? )");

        estado.setString(1, insUser.getNome());
        estado.setString(2, insUser.getEmail());
        estado.setString(3, insUser.getID().toString());
        estado.setString(4, Permissoes.CLIENTE.getPermissaoNome());
    
        estado.execute();
        estado.close();
        log.info("Cliente: "+insUser.getNome()+" foi adicionado por: "+Sessao.getUser().getNome());
    }


}
