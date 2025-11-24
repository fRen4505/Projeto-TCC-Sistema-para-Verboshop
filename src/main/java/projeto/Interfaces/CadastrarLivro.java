package projeto.Interfaces;

import java.io.IOException;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.Livro;

public class CadastrarLivro extends Dialog<Livro>{
    
    @FXML
    private TextField insTitulo, insAutor, insEdit, insPreco, insCode, insQtnd;
    
    public CadastrarLivro(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Cadastro de novo Livro");
            setDialogPane(tela);

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {
                    return new Livro(
                        insTitulo.getText(),
                        insAutor.getText(), 
                        insEdit.getText(), 
                        Double.parseDouble(insPreco.getText().replace(',', '.')), 
                        Integer.parseInt(insQtnd.getText()),
                        insCode.getText()
                    );
                }else{
                    return null;
                }
            });

        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por "+e1.getMessage() + ", cadastro cancelado",
                "Erro", 
                0
            );       
        }catch(NumberFormatException e2){
            JOptionPane.showMessageDialog(
                null, 
                "Erro por valor errado ou vazio, cadastro cancelado \n motivo: "+e2.getMessage(),
                "Erro", 
                0
            );  
        }

    }

}
