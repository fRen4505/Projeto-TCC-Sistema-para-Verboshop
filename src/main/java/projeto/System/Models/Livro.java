package projeto.System.Models;

import projeto.System.Models.valores.CodigoISBN;
import projeto.System.Models.valores.Dinheiro;

public class Livro {
    
    private String Titulo;
    private String Autor;
    private String Editora;
    private CodigoISBN ISBN;
    private Dinheiro Preço;
    private Integer Quantidade = 0;

    public Livro(String insTitulo, String insAutor, String insEdit, double insPreço, String insCodigo) {
        try {
            if (insTitulo != "" && insAutor != "" && insEdit != "" && insCodigo != "") {
                this.Titulo = insTitulo;
                this.Autor = insAutor;
                this.Editora = insEdit;
                this.Preço = new Dinheiro(insPreço);
                this.ISBN = new CodigoISBN(insCodigo);
            }
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dados inseridos para livro são invalidos \n" + e.getMessage());
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta de dados para livro \n" + e.getMessage());
        }
    }

    public Livro(String insTitulo, String insAutor, String insEdit, double insPreço, Integer insQtnd) {
        try {
            if (insTitulo != "" && insAutor != "" &&  insEdit!= "") {
                this.Titulo = insTitulo;
                this.Autor = insAutor;
                this.Editora = insEdit;
                this.Preço = new Dinheiro(insPreço);
                this.Quantidade = insQtnd;
            } 
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dados inseridos para livro são invalidos \n" + e.getMessage());
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta de dados para livro \n" + e.getMessage());
        }
               
    }

    public Livro(String insTitulo, String insAutor, String insEdit, double insPreço, Integer insQtnd, String insCodigo) {
        this(insTitulo, insAutor, insEdit, insPreço, insCodigo);
        this.Quantidade = insQtnd;
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

    public Dinheiro getPreço() {
        return Preço;
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
