package projeto.Interfaces;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.User;

public class NovoClientePane extends Dialog<User>{

    @FXML
    private TextField insMail;

    @FXML
    private TextField insNome;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public NovoClientePane(){

        alert.setTitle("Erro");
        alert.setHeaderText(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/ClienteCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            this.setTitle("Cadastro de novo cliente");
            this.setDialogPane(tela);

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                try {
                    if(dialogButton == confirm) {
                        return new User(
                            insNome.getText(),
                            insMail.getText(), 
                            null
                        );
                    }
                } catch (Exception e) {
                    alert.setContentText("Erro por falta de dados, cadastro cancelado");
                    alert.showAndWait();
                    return null;
                }
                return null;
            });
            
        } catch (Exception e2) {
            alert.setContentText( e2.getMessage() + ", cadastro cancelado");
            alert.showAndWait();        
        }

    }

    
}
