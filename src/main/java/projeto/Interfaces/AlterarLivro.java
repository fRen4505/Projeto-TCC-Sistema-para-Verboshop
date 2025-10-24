package projeto.Interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.Livro;

public class AlterarLivro extends Dialog<Livro>{
    
    @FXML
    private Label qtnd;
    @FXML
    private TextField altTitulo, altAutor, altEdit, altPreco ;
    @FXML
    private Button add, sub;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public AlterarLivro(Livro ins){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroAlteracaoPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            this.setTitle("Alteração de dados de Livro");
            this.setDialogPane(tela);

            altTitulo.setText(ins.getTitulo());
            altAutor.setText(ins.getAutor());
            altEdit.setText(ins.getEditora());
            altPreco.setText(ins.getPreço().toString());
            qtnd.setText(ins.getQuantidade() + "");

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {
                    return new Livro(
                        altTitulo.getText(),
                        altAutor.getText(), 
                        altEdit.getText(), 
                        Double.parseDouble(altPreco.getText()), 
                        Integer.parseInt(qtnd.getText())
                    );
                } else {
                    return ins;
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void addAlt(ActionEvent a){
        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "");
    }

    public void subAlt(ActionEvent s){
        if (Integer.parseInt(qtnd.getText()) > 0) {
            if ( (Integer.parseInt(qtnd.getText()) - 1) < 0 ) {
                qtnd.setText("0");
            } else {
                qtnd.setText( 
                    (    (Integer.parseInt(qtnd.getText()) - 1)    )+""
                );
            }
        }
    }

}
