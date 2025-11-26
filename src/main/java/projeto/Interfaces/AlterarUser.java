package projeto.Interfaces;

import javafx.scene.control.Dialog;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

//Classe tipo Dialog (tela pop-up) do JavaFX para inserção de dados usados na alteração de um cadastro de usuario,
//sendo utilizada dentro do UserPane
public class AlterarUser extends Dialog<User>{

    @FXML
    private TextField altNome, altMail;
    @FXML
    private ToggleGroup cargo = new ToggleGroup();
    @FXML
    private RadioButton altUser = new RadioButton("Usuario"), 
                        altAdmin = new RadioButton("Administrador");

    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela, completando os dados com o informções do usuario
    public AlterarUser(User usr){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserAlteracaoPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Alteração de cadastro de Usuario");
            setDialogPane(tela);

            altNome.setText(usr.getNome());
            altMail.setText(usr.getEmail());

            if (usr.getFunção() == Permissoes.CLIENTE) {
                altAdmin.setVisible(false);
                altUser.setVisible(false);
            }

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == confirm) {
                    try {
                        if (usr.getFunção() != Permissoes.CLIENTE) {
                            RadioButton selCargo = (RadioButton) cargo.getSelectedToggle();
                            return new User(
                                altNome.getText(),
                                altMail.getText(), 
                                Permissoes.valueOf(selCargo.getText().toUpperCase())
                            );   
                        }else{
                            return new User(
                                altNome.getText(),
                                altMail.getText(), 
                                Permissoes.CLIENTE
                            );   
                        } 
                    }catch(IllegalArgumentException e1){
                        JOptionPane.showMessageDialog(
                            null, 
                            "Erro por valor errado, alteração cancelada \n motivo: "+e1.getMessage(),
                            "Erro", 
                            0
                        );
                        return null;
                    }catch(NullPointerException e3){
                        JOptionPane.showMessageDialog(
                            null, 
                            "Erro por função estar faltando, alteração cancelada \n motivo: "+e3.getMessage(),
                            "Erro", 
                            0
                        );
                        return null;
                    }
                }else{
                    return null;
                }
            });
   
        }catch (IOException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, alteração cancelada, motivo: "+e2.getMessage() ,
                "Erro", 
                0
            );    
        }

    }

}
