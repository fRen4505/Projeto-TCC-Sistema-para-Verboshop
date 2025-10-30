package projeto.System;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import projeto.System.Contracts.LivroInterface;
import projeto.System.Contracts.PedidoInterface;
import projeto.System.Contracts.UserInterface;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Permissoes;

public abstract class PerfilDAO extends DAO implements UserInterface, LivroInterface, PedidoInterface {

    private User currentUser;
    private Permissoes nivel;

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

    @Override
    public abstract void criarPedido(Pedido insPedido) throws SQLException;   

    @Override
    public abstract void deletarPedido(UUID insPedidoID) throws SQLException;

    @Override
    public abstract List<Pedido> getPedidos() throws SQLException;

    @Override
    public abstract void entreguePedido(UUID insPedidoID) throws SQLException;

    public User getCurrentUser() {
        return currentUser;
    }

    public Permissoes getNivel() {
        return nivel;
    }

    public Permissoes getCurrentUserPermissiao(){
        return this.currentUser.getFunção();
    }

    
}
