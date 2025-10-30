package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import projeto.System.Models.Pedido;

public interface PedidoInterface {
    
    public void criarPedido(Pedido insPedido) throws SQLException;

    public void deletarPedido(UUID insPedidoID) throws SQLException;

    public List<Pedido> getPedidos() throws SQLException; 

    public void entreguePedido(UUID insPedidoID) throws SQLException;

}
