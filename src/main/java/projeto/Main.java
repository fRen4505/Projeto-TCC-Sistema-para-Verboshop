package projeto;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    
    //private static InitDAO verificação = InitDAO.getInstancia(); 
    private Stage telaMain = new Stage();

    @Override
    public void start(Stage stage) {

        Login login = new Login();

        telaMain = login.tela(stage); ;

        telaMain.show();

    }

    public static void main(String[] args) {
        
        launch(args);

    }
}