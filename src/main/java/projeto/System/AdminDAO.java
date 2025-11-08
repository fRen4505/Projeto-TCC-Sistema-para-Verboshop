package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import projeto.Sessao;
import projeto.System.Contracts.LivroAdminInterface;
import projeto.System.Contracts.UserAdminInterface;
import projeto.System.Models.Livro;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Permissoes;

public class AdminDAO extends PerfilDAO implements LivroAdminInterface, UserAdminInterface {

    private static AdminDAO Instancia;

    private AdminDAO(User insCurrent, Permissoes permissaoDAO) {
        super(insCurrent, permissaoDAO);
    }

    public static AdminDAO getInstancia(User current){
        if (Instancia == null) {
            Instancia = new AdminDAO(current, Permissoes.ADMINISTRADOR);
            return Instancia;
        }else {
            return Instancia;
        }
    }

    private User getUserbyID(UUID insUserID) throws SQLException{

        PreparedStatement estado = super.getConneccao().prepareStatement("select * from user where user.id = ? ");
        estado.setString(1, insUserID.toString());

        ResultSet valores = estado.executeQuery();

        return new User(
            valores.getString("nome"), 
            valores.getString("email"), 
            valores.getString("id"),
            Permissoes.valueOf(valores.getString("permissao"))
        );
       
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

        PreparedStatement estado = super.getConneccao().prepareStatement("insert into user values(? , ? , ? , ? )");

        estado.setString(1, insUser.getNome());
        estado.setString(2, insUser.getEmail());
        estado.setString(3, insUser.getID().toString());
        estado.setString(4, insUser.getFunção().toString());
    
        estado.execute();
        estado.close();
        log.info("Usuario: "+insUser.getNome()+" adicionado por: "+Sessao.getUser().getNome());
    }

    @Override 
    public void alterarUser(UUID insUserID, User insUserAlt) throws SQLException {

        User old = getUserbyID(insUserID);

        log.info("Usuario: "+old.getNome()+" teve os dados alterados: "+
            (!insUserAlt.getNome().equals(old.getNome()) ? old.getNome() + " para " + insUserAlt.getNome() + "\t": " ") +
            (!insUserAlt.getEmail().equals(old.getEmail()) ? old.getNome() + " para " + insUserAlt.getNome() + "\t": " ") +
            (!insUserAlt.getFunção().toString().equals(old.getFunção().toString()) ? old.getFunção().toString() + " para " + insUserAlt.getFunção().toString() + "\t": " ") +
        " por: "+Sessao.getUser().getNome());

        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update user set nome = ? , email = ? , permissao = ? where user.id = ? ");

        estado.setString(1, insUserAlt.getNome());
        estado.setString(2, insUserAlt.getEmail());
        estado.setString(3, insUserAlt.getFunção().toString());

        estado.setString(4, insUserID.toString());

        estado.execute();
        estado.close();
    }

    @Override 
    public void deletarUser(UUID insUserID) throws SQLException {
        log.info("Usuario: "+getUserbyID(insUserID).getNome()+" deletado por: "+Sessao.getUser().getNome());

        PreparedStatement estado = super.getConneccao().prepareStatement("delete from user where user.id = ? ");
        estado.setString(1, insUserID.toString());

        estado.execute();
        estado.close();
    }

    //==================================== METODOS DE LIVROS ==========================================

    @Override 
    public void alterarLivro(CodigoISBN insLivID, Livro insLivAlt) throws SQLException {
        
        Livro old = getLivrobyID(insLivID);

        log.info("Livro: "+old.getTitulo()+" teve os dados alterados: "+
            (!insLivAlt.getTitulo().equals(old.getTitulo()) ? old.getTitulo() + " para " + insLivAlt.getTitulo() + "\t": " ") +
            (!insLivAlt.getAutor().equals(old.getAutor()) ? old.getAutor() + " para " + insLivAlt.getAutor() + "\t": " ") +
            (!insLivAlt.getEditora().equals(old.getEditora()) ? old.getEditora() + " para " + insLivAlt.getEditora() + "\t": " ") +
            (!insLivAlt.getPreço().valor().equals(old.getPreço().valor()) ? old.getPreço().valor() + " para " + insLivAlt.getPreço().valor() + "\t": " ") +
            (insLivAlt.getQuantidade().compareTo(old.getQuantidade()) != 0 ? old.getQuantidade() + " para " + insLivAlt.getQuantidade() + "\t": " ") +
        " por: "+Sessao.getUser().getNome());

        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update livro set titulo = ? , autor  = ? , editora  = ? , dinheiro  = ? , quantidade  = ?  where livro.isbn = ? ");

        estado.setString(1, insLivAlt.getTitulo());
        estado.setString(2, insLivAlt.getAutor());
        estado.setString(3, insLivAlt.getEditora());
        estado.setDouble(4, insLivAlt.getPreço().getQuantiaDouble());
        estado.setInt(5, insLivAlt.getQuantidade());

        estado.setString(6, insLivID.valorISBN());

        estado.execute();
        estado.close();

    }

    @Override 
    public void adicionarLivro(Livro livroIns) throws SQLException {

        PreparedStatement estado = super.getConneccao().prepareStatement(" insert into livro values( ? , ? , ? , ? , ? , ? ) ");
        estado.setString(1, livroIns.getTitulo());
        estado.setString(2, livroIns.getAutor());
        estado.setString(3, livroIns.getEditora());
        estado.setString(4, livroIns.getISBN().valorISBN());
        estado.setDouble(5, livroIns.getPreço().getQuantiaDouble());
        estado.setInt(6, livroIns.getQuantidade());

        estado.execute();
        estado.close();
        log.info("Livro: "+livroIns.getTitulo()+" foi cadastrado por: "+Sessao.getUser().getNome());
    }

    @Override 
    public void deletarLivro(CodigoISBN insLivID) throws SQLException {
        log.info("Livro: "+getLivrobyID(insLivID).getTitulo()+" deletado por: "+Sessao.getUser().getNome());

        PreparedStatement estado = super.getConneccao().prepareStatement("delete from livro where livro.isbn = ? ");
        estado.setString(1, insLivID.valorISBN());

        estado.execute();
        estado.close();
    }

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


}
