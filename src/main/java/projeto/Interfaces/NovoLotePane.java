package projeto.Interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public NovoLotePane(List<Livro> insLibs){

        alert.setTitle("Erro");
        alert.setHeaderText(null);

        HashMap<String, Integer> insLote = new HashMap<>();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LoteCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            this.setTitle("Novo lote");
            this.setDialogPane(tela);

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

                add.setOnAction((adicio) ->{
                    if (insLote.containsKey(val)) {
                        insLote.put(val, insLote.get(val) + 1);
                        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "" );
                    }else{
                        insLote.putIfAbsent(val, 1);
                        qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "" );
                    }
                });

                sub.setOnAction((subtra) ->{
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

            this.setResultConverter(dialogButton ->{
                try {
                    if(dialogButton == confirm){

                        ArrayList<Livro> lote = new ArrayList<Livro>();

                        insLote.forEach( (isbn, qntd) -> {
                            for (Livro livro : insLibs) {
                                if(livro.getISBN().valorISBN().equals(isbn)){
                                    lote.add(new Livro(
                                        livro.getTitulo(), 
                                        livro.getAutor(), 
                                        livro.getEditora(), 
                                        livro.getPreço().doubleValue(), 
                                        (livro.getQuantidade() + qntd), 
                                        livro.getISBN().valorISBN()
                                    ));
                                }
                            }
                        });
                        return lote;
                    }
                } catch (NullPointerException e1) {
                    alert.setContentText("Erro de referência nula: " + e1.getMessage());
                    alert.showAndWait();
                    return null;
                }
                return null;
            });


        } catch (IOException e2) {
            alert.setContentText("Erro ao carregar layout FXML: " + e2.getMessage());
            alert.showAndWait();        
        }

    }

}
