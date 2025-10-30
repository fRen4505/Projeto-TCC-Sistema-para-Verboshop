package projeto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import projeto.System.InitDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class Login {

    private Stage stage;
    private static InitDAO verificação = InitDAO.getInstancia(); 

    private List<User> usuarios;

    @FXML
    private TextField insNome, insMail;
    @FXML
    private Button logButton, telaLogin;
    @FXML
    private ToggleGroup funcao = new ToggleGroup();
    @FXML
    private RadioButton adminButton = new RadioButton("Administrador"); 
    @FXML
    private RadioButton userButton = new RadioButton("Usuario");
        
    public Login(Stage inStage){

        try {
            this.stage = inStage;
            this.usuarios = verificação.getUsers();

            if (this.usuarios.isEmpty()) {
                this.telaCadastro();
            } else {
                this.telaLogin();
            }
            
        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar e recolher os dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }
    
    public void telaLogin(){
        try {
            adminButton.setToggleGroup(funcao);
            userButton.setToggleGroup(funcao);

            this.stage.setTitle("Login de perfil");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LoginGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            Scene cena = new Scene(tela);
            this.stage.setScene(cena);
            this.stage.show();

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao exibir tela \nMotivo "+ e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void telaCadastro(){
        try {
            this.stage.setTitle("Cadastro Inicial");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PrimeiroCadastroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            Scene cena = new Scene(tela);
            this.stage.setScene(cena);
            this.stage.show();

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao carregar tela \nMotivo " + e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void CadastroInicial(ActionEvent e){

        try {
            verificação.adicionarUser(
                new User(
                    insNome.getText(), 
                    insMail.getText(), 
                    null
                )
            );

            new Login(this.stage).telaLogin();

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \nMotivo " +e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, cadastro cancelado",
                "Erro", 
                0
            ); 
        }
        
    }

    public void logging(ActionEvent e){

        try {
            RadioButton logFuncao = (RadioButton) funcao.getSelectedToggle();
    
            User temp = new User(
                insNome.getText(), 
                insMail.getText(), 
                Permissoes.valueOf(logFuncao.getText().toUpperCase())
            );
    
            this.usuarios.forEach(usr -> {
    
                if (temp.getNome().equals(usr.getNome()) 
                    && temp.getEmail().equals(usr.getEmail()) 
                    && temp.getFunção().getPermissaoNome().equals(usr.getFunção().name())
                ) {       
                    Sessao.setLoggado(usr);
    
                    new Main().entrar(stage);
                }
                
            });
    
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, login cancelado",
                "Erro", 
                0
            ); 
        }
    }

    public void telaClose(){
        usuarios.clear();
        this.stage.close();
    }

}
