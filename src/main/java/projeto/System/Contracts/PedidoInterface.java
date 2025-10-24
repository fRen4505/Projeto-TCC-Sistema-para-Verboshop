package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import projeto.System.Models.Pedido;

public interface PedidoInterface {
    
    public void criarPedido(Pedido insPedido) throws SQLException;

    public void alterarPedido(UUID insPedidoID, Pedido insPedidoAlt) throws SQLException;

    public List<Pedido> getPedidos() throws SQLException; 

}
