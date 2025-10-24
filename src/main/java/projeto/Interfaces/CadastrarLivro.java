package projeto.Interfaces;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.Livro;

public class CadastrarLivro extends Dialog<Livro>{
    
    @FXML
    private TextField insTitulo, insAutor, insEdit, insPreco, insCode, insQtnd;
    
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    
    public CadastrarLivro(){

        alert.setTitle("Erro");
        alert.setHeaderText(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            this.setTitle("Cadastro de novo Livro");
            this.setDialogPane(tela);

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                try {
                    if (dialogButton == confirm) {
                        return new Livro(
                            insTitulo.getText(),
                            insAutor.getText(), 
                            insEdit.getText(), 
                            Double.parseDouble(insPreco.getText()), 
                            Integer.parseInt(insQtnd.getText()),
                            insCode.getText()
                        );
                    }else{
                        return null;
                    }

                } catch (Exception e) {
                    alert.setContentText("Erro nos dados, cadastro cancelado");
                    alert.showAndWait();
                    return null;
                }
            });

        } catch (Exception e2) {
            alert.setContentText( e2.getMessage() + ", cadastro cancelado");
            alert.showAndWait();        
        }

    }

}
