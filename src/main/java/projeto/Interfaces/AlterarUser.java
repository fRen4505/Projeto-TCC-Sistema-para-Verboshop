package projeto.Interfaces;

import javafx.scene.control.Dialog;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;;

public class AlterarUser extends Dialog<User>{

    @FXML
    private TextField altNome, altMail;
    @FXML
    private RadioButton altUser, altAdmin;

    public AlterarUser(User usr){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserAlteracaoPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Alteração de cadastro de Usuario");
            setDialogPane(tela);

            altNome.setText(usr.getNome());
            altMail.setText(usr.getEmail());

            switch (usr.getFunção()) {
                case ADMINISTRADOR: altAdmin.arm();
                    break;

                case USUARIO: altUser.arm();
                    break;
            
                default:
                    break;
            }

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == confirm) {
                    if (altAdmin.isSelected()) {
                        return new User(
                            altNome.getText(),
                            altMail.getText(), 
                            Permissoes.ADMINISTRADOR
                        );
                    }if (altUser.isSelected()) {
                        return new User(
                            altNome.getText(),
                            altMail.getText(), 
                            Permissoes.USUARIO
                        );                       
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            });

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por "+e1.getMessage() + ", alteração cancelada",
                "Erro", 
                0
            );    
        }

    }

}
