package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import projeto.System.Contracts.LivroAdminInterface;
import projeto.System.Contracts.UserAdminInterface;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

public class AdminDAO extends PerfilDAO implements LivroAdminInterface, UserAdminInterface {

    private static AdminDAO Instancia;
    private String separacao = ":!:";

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

        PreparedStatement estado = super.getConneccao().prepareStatement("insert into user values(? , ? , ? , ? )");

        estado.setString(1, insUser.getNome());
        estado.setString(2, insUser.getEmail());
        estado.setString(3, insUser.getID().toString());
        estado.setString(4, insUser.getFunção().toString());
    
        estado.execute();
        estado.close();
    }

    @Override 
    public void alterarUser(UUID insUserID, User insUserAlt) throws SQLException {

        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update user set nome = ? , email = ? , permissao = ? where user.id = ?");

        estado.setString(1, insUserAlt.getNome());
        estado.setString(2, insUserAlt.getEmail());
        estado.setString(3, insUserAlt.getFunção().toString());

        estado.setString(4, insUserID.toString());

        estado.execute();
        estado.close();
    }

    @Override 
    public void deletarUser(UUID insUserID) throws SQLException {
        PreparedStatement estado = super.getConneccao().prepareStatement("delete from user where user.id = ? ");
        estado.setString(1, insUserID.toString());

        estado.execute();
        estado.close();
    }

    @Override 
    public void alterarLivro(CodigoISBN insLivID, Livro insLivAlt) throws SQLException {
        
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
    }

    @Override 
    public void deletarLivro(CodigoISBN insLivID) throws SQLException {
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
            Livro liv = new Livro(valores.getString("titulo"), 
                valores.getString("autor"), 
                valores.getString("editora"), 
                valores.getDouble("dinheiro"), 
                valores.getInt("quantidade"),
                valores.getString("isbn")
            );
            
            livros.add(liv);
        }
        return livros;
    }

    @Override
    public void criarPedido(Pedido insPedido) throws SQLException {
        
        String concatenacaoISBNs ="";
        HashMap<String, Integer> encomendadosList = new HashMap<>();
        
        for (int i = 0; i < insPedido.getEncomendas().size(); i++) {
            Livro encomendado = insPedido.getEncomendas().get(i);

            concatenacaoISBNs = concatenacaoISBNs + separacao + insPedido.getEncomendas().get(i).getISBN().valorISBN();

            if (encomendadosList.containsKey(encomendado.getISBN().valorISBN())) {
                encomendadosList.put(
                    encomendado.getISBN().valorISBN(), 
                    (encomendadosList.get(encomendado.getISBN().valorISBN()) + 1)
                );
            } else {
                encomendadosList.putIfAbsent(encomendado.getISBN().valorISBN(), 1);
            }

        }

        encomendadosList.forEach((codigo, qtnd) -> {
            
            try {
                Livro altLivro = insPedido.getLivroEncomendado(codigo);
                altLivro.diminuirQunatidade(qtnd);

                this.alterarLivro( new CodigoISBN(codigo), altLivro);
            } catch (SQLException e) {
                //TODO catch
                //throw new SQLException("Falha ao acessar dados de livros para cadastro de pedido");
            }
        });

        PreparedStatement estado = super.getConneccao().prepareStatement("insert into pedido values( ? , ? , ? , ? , ? , ? , ? ) ");

        estado.setString(1, insPedido.getCriador().getID().toString());
        estado.setString(2, insPedido.getCliente().getID().toString());
        estado.setString(3, insPedido.getPagamento().name());
        estado.setString(4, insPedido.getDataCriação().toString());
        estado.setString(5, concatenacaoISBNs);
        estado.setString(6, insPedido.getIDpedido().toString());
        estado.setInt(7, 0);

        estado.execute();
        estado.close();
    
    }

    @Override  
    public void deletarPedido(UUID insPedidoID) throws SQLException {
        PreparedStatement estado = super.getConneccao().prepareStatement("delete from pedido where pedido.ID = ? ");
        estado.setString(1, insPedidoID.toString());

        estado.execute();
        estado.close();
    
    }

    @Override
    public List<Pedido> getPedidos() throws SQLException {

        List<Pedido> pedidosSalvos = new ArrayList<Pedido>();

        Statement estado = super.getConneccao().createStatement();
        ResultSet pedidos = estado.executeQuery(" select * from pedido ");

        while (pedidos.next()) {

            String[] isbns = pedidos.getString("livrosCodigo").split(separacao);

            User cliente = null;
            User funcionario = null;
            List<Livro> livros = new ArrayList<Livro>();

            for (int i = 0; i < this.getUsers().size(); i++) {
                if (this.getUsers().get(i).getID().toString().equals(pedidos.getString("clienteID"))) {
                    cliente = new User(
                        this.getUsers().get(i).getNome(), 
                        this.getUsers().get(i).getEmail(),
                        this.getUsers().get(i).getID().toString(),
                        this.getUsers().get(i).getFunção()
                    );
                }
                if (this.getUsers().get(i).getID().toString().equals(pedidos.getString("criadorID"))) {
                    funcionario = new User(
                        this.getUsers().get(i).getNome(), 
                        this.getUsers().get(i).getEmail(),
                        this.getUsers().get(i).getID().toString(), 
                        this.getUsers().get(i).getFunção()
                    );
                }
            }
            for (int i = 0; i < this.getLivros().size(); i++) {

                for (int j = 0; j < isbns.length; j++) {
                    if (this.getLivros().get(i).getISBN().valorISBN().equals(isbns[j])) {
                        Livro encomendado = new Livro(
                            this.getLivros().get(i).getTitulo(), 
                            this.getLivros().get(i).getAutor(), 
                            this.getLivros().get(i).getEditora(), 
                            this.getLivros().get(i).getPreço().getQuantiaDouble(), 
                            this.getLivros().get(i).getISBN().valorISBN()
                        );
                        livros.add(encomendado);
                    }
                }
            }

            Pedido pedi = new Pedido(
                funcionario,
                cliente, 
                Pagamentos.valueOf(pedidos.getString("metodoPagamento")),
                livros,
                pedidos.getString("ID"),
                pedidos.getInt("entregue")
            );

            pedidosSalvos.add(pedi);
        }

        return pedidosSalvos;
    }

    @Override
    public void entreguePedido(UUID insPedidoID) throws SQLException{
        PreparedStatement estado = super.getConneccao().prepareStatement(" update pedido set entregue = ? where pedido.ID = ? ");
        estado.setInt(1, 1);
        estado.setString(2, insPedidoID.toString());

        estado.execute();
        estado.close();
    }

}
