package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.List;

import projeto.System.Models.Pedido;
import projeto.System.Models.User;

public interface PedidoInterface {
    
    public void criarPedido(Pedido insPedido) throws SQLException;

    public void alterarPedido(User insPedidoID, Pedido insPedidoAlt) throws SQLException;

    public List<Pedido> getPedidos() throws SQLException; 

}
