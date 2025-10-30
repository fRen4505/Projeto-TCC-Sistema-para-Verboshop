package projeto.Interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projeto.System.Models.Livro;

public class NovoLotePane extends Dialog<ArrayList<Livro>>{
    
    @FXML
    private VBox loteLivs;

    public NovoLotePane(List<Livro> insLibs){

        HashMap<String, Integer> insLote = new HashMap<>();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LoteCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Novo lote");
            setDialogPane(tela);

            for (Livro livro : insLibs) {

                Label nome = new Label(livro.getTitulo());
                Label qtnd = new Label("0");
                Button add = new Button("+");
                Button sub = new Button("-");

                HBox buttons = new HBox();
                buttons.setSpacing(10);
                buttons.getChildren().addAll(add, qtnd, sub);

                HBox info = new HBox();
                info.setSpacing(15.5);
                info.getChildren().addAll(nome , buttons);
                
                String val = livro.getISBN().valorISBN();

                add.setOnAction((_) ->{
                    if (insLote.containsKey(val)) {
                        insLote.put(val, insLote.get(val) + 1);
                        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "" );
                    }else{
                        insLote.putIfAbsent(val, 1);
                        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "" );
                    }
                });

                sub.setOnAction((_) ->{
                    if (Integer.parseInt(qtnd.getText()) > 0) {
                        
                        qtnd.setText(String.valueOf( (Integer.parseInt(qtnd.getText()) - 1) ));
                        insLote.put(val, Integer.parseInt(qtnd.getText()));

                        if (Integer.parseInt(qtnd.getText()) == 0) {
                            insLote.remove(val);
                        }
                    }
                });

                loteLivs.getChildren().add(info);
            }

            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            setResultConverter(dialogButton ->{
                if(dialogButton == confirm){

                    ArrayList<Livro> lote = new ArrayList<Livro>();

                    for (Livro livro : insLibs) {
                        insLote.forEach( (isbn, qntd) -> {
                            
                            if(livro.getISBN().valorISBN().equals(isbn)){
                                lote.add(new Livro(
                                    livro.getTitulo(), 
                                    livro.getAutor(), 
                                    livro.getEditora(), 
                                    livro.getPreço().getQuantiaDouble(), 
                                    (livro.getQuantidade() + qntd), 
                                    livro.getISBN().valorISBN()
                                ));
                            }
                        });
                    }
                    return lote;
                }else{
                    return null;
                }        
            });

        }catch (NullPointerException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, dados vazios e não cadastrados, cancelado \n motivo "+e1.getMessage(),
                "erro", 
                0
            );      

        } catch (IOException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, por input incorreta \n motivo, cancelado"+e2.getMessage(),
                "erro", 
                0
            );            
        }

    }

}
