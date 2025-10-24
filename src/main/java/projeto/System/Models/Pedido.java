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

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEcomendas){
        this.Criador = insCriador;
        this.Cliente = insCLient;
        this.pagamento = insPaga;
        this.DataCriação = LocalDateTime.now();
        this.Encomendas = insEcomendas;
        this.IDPedido = UUID.randomUUID();
    }

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEcomendas, String insID){
        this.Criador = insCriador;
        this.Cliente = insCLient;
        this.pagamento = insPaga;
        this.DataCriação = LocalDateTime.now();
        this.Encomendas = insEcomendas;
        this.IDPedido = UUID.fromString(insID);
    }

    public void addEncomendas(Livro insLivro) {
        Encomendas.add(insLivro);
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
