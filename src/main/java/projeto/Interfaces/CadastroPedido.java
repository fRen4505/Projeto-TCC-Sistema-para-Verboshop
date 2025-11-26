package projeto.Interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projeto.Sessao;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

//Classe tipo Dialog (tela pop-up) do JavaFX para inserção de dados usados no cadastro de um novo pedido,
//sendo utilizada dentro do AdminCTRL e UserCTRL
public class CadastroPedido extends Dialog<Pedido>{
    
    @FXML
    private VBox alLibs = new VBox();
    @FXML
    private ComboBox<Pagamentos> metodos = new ComboBox<>();
    @FXML
    private ComboBox<User> clientes = new ComboBox<>();
    
    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela, 
    //alem de listar livros e clientes cadastrados para seleção a partir de listas recebidas dos DAOs
    public CadastroPedido(List<Livro> insLibs, List<User> insClients){

        List<Livro> encomendas = new ArrayList<>();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PedidoCadastroPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            setTitle("Cadastro de novo Pedido");
            setDialogPane(tela);

            for (Livro liv : insLibs) {
                if (liv.getQuantidade() > 0) {

                    Label nome = new Label(liv.getTitulo() + " ");
                    Label qtnd = new Label("0");
                    Button add = new Button("+");
                    Button sub = new Button("-");

                    HBox buttons = new HBox();
                    buttons.setSpacing(10);
                    buttons.getChildren().addAll(add, qtnd, sub);

                    HBox livItem = new HBox();
                    livItem.setSpacing(15.5);
                    livItem.getChildren().addAll(nome , buttons);
                    
                    add.setOnAction((_) ->{
                        if (Integer.parseInt(qtnd.getText()) < liv.getQuantidade()) {
                            encomendas.add(liv);

                            qtnd.setText( (Integer.parseInt(qtnd.getText()) + 1) + "");
                        }
                    });

                    sub.setOnAction((_) ->{
                        if (Integer.parseInt(qtnd.getText()) > 0) {
                            encomendas.remove(liv);

                            qtnd.setText( (Integer.parseInt(qtnd.getText()) - 1) + "");
                        }
                    });

                    alLibs.getChildren().add(livItem);
                    
                }
            }

            for (User client : insClients) {
                if (client.getFunção().equals(Permissoes.CLIENTE)) {
                    
                    clientes.getItems().add(client);

                    clientes.setCellFactory(_ -> new ListCell<>() {
                        @Override
                        protected void updateItem(User client, boolean empty) {
                            super.updateItem(client, empty);
                            setText(empty || client.getNome().toString() == null ? null : client.getNome().toString());
                        }
                    });

                }
            }

            metodos.getItems().addAll(Pagamentos.values());

            metodos.setCellFactory(_ -> new ListCell<>() {
                @Override
                protected void updateItem(Pagamentos metodo, boolean empty) {
                    super.updateItem(metodo, empty);
                    setText(empty || metodo.name().toString() == null ? null : metodo.name().toString());

                }
            });
            
            ButtonType confirm = new ButtonType("Cadastrar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            setResultConverter(dialogButton ->{
                if(dialogButton == confirm && !encomendas.isEmpty()){
                    try {
                        return new Pedido(
                            Sessao.getUser(), 
                            clientes.getValue(), 
                            metodos.getValue(),
                            encomendas
                        );
                    } catch (NullPointerException e1) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "Cadastro cancelado por falta de dados: " + e1.getMessage(),
                            "Erro", 
                            0
                        );   
                        return null;
                    }
                }else{
                    return null;
                }
            });

        } catch (IOException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por "+e2.getMessage() + ", cadastro cancelado",
                "Erro", 
                0
            );          
        }

    }

}
