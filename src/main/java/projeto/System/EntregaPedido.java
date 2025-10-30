package projeto.System;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import projeto.Sessao;
import projeto.System.Models.Pedido;

public class EntregaPedido {

    private final ScheduledExecutorService verificar = Executors.newScheduledThreadPool(1);
    private LocalDateTime dataPosEntrega;
    private Pedido pedidoEntregue;
    private PerfilDAO dao = Sessao.getDAO();

    public EntregaPedido(LocalDateTime insData, Pedido insPedido) {
        this.dataPosEntrega = insData.plusHours(1);//.plusDays(1); //TESTE MUDAR PRA DIAS DEPOPIS DE TESTAR
        this.pedidoEntregue = insPedido;
    }

    public void iniciar() {
        Runnable checkTask = () -> {
            try {
                if (LocalDateTime.now().isAfter(dataPosEntrega)) {
                    this.dao.deletarPedido(pedidoEntregue.getIDpedido());
                }
            } catch (SQLException e) {
                // TODO: catch
                e.printStackTrace();
            }
        };

        verificar.scheduleAtFixedRate(checkTask, 0, 10, TimeUnit.MINUTES); //TESTE MUDAR PRA DIAS DEPOPIS DE TESTAR
    }

    public void parar() {
        verificar.shutdown();
    }

}
