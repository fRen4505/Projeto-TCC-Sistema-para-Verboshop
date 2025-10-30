package projeto.Interfaces;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class CadastroUser extends Dialog<User>{
    
    @FXML
    private TextField insNome, insMail;
    @FXML
    private RadioButton insAdmin, insUser, insClient;

    public CadastroUser(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Cadastro de novo Usuario");
            setDialogPane(tela);

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            setResultConverter(dialogButton -> {
                if(dialogButton == confirm) {
                    if (insAdmin.isSelected()) {
                        return new User(
                            insNome.getText(),
                            insMail.getText(), 
                            Permissoes.ADMINISTRADOR
                        );
                    }if (insUser.isSelected()) {
                        return new User(
                            insNome.getText(),
                            insMail.getText(), 
                            Permissoes.USUARIO
                        );                       
                    }if (insClient.isSelected()) {
                        return new User(
                            insNome.getText(),
                            insMail.getText(), 
                            Permissoes.CLIENTE
                        );
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            });
            
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por "+e1.getMessage() + ", cadastro cancelado",
                "Erro", 
                0
            );       
        }catch(NullPointerException e2){
            JOptionPane.showMessageDialog(
                null, 
                "Erro por valor errado ou vazio, cadastro cancelado \n motivo: "+e2.getMessage(),
                "Erro", 
                0
            );  
        }

    }
    
}
