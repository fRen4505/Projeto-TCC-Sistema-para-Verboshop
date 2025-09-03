package projeto.System.Models.valores;

public enum Pagamentos {

    DINHEIRO("dinheiro"), CARTAO("cartao"), PIX("pix");

    private String metodo;

    public String getNomes() {
        return this.metodo;
    }

    private Pagamentos(String metodos){
        this.metodo = metodos;
    }

}
