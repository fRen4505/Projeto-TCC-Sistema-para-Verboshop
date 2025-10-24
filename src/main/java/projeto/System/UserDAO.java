package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private String separacao = ":!:";

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
                valores.getInt("quantidade"),
                valores.getString("isbn")
            );
            
            livros.add(liv);
        }
        return livros;
    }

    @Override
    public void alterarLivro(CodigoISBN insLivID, Livro insAlt) throws SQLException {
    
        PreparedStatement estado = super.getConneccao()
            .prepareStatement("update livro set quantidade = ? where livro.isbn = ? ");

        estado.setInt(1, insAlt.getQuantidade());
        estado.setString(2, insLivID.valorISBN());

        estado.execute();
        estado.close();
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
            Livro altLivro = insPedido.getLivroEncomendado(codigo);
            altLivro.diminuirQunatidade(qtnd);

            try {
                this.alterarLivro( new CodigoISBN(codigo), altLivro);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        PreparedStatement estado = super.getConneccao().prepareStatement("insert into pedido values( ? , ? , ? , ? , ? , ? ) ");

        estado.setString(1, insPedido.getCriador().getID().toString());
        estado.setString(2, insPedido.getCliente().getID().toString());
        estado.setString(3, insPedido.getPagamento().name());
        estado.setString(4, insPedido.getDataCriação().toString());
        estado.setString(5, concatenacaoISBNs);
        estado.setString(6, insPedido.getIDpedido().toString());

        estado.execute();
        estado.close();
    
    }

    @Override
    public void alterarPedido(UUID insID, Pedido insPedidoAlt) throws SQLException {
        PreparedStatement estado = super.getConneccao()
            .prepareStatement(" update pedido set metodoPagamento = ? , data = ? , livrosCodigo = ? where pedido.ID = ?");

        estado.setString(1, insPedidoAlt.getPagamento().toString());
        estado.setString(2, insPedidoAlt.getDataCriação().toString());

        for (int i = 0; i < insPedidoAlt.getEncomendas().size(); i++) {
            estado.setString(3, insPedidoAlt.getEncomendas().get(i).getISBN().toString());
        }

        estado.setString(4, insID.toString());

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
            
            User usr = new User(
                valores.getString("nome"), 
                valores.getString("email"),
                valores.getString("id"), 
                permissao
            );

            //if (usr.getFunção().equals(Permissoes.CLIENTE)) {        <===== talvez repor, tenho q ver
                usuarios.add(usr);
            //}
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

   
    @Override // <<< ======================================== JA FEITO
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
                            this.getLivros().get(i).getPreço().doubleValue(), 
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
                pedidos.getString("ID")
            );

            pedidosSalvos.add(pedi);
        }

        return pedidosSalvos;
    }

}
