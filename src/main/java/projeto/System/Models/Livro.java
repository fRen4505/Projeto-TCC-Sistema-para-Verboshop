package projeto.System.Models;

import java.math.BigDecimal;

import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Dinheiro;

public class Livro {
    
    private String Titulo;
    private String Autor;
    private String Editora;
    private CodigoISBN ISBN;
    private Dinheiro Preço;
    private Integer Quantidade;

    public Livro(String insTitulo, String insAutor, String insEdit, double insPreço, String insCodigo) {
        this.Titulo = insTitulo;
        this.Autor = insAutor;
        this.Editora = insEdit;
        this.Preço = new Dinheiro(insPreço);
        this.ISBN = new CodigoISBN(insCodigo);
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getAutor() {
        return Autor;
    }
    
    public String getEditora() {
        return Editora;
    }

    public CodigoISBN getISBN() {
        return ISBN;
    }

    public BigDecimal getPreço() {
        return Preço.getQuantia();
    }
    public void setPreço(double preço) {
        Preço.novaQuantia(preço);
    }

    public Integer getQuantidade() {
        return Quantidade;
    }
    public void aumentarQuantidade(Integer insQuantidade) {
        if (insQuantidade >= 0) {
            this.Quantidade = Quantidade + insQuantidade;
        }
    }
    public void diminuirQunatidade(Integer insQuantidade){
        if (insQuantidade > 0) {
            if ( (Quantidade - insQuantidade) < 0 ) {
                this.Quantidade = 0;
            } else {
                this.Quantidade = Quantidade - insQuantidade;
            }
        }
    }

}
