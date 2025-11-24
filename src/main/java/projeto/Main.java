package projeto;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
    
    private final Logger log = LoggerFactory.getLogger(Main.class);

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
                    log.info("administrador: " + Sessao.getUser().getNome() + " loggou");
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
                    log.info("usuario: " + Sessao.getUser().getNome() + " loggou");
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

        stage.setOnCloseRequest( new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                String[] vals = {"sim", "não"};
                String opt = (String)JOptionPane.showInputDialog(null, 
                    "Deseja mesmo sair?",
                    "Sair", 
                    2, 
                    null, 
                    vals,vals[1]
                );
                if (opt.equals("sim")) {
                    log.error("Sistema fechando");
                }
            }
            
        });
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}