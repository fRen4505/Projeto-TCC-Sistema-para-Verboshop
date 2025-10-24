package projeto.Interfaces;

import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import projeto.Sessao;
import projeto.System.AdminDAO;
import projeto.System.DAO;
import projeto.System.UserDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.valores.Permissoes;

public class LivroPane {

    @FXML
    private Pane livPane;

    @FXML
    private Button alterar = new Button(), deletar = new Button();

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

    private DAO dao;
    private Livro insLiv;

    public LivroPane(){}

    public Pane painel(Livro insLiv){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroPane.fxml"));
            Pane tela = loader.load();

            LivroPane controller = loader.getController();

            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {
                controller.dao = (AdminDAO) Sessao.getDAO();
            }else{
                controller.dao = (UserDAO) Sessao.getDAO();
                controller.alterar.setVisible(false);
                controller.deletar.setVisible(false);
            }
            
            controller.livTitulo.setText(livTitulo.getText() + insLiv.getTitulo());
            controller.livAutor.setText(livAutor.getText() + insLiv.getAutor());
            controller.livEditor.setText(livEditor.getText() + insLiv.getEditora());
            controller.livISBN.setText(livISBN.getText() + insLiv.getISBN().valorISBN());
            controller.livPreco.setText(livPreco.getText() + insLiv.getPreço().toString());
            if (insLiv.getQuantidade() > 0) {
                controller.livQtnd.setText(livQtnd.getText() + insLiv.getQuantidade().toString());
            } else {
                controller.livQtnd.setText(livQtnd.getText() + insLiv.getQuantidade().toString());
                //controller.livQtnd.setText(livQtnd.getText() + "Não há no estoque");
                tela.setStyle("-fx-background-color: #ff0000");
            }

            controller.insLiv = insLiv;
            
            return tela;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();        
        }

        return this.livPane;
    }

    public void livroDelete(ActionEvent e){
        try {
            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {
                AdminDAO daoAlt = ((AdminDAO) this.dao);
                
                daoAlt.deletarLivro(insLiv.getISBN());
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void livroAlterar(ActionEvent e){
        try {
            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {
                AdminDAO daoAlt = ((AdminDAO) this.dao);

                AlterarLivro dialog = new AlterarLivro(this.insLiv);
                Optional<Livro> resultado = dialog.showAndWait();

                daoAlt.alterarLivro(this.insLiv.getISBN(), resultado.get());
            }
            
        } catch (Exception e1) {
            // TODO: handle exception
        }

    }

}
