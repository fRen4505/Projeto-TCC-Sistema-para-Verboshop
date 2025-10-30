package projeto;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    

    @Override
    public void start(Stage stage) {

        if (Sessao.getUser() == null) {
            new Login(stage);
        }else{
            this.entrar(stage);
        }
    }

    public void entrar(Stage stage){
        
        Parent root;
        FXMLLoader loader;
        Scene cena;

        switch (Sessao.getUser().getFunção()) {

            case ADMINISTRADOR:
                try {
                    AdminCTRL adminLog = new AdminCTRL(Sessao.getUser(), stage);

                    loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
                    loader.setController(adminLog);
                    root = loader.load();
                                                
                    cena = new Scene(root);
                    stage.setScene(cena);
                    stage.show();

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Erro ao carregar tela \nMotivo " + e.getMessage(),
                        "Erro", 
                        0
                    );            
                }
                break;

            case USUARIO:
                try {
                    UserCTRL userLog = new UserCTRL(Sessao.getUser(), stage);
                            
                    loader = new FXMLLoader(getClass().getResource("/GUIs/UserGUI.fxml"));
                    loader.setController(userLog);
                    root = loader.load();

                    cena = new Scene(root);
                    stage.setScene(cena);
                    stage.show();

                }catch (IOException e) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Erro ao carregar tela \nMotivo " + e.getMessage(),
                        "Erro", 
                        0
                    );
                }
                break;
            default:
            
                break;
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}