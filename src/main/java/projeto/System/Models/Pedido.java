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
    private List<Livro> Encomendas;
    private UUID IDPedido;
    private Integer Entregue;

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEncomendas){
        try {
            if (insCriador != null && insCLient!= null && insPaga!= null && insEncomendas!= null) {
                this.Criador = insCriador;
                this.Cliente = insCLient;
                this.pagamento = insPaga;
                this.DataCriação = LocalDateTime.now();
                this.Encomendas = insEncomendas;
                this.IDPedido = UUID.randomUUID();
            }
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta ou inexistencia de dados para pedido \n" + e.getMessage());
        }
    }

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEcomendas, String insID, Integer insEntregue){
        this(insCriador, insCLient, insPaga, insEcomendas);
        this.IDPedido = UUID.fromString(insID);
        this.Entregue = insEntregue;
    }

    public List<Livro> getEncomendas() {
        return Encomendas;
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
