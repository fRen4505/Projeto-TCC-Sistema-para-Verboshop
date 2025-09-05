package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projeto.System.Contracts.LivroInterface;
import projeto.System.Contracts.PedidoInterface;
import projeto.System.Contracts.UserInterface;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

public class UserDAO extends DAO implements LivroInterface, PedidoInterface, UserInterface {

    private static UserDAO Instancia;

    private UserDAO(User insCurrent,  Permissoes permissaoDAO) {
        super(insCurrent, permissaoDAO);
    }

    public static UserDAO getInstancia(User current){
        if (Instancia == null) {
            Permissoes insPermi = Permissoes.USUARIO;
            Instancia = new UserDAO(current, insPermi);
            return Instancia;
        }else {
            return Instancia;
        }
    }
    
    @Override
    public List<Livro> getLivros() throws SQLException {
    
        List<Livro> livros = new ArrayList<Livro>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet valores = estado.executeQuery("select * from livro");

        while (valores.next()) {
            Livro liv = new Livro(valores.getString("titulo"), 
                valores.getString("autor"), 
                valores.getString("editora"), 
                valores.getDouble("dinheiro"), 
                valores.getString("isbn"));
            
            livros.add(liv);
        }
        return livros;
    }

    @Override
    public void alterarLivro(CodigoISBN insLivID, Livro insAlt) throws SQLException {
    
        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update livro set quantidade = ? where livro.isbn = ? ");

        estado.setInt(1, insAlt.getQuantidade());
        estado.setString(2, insLivID.getISBN());

        estado.execute();
        estado.close();
    }
    

    @Override
    public void criarPedido(Pedido insPedido) throws SQLException {
        PreparedStatement estado = super.getConneccao().prepareStatement("insert into pedido values( ? , ? , ? , ? , ? ) ");
        
        estado.setString(1, insPedido.getCriador().getID().toString());
        estado.setString(2, insPedido.getCliente().getID().toString());
        estado.setString(3, insPedido.getPagamento().name());
        estado.setString(4, insPedido.getDataCriação().toString());

        for (int i = 0; i < insPedido.getEncomendas().size(); i++) {
            estado.setString(5, insPedido.getEncomendas().get(i).getISBN().getISBN());
        }

        estado.execute();
        estado.close();
    }

    @Override
    public void alterarPedido(User insPedidoID, Pedido insAlt) throws SQLException {
        
        PreparedStatement estado = super.getConneccao()
            .prepareStatement(" update pedido set metodoPagamento = ? , data = ? , livrosCodigo = ? where pedido.clienteID = ?");

        estado.setString(1, insAlt.getPagamento().toString());
        estado.setString(2, insAlt.getDataCriação().toString());

        for (int i = 0; i < insAlt.getEncomendas().size(); i++) {
            estado.setString(3, insAlt.getEncomendas().get(i).getISBN().getISBN());
        }

        estado.setString(4, insPedidoID.getID().toString());

        estado.execute();
        estado.close();
    }

    
    @Override
    public List<User> getUsers() throws SQLException {
        
        List<User> usuarios = new ArrayList<User>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet valores = estado.executeQuery("select * from user");

        while (valores.next()) {
            Permissoes permissao = Permissoes.valueOf(valores.getString("permissao"));
            
            User usr = new User(valores.getString("nome"), 
                valores.getString("email"), permissao);

            if (usr.getFunção().equals(Permissoes.CLIENTE)) {
                usuarios.add(usr);
            }
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
    }

    @Override
    public List<Pedido> getPedidos() throws SQLException {
        
        List<Pedido> pedidosSalvos = new ArrayList<Pedido>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet pedidos = estado.executeQuery(" select * from pedido ");

        while (pedidos.next()) {

            AdminDAO getUsers = AdminDAO.getInstancia(getCurrentUser());

            User cliente = null;
            User funcionario = null;
            List<Livro> livros = new ArrayList<Livro>();

            for (int i = 0; i < getUsers.getUsers().size(); i++) {
                if (getUsers.getUsers().get(i).getID().toString() == pedidos.getString("clienteID")) {
                    cliente = new User(
                        getUsers.getUsers().get(i).getNome(), 
                        getUsers.getUsers().get(i).getEmail(), 
                        getUsers.getUsers().get(i).getFunção()
                    );
                }
                if (getUsers.getUsers().get(i).getID().toString() == pedidos.getString("criadorID")) {
                    funcionario = new User(
                        getUsers.getUsers().get(i).getNome(), 
                        getUsers.getUsers().get(i).getEmail(), 
                        getUsers.getUsers().get(i).getFunção()
                    );
                }
            }
            for (int i = 0; i < this.getLivros().size(); i++) {
                if (this.getLivros().get(i).getISBN().toString() == pedidos.getString("livrosCodigo")) {
                    Livro encomendado = new Livro(
                        this.getLivros().get(i).getTitulo(), 
                        this.getLivros().get(i).getAutor(), 
                        this.getLivros().get(i).getEditora(), 
                        this.getLivros().get(i).getPreço().doubleValue(), 
                        this.getLivros().get(i).getISBN().getISBN()
                    );

                    livros.add(encomendado);
                }
            }

            Pedido pedi = new Pedido(funcionario, cliente, Pagamentos.valueOf(pedidos.getString("metodoPagamento")) , livros);
            pedidosSalvos.add(pedi);
        }

        return pedidosSalvos;
    }

}
