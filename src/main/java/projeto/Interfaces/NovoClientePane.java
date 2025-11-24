package projeto.Interfaces;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
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

    public NovoClientePane(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/ClienteCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Cadastro de novo cliente");
            setDialogPane(tela);

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == confirm) {
                    return new User(
                        insNome.getText(),
                        insMail.getText(), 
                        null
                    );  
                }else{
                    return null;
                }
            });

        }catch (NullPointerException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, dados vazios e não cadastrados, cancelado \n motivo "+e1.getMessage(),
                "erro", 
                0
            );      

        } catch (IOException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por input incorreta, cancelado \n motivo "+e2.getMessage(),
                "erro", 
                0
            );            
        }

    }
  
}
