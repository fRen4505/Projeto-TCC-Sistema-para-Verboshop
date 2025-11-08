package projeto.System.Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import projeto.System.Models.valores.Pagamentos;

public class Pedido {
    
    private User Criador;
    private User Cliente; 
    private Pagamentos pagamento;
    private LocalDateTime DataCriação;
    private LocalDateTime DataEntrega;
    private List<Livro> Encomendas;
    private UUID IDPedido;
    private Integer Entregue;

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEncomendas){
        try {
            if (insCriador != null && insCLient!= null && insPaga!= null && insEncomendas != null) {
                this.Criador = insCriador;
                this.Cliente = insCLient;
                this.pagamento = insPaga;
                this.DataCriação = LocalDateTime.now();
                this.DataEntrega = DataCriação.plusDays(29); 
                this.Encomendas = insEncomendas;
                this.IDPedido = UUID.randomUUID();
            }
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta ou inexistencia de dados para pedido \n" + e.getMessage());
        }
    }

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEcomendas, String insID, Integer insEntregue, String insData, String insDataEntrega){
        this(insCriador, insCLient, insPaga, insEcomendas);
        this.IDPedido = UUID.fromString(insID);
        this.Entregue = insEntregue;
        this.DataCriação = LocalDateTime.parse(insData);
        this.DataEntrega = LocalDateTime.parse(insDataEntrega);
    }

    public List<Livro> getEncomendas() {
        if (Encomendas != null) {
            return Encomendas;
        } else {
            throw new NullPointerException("Falta ou inexistencia de dados para pedido \n");
        }
    }

    public Livro getLivroEncomendado(String ISBNins){
        for (Livro livro : Encomendas) {
            if (livro.getISBN().valorISBN().equals(ISBNins)) {
                return livro;
            }
        }
        return null;
    }

    public UUID getIDpedido(){
        return IDPedido;
    }

    public Integer getEntregue(){
        return Entregue;
    }

    public LocalDateTime getDataCriação() {
        return DataCriação;
    }

    public LocalDateTime getDataEntrega(){
        return DataEntrega;
    }

    public User getCliente() {
        return Cliente;
    }

    public User getCriador() {
        return Criador;
    }

    public Pagamentos getPagamento() {
        return pagamento;
    }



}
