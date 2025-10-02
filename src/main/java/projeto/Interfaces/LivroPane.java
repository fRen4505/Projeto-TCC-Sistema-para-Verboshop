package projeto.Interfaces;

import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import projeto.System.AdminDAO;
import projeto.System.DAO;
import projeto.System.Models.Livro;
import projeto.System.Models.valores.CodigoISBN;

public class LivroPane {

    @FXML
    private Pane livPane;

    @FXML
    private Button alterar, deletar;

    @FXML
    private Label livTitulo = new Label("Titulo: ");
    @FXML
    private Label livAutor = new Label("Autor: ");
    @FXML
    private Label livEditor = new Label("Editora: ");
    @FXML
    private Label livISBN = new Label("ISBN: ");
    @FXML
    private Label livPreco = new Label("Preço: ");
    @FXML
    private Label livQtnd = new Label("Quantidade: ");

    private CodigoISBN isbn;
    private AdminDAO dao;

    public LivroPane(){}

    public Pane painel(Livro insLiv, DAO insDao){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroPane.fxml"));
            Pane tela = loader.load();

            LivroPane controller = loader.getController();

            controller.livTitulo.setText(livTitulo.getText() + insLiv.getTitulo());
            controller.livAutor.setText(livAutor.getText() + insLiv.getAutor());
            controller.livEditor.setText(livEditor.getText() + insLiv.getEditora());
            controller.livISBN.setText(livISBN.getText() + insLiv.getISBN().getISBN());
            controller.livPreco.setText(livPreco.getText() + insLiv.getPreço().toString());
            if (insLiv.getQuantidade() != 0) {
                controller.livQtnd.setText(livQtnd.getText() + insLiv.getQuantidade().toString());
            } else {
                controller.livQtnd.setText(livQtnd.getText() + "Não definido ainda");
            }

            controller.isbn = new CodigoISBN(insLiv.getISBN().getISBN());
            controller.dao = (AdminDAO) insDao;
            
            return tela;

        } catch (Exception e) {
            e.printStackTrace();        
        }

        return this.livPane;
    }

    /*  public Pane painel(String insTitu, String insAutor, String insEdit, String insISBN, String insPreco, String insQtnd, DAO insDao ){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroPane.fxml"));
            Pane tela = loader.load();

            LivroPane controller = loader.getController();

            controller.livTitulo.setText(livTitulo.getText() + insTitu);
            controller.livAutor.setText(livAutor.getText() + insAutor);
            controller.livEditor.setText(livEditor.getText() + insEdit);
            controller.livISBN.setText(livISBN.getText() + insISBN);
            controller.livPreco.setText(livPreco.getText() + insPreco);
            controller.livQtnd.setText(livQtnd.getText() + insQtnd);

            controller.isbn = new CodigoISBN(insISBN);
            controller.dao = (AdminDAO) insDao;
            
            return tela;

        } catch (Exception e) {
            e.printStackTrace();        
        }

        return this.livPane;
    }
*/
   
    public void livroDelete(ActionEvent e){

    }

    public void livroAlterar(ActionEvent e){

    }

}
