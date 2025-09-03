package projeto.System.Models;

import java.time.LocalDateTime;
import java.util.List;

import projeto.System.Models.valores.Pagamentos;

public class Pedido {
    
    private User Criador;
    private User Cliente; //ou String Cliente
    private Pagamentos pagamento;
    private LocalDateTime DataCriação;
    private List<Livro> Encomendas;

    public Pedido(User insCriador, User insCLient, Pagamentos insPaga, List<Livro> insEcomendas){
        this.Criador = insCriador;
        this.Cliente = insCLient;
        this.pagamento = insPaga;
        this.DataCriação = LocalDateTime.now();
        this.Encomendas = insEcomendas;
    }

    public void addEncomendas(Livro insLivro) {
        Encomendas.add(insLivro);
    }

    public List<Livro> getEncomendas() {
        return Encomendas;
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
