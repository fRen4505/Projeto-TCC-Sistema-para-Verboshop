package projeto.Interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CadastroInicial {
    
    private Stage stage = new Stage();

    public Stage getStage() {
        return stage;
    }

    public CadastroInicial(){

        this.stage = new Stage();

        try {
            this.stage.setTitle("Cadastro Inicial");
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/PrimeiroCadastroGUI.fxml"));
            Scene cena = new Scene(tela);
            this.stage.setScene(cena);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
