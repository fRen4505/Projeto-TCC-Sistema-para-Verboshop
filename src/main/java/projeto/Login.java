package projeto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import projeto.System.InitDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class Login {

    private Stage stage;
    private static InitDAO verificação = InitDAO.getInstancia(); 

    private User usuarioAtual;

    private List<User> usuarios;

    @FXML
    private TextField insNome;
    @FXML
    private TextField insMail;
    @FXML
    private Button logButton, telaLogin;
    @FXML
    private ToggleGroup funcao;
    @FXML
    private RadioButton adminButton, userButton;
    
    public User getUsuarioAtual() {
        return usuarioAtual;
    }

    public Login(){}

    public Stage tela(Stage inStage){
                
        try {
            this.stage = inStage;
            this.usuarios = verificação.getUsers();
            
            if (this.usuarios.isEmpty() == true) {
                this.stage.setTitle("Cadastro Inicial");
                Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/PrimeiroCadastroGUI.fxml"));
                Scene cena = new Scene(tela);
                this.stage.setScene(cena);

            }else{
                this.stage.setTitle("Login");
                Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/LoginGUI.fxml"));
                Scene cena = new Scene(tela);
                this.stage.setScene(cena);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.stage;
    }

    public void logging(ActionEvent e) throws IOException{

        try {
            usuarios = verificação.getUsers();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        String logName = insNome.getText();
        String logMail = insMail.getText();
        String logFuncao = funcao.getSelectedToggle().toString();

        Parent root;
        FXMLLoader loader;

        if (logButton.isArmed()) {
        
            for (User usr : this.usuarios) {

                if (usr.getNome().equals(logName) && usr.getEmail().equals(logMail) ) {
    
                    if (adminButton.isSelected() && usr.getFunção() == Permissoes.ADMINISTRADOR ) {
    
                        this.usuarioAtual = usr;
    
                        //System.out.println("True, usr: " + usr.getNome() + " " + usr.getID() + " " + usr.getFunção().getPermissaoNome());
    
                        loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
                        root = loader.load();
                        
                        AdminCTRL adminLog = loader.getController();
                        Stage currStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                        
                        adminLog.tela(this.usuarioAtual, currStage);
    
                        //Parent tela = root;
                        Scene cena = new Scene(root);
                        currStage.setScene(cena);
                        currStage.show();
                        //this.stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                       // this.stage.setScene(cena);
                       // this.stage.show();
    
                        break;
                    }
                    if (userButton.isSelected() && usr.getFunção() == Permissoes.USUARIO ) {
    
                        this.usuarioAtual = usr;
    
                        //System.out.println("True, usr: " + usr.getNome() + " " + usr.getID() + " " + usr.getFunção().getPermissaoNome());
                        
                        loader = new FXMLLoader(getClass().getResource("/GUIs/UserGUI.fxml"));
                        root = loader.load();
                        
                        UserCTRL userLog = loader.getController();
                        userLog.tela(this.usuarioAtual, this.stage);
    
                        Parent tela = root;
                        Scene cena = new Scene(tela);
                        this.stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                        this.stage.setScene(cena);
                        this.stage.show();
    
    
                        break;
                    }
    
                }else{
                    System.out.println("false");
                    System.out.println(logFuncao);
                }
    
            }
    
        }

        /*for (User usr : this.usuarios) {

            if (usr.getNome().equals(logName) && usr.getEmail().equals(logMail) ) {

                if (adminButton.isSelected() && usr.getFunção() == Permissoes.ADMINISTRADOR ) {

                    this.usuarioAtual = usr;

                    System.out.println("True, usr: " + usr.getNome() + " " + usr.getID() + " " + usr.getFunção().getPermissaoNome());

                    loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
                    root = loader.load();
                    
                    AdminCTRL adminLog = loader.getController();
                    adminLog.tela(this.usuarioAtual, this.stage);

                    Parent tela = root;
                    Scene cena = new Scene(tela);
                    this.stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    this.stage.setScene(cena);
                    this.stage.show();

                    break;
                }
                if (userButton.isSelected() && usr.getFunção() == Permissoes.USUARIO ) {

                    this.usuarioAtual = usr;

                    System.out.println("True, usr: " + usr.getNome() + " " + usr.getID() + " " + usr.getFunção().getPermissaoNome());
                    
                    loader = new FXMLLoader(getClass().getResource("/GUIs/UserGUI.fxml"));
                    root = loader.load();
                    
                    UserCTRL userLog = loader.getController();
                    userLog.tela(this.usuarioAtual, this.stage);

                    Parent tela = root;
                    Scene cena = new Scene(tela);
                    this.stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    this.stage.setScene(cena);
                    this.stage.show();


                    break;
                }

            }else{
                System.out.println("false");
                System.out.println(logFuncao);
            }

        }

        */ //this.telaClose();

    }

    public void CadastradoInicial(ActionEvent e){

        System.out.println(">>Cadastro de primeiro usuario<<");

        String username = insNome.getText();
        String email = insMail.getText();
       
        User primeiroADM = new User(username, email, null);

        try {
            verificação.adicionarUser(primeiroADM);
            System.out.println("usuario novo: " + primeiroADM.getNome());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        

    }

    public void mudarTela(ActionEvent e){
        try {
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/LoginGUI.fxml"));
            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void telaClose(){
        usuarios.clear();
        //this.stage = null;
        //this.stage.close();
    }
}
