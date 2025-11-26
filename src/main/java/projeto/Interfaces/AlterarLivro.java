package projeto.Interfaces;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import projeto.System.Models.Livro;

//Classe tipo Dialog (tela pop-up) do JavaFX para inserção de dados usados na alteração de um cadastro de livro,
//sendo utilizada dentro do LivroPane
public class AlterarLivro extends Dialog<Livro>{
    
    @FXML
    private Label qtnd;
    @FXML
    private TextField altTitulo, altAutor, altEdit, altPreco ;
    @FXML
    private Button add, sub;

    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela, completando os dados com o informações do livro
    public AlterarLivro(Livro ins){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroAlteracaoPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Alteração de dados de Livro");
            setDialogPane(tela);

            altTitulo.setText(ins.getTitulo());
            altAutor.setText(ins.getAutor());
            altEdit.setText(ins.getEditora());
            altPreco.setText(ins.getPreço().valor());
            qtnd.setText(ins.getQuantidade() + "");

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {
                    return new Livro(
                        altTitulo.getText(),
                        altAutor.getText(), 
                        altEdit.getText(), 
                        Double.parseDouble(altPreco.getText().replace(',', '.').substring(altPreco.getText().indexOf(" ") + 1)), 
                        Integer.parseInt(qtnd.getText())
                    );
                } else {
                    return ins;
                }
            });

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por "+e1.getMessage() + ", alteração cancelada",
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para aumentar valor de label que é usado para alterar quantidade do livro no estoque, 
    //acionado ao pressionar botão + (add) desta classe
    public void addAlt(ActionEvent a){
        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "");
    }

    //Metodo para diminuir valor de label que é usado para alterar quantidade do livro no estoque, 
    //acionado ao pressionar botão - (sub) desta classe
    public void subAlt(ActionEvent s){
        if (Integer.parseInt(qtnd.getText()) > 0) {
            if ( (Integer.parseInt(qtnd.getText()) - 1) < 0 ) {
                qtnd.setText("0");
            } else {
                qtnd.setText( 
                    (    (Integer.parseInt(qtnd.getText()) - 1)    )+""
                );
            }
        }
    }

}
