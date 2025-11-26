package projeto.Interfaces;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

//Classe tipo Dialog (tela pop-up) do JavaFX para inserção de dados usados no cadastro de um novo usuario,
//sendo utilizado dentro do AdminCTRL
public class CadastroUser extends Dialog<User>{
    
    @FXML
    private TextField insNome, insMail;
    @FXML
    private ToggleGroup cargo = new ToggleGroup();
    @FXML
    private RadioButton insAdmin = new RadioButton("Administrador"), 
                        insUser = new RadioButton("Usuario"), 
                        insClient = new RadioButton("Cliente");
    
    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela
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
                    try {
                        RadioButton selCargo = (RadioButton) cargo.getSelectedToggle();
                        return new User(
                            insNome.getText(),
                            insMail.getText(), 
                            Permissoes.valueOf(selCargo.getText().toUpperCase())
                        );
                    }catch(IllegalArgumentException e1){
                        JOptionPane.showMessageDialog(
                            null, 
                            "Erro por valor errado, cadastro cancelado \n motivo: "+e1.getMessage(),
                            "Erro", 
                            0
                        );
                        return null;
                    }catch(NullPointerException e3){
                        JOptionPane.showMessageDialog(
                            null, 
                            "Erro por função estar faltando, cadastro cancelado \n motivo: "+e3.getMessage(),
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
                "Erro por "+e2.getMessage() + ", cadastro cancelado",
                "Erro", 
                0
            );       
        }
    }
    
}
