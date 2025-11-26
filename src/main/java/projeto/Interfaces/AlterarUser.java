package projeto.Interfaces;

import javafx.scene.control.Dialog;

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

    Permissoes funcao;

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
                funcao = Permissoes.CLIENTE;
            }else{
                RadioButton selCargo = (RadioButton) cargo.getSelectedToggle();
                funcao = Permissoes.valueOf(selCargo.getText().toUpperCase());
            }

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton -> {
                if(dialogButton == confirm) {

                    return new User(
                        altNome.getText(),
                        altMail.getText(), 
                        funcao
                    );

                }else{
                    return null;
                }
            });

        }catch (NullPointerException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, alteração cancelada, motivo: "+e1.getMessage() ,
                "Erro", 
                0
            );    
        }catch (Exception e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, alteração cancelada, motivo: "+e2.getMessage() ,
                "Erro", 
                0
            );    
        }

    }

}
