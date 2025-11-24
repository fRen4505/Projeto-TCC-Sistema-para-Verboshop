package projeto.System;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import projeto.Sessao;
import projeto.System.Contracts.LivroInterface;
import projeto.System.Contracts.PedidoInterface;
import projeto.System.Contracts.UserInterface;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

public abstract class PerfilDAO extends DAO implements UserInterface, LivroInterface, PedidoInterface {

    private User currentUser;
    private Permissoes nivel;
    
    protected final Logger log = LoggerFactory.getLogger(PerfilDAO.class);
    private String separacao = ":!:";

    protected PerfilDAO(User insCurrent, Permissoes permissao){ 
        if (insCurrent.getFunção().equals(permissao)) {
            this.currentUser = insCurrent;
            this.nivel = permissao;
        }
    }

    @Override
    public abstract List<User> getUsers() throws SQLException;

    @Override
    public abstract void adicionarUser(User insUser) throws SQLException;

    @Override
    public abstract List<Livro> getLivros() throws SQLException;

    @Override
    public abstract void alterarLivro(CodigoISBN insLivID, Livro insAlt) throws SQLException;

    private Pedido getPedidobyID(UUID insPediID) throws SQLException {

        User cliente = null;
        User funcionario = null;
        List<Livro> livros = new ArrayList<Livro>();

        PreparedStatement estado = super.getConneccao().prepareStatement(" select * from pedido where pedido.ID = ? ");
        estado.setString(1, insPediID.toString());

        ResultSet pedidos = estado.executeQuery();

        for (User usr : this.getUsers()) {
            if (usr.getID().toString().equals(pedidos.getString("clienteID"))) {
                cliente = new User(
                    usr.getNome(), 
                    usr.getEmail(),
                    usr.getID().toString(),
                    usr.getFunção()
                );
            }
            if (usr.getID().toString().equals(pedidos.getString("criadorID"))) {
                funcionario = new User(
                    usr.getNome(), 
                    usr.getEmail(),
                    usr.getID().toString(), 
                    usr.getFunção()
                );
            }
        }

        String[] isbns = pedidos.getString("livrosCodigo").split(separacao);
        for (Livro livGet : this.getLivros()) {

            for (int j = 0; j < isbns.length; j++) {
                if (livGet.getISBN().valorISBN().equals(isbns[j])) {
                    livros.add(new Livro(
                        livGet.getTitulo(), 
                        livGet.getAutor(), 
                        livGet.getEditora(), 
                        livGet.getPreço().getQuantiaDouble(), 
                        livGet.getISBN().valorISBN()
                    ));
                }
            }

        }

        return new Pedido(
            funcionario,
            cliente, 
            Pagamentos.valueOf(pedidos.getString("metodoPagamento")),
            livros,
            pedidos.getString("ID"),
            pedidos.getInt("entregue"),
            pedidos.getString("data"),
            pedidos.getString("dataEntrega")
        );
        
    }

    @Override
    public void criarPedido(Pedido insPedido) throws SQLException {
        
        String concatenacaoISBNs ="";
        String titulos = " ";
        HashMap<String, Integer> encomendadosList = new HashMap<>();
        
        for (Livro insLiv : insPedido.getEncomendas()) {

            concatenacaoISBNs = concatenacaoISBNs + separacao + insLiv.getISBN().valorISBN();

            if (encomendadosList.containsKey(insLiv.getISBN().valorISBN())) {
                encomendadosList.put(
                    insLiv.getISBN().valorISBN(), 
                    (encomendadosList.get(insLiv.getISBN().valorISBN()) + 1)
                );
            } else {
                encomendadosList.putIfAbsent(insLiv.getISBN().valorISBN(), 1);
                titulos = titulos + insLiv.getTitulo() + " - ";
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

        PreparedStatement estado = super.getConneccao().prepareStatement("insert into pedido values( ? , ? , ? , ? , ? , ? , ? , ? ) ");

        estado.setString(1, insPedido.getCriador().getID().toString());
        estado.setString(2, insPedido.getCliente().getID().toString());
        estado.setString(3, insPedido.getPagamento().name());
        estado.setString(4, insPedido.getDataCriação().toString());
        estado.setString(5, insPedido.getDataEntrega().toString());
        estado.setString(6, concatenacaoISBNs);
        estado.setString(7, insPedido.getIDpedido().toString());
        estado.setInt(8, 0);

        estado.execute();
        estado.close();
        log.info("Pedido para: "+insPedido.getCliente().getNome()+" com os "+insPedido.getEncomendas().size()+" livros: "+titulos+" foi cadastrado por: "+Sessao.getUser().getNome());
    }

    protected void deletarPedido(UUID insPedidoID) throws SQLException {
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

            for (User getUsr : this.getUsers()) {
                if (getUsr.getID().toString().equals(pedidos.getString("clienteID"))) {
                    cliente = new User(
                        getUsr.getNome(), 
                        getUsr.getEmail(),
                        getUsr.getID().toString(),
                        getUsr.getFunção()
                    );
                }
                if (getUsr.getID().toString().equals(pedidos.getString("criadorID"))) {
                    funcionario = new User(
                        getUsr.getNome(), 
                        getUsr.getEmail(),
                        getUsr.getID().toString(), 
                        getUsr.getFunção()
                    );
                }
            }
            for (Livro getLiv : this.getLivros()) {

                for (int j = 0; j < isbns.length; j++) {
                    if (getLiv.getISBN().valorISBN().equals(isbns[j])) {
                        livros.add(new Livro(
                            getLiv.getTitulo(), 
                            getLiv.getAutor(), 
                            getLiv.getEditora(), 
                            getLiv.getPreço().getQuantiaDouble(), 
                            getLiv.getISBN().valorISBN()
                        ));
                    }
                }
            }

            pedidosSalvos.add(new Pedido(
                funcionario,
                cliente, 
                Pagamentos.valueOf(pedidos.getString("metodoPagamento")),
                livros,
                pedidos.getString("ID"),
                pedidos.getInt("entregue"),
                pedidos.getString("data"),
                pedidos.getString("dataEntrega")
            ));
        }

        return pedidosSalvos;
    }

    public Boolean pedidoContemLivro(CodigoISBN livCode) throws SQLException{

        for (Pedido ped : this.getPedidos()) {
            for (Livro livro : ped.getEncomendas()) {
            
                if (livro.getISBN().valorISBN().equals(livCode.valorISBN())) {
                    return true;
                } else {
                    continue;
                }
            }
        }
       
        return false;
    
    }

    @Override
    public void entreguePedido(UUID insPedidoID) throws SQLException{
        Pedido insPedido = this.getPedidobyID(insPedidoID);

        if (insPedido.getEntregue() == 1 && LocalDateTime.now().isAfter(insPedido.getDataEntrega())) {
            log.info("Pedido para: "+insPedido.getCliente().getNome()+" com "+insPedido.getEncomendas().size()+" livros foi possivelmente entregue e retirado dos dados");

            this.deletarPedido(insPedido.getIDpedido());
        }else{
            log.info("Pedido para: "+insPedido.getCliente().getNome()+" com "+insPedido.getEncomendas().size()+" livros foi marcado como entregue por: "+Sessao.getUser().getNome());

            PreparedStatement estado = super.getConneccao().prepareStatement(" update pedido set entregue = ? where pedido.ID = ? ");
            estado.setInt(1, 1);
            estado.setString(2, insPedidoID.toString());
    
            estado.execute();
            estado.close();
        }
        
    }

    @Override
    public void cancelarPedido(UUID insPedidoID) throws SQLException {
        Pedido insPedido = this.getPedidobyID(insPedidoID);

        log.info("Pedido para: "+insPedido.getCliente().getNome()+" com "+insPedido.getEncomendas().size()+" livros foi cancelado por: "+Sessao.getUser().getNome());

        for (Livro val : this.getLivros()) {
            insPedido.getEncomendas().forEach((liv) -> {
                if (val.getISBN().valorISBN().equals(liv.getISBN().valorISBN())) {
                    val.aumentarQuantidade(1);
                }
            });
            this.alterarLivro(val.getISBN(), val);               
        }
        this.deletarPedido(insPedidoID);   
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Permissoes getNivel() {
        return nivel;
    }

}
