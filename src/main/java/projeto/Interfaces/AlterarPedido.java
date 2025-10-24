package projeto.Interfaces;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import projeto.Sessao;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

public class AlterarPedido extends Dialog<Pedido>{

    @FXML
    private ComboBox<Pagamentos> altMetodos = new ComboBox<>();
    @FXML
    private ComboBox<User> altClientes = new ComboBox<>();
    @FXML
    private VBox altEncos = new VBox();
    @FXML
    private VBox estoLivs = new VBox();

    private Alert alert = new Alert(Alert.AlertType.ERROR);


    public AlterarPedido(Pedido insPedido, List<Livro> insLibs, List<User> insClients){

        alert.setTitle("Erro");
        alert.setHeaderText(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PedidoAlteracaoPane.fxml"));
            loader.setController(this);
            DialogPane tela = loader.load();

            this.setTitle("Alteração de Pedido");
            this.setDialogPane(tela);
            
            for (User client : insClients) {
                if (client.getFunção().equals(Permissoes.CLIENTE)) {
                    
                    altClientes.getItems().add(client);

                    altClientes.setCellFactory(lv -> new ListCell<>() {
                        @Override
                        protected void updateItem(User client, boolean empty) {
                            super.updateItem(client, empty);
                            setText(empty || client.getNome() == null ? null : client.getNome());
                        }
                    });

                }
            }

            altMetodos.setCellFactory(pg -> new ListCell<>() {
                @Override
                protected void updateItem(Pagamentos metodo, boolean empty) {
                    super.updateItem(metodo, empty);
                    if (empty || metodo == null) {
                        setText(null);
                    } else {
                        switch (metodo) {
                            case CREDITO -> setText("Cartão de Credito");
                            case DEBITO -> setText("Cartão de Debito");
                            case DINHEIRO -> setText("Dinheiro");
                            case PIX -> setText("PIX");
                        }
                    }
                }
            });
            altMetodos.getItems().addAll(Pagamentos.CREDITO, Pagamentos.DEBITO, Pagamentos.DINHEIRO, Pagamentos.PIX);
            
            ArrayList<Livro> encomendas = new ArrayList<>();

            insPedido.getEncomendas().forEach(livro ->{
                encomendas.add(livro);
            });
            
            for (Livro liv : insLibs) {
                if (liv.getQuantidade() > 0) {
                    Button livItem = new Button("Adicionar: " + liv.getTitulo());

                    livItem.setOnAction((adicio) ->{
                        encomendas.add(liv);                    
                    });

                    estoLivs.getChildren().add(livItem);
                }
            }

            for (Livro liv : encomendas) {
                Button livItem = new Button("Remover: " + liv.getTitulo());

                livItem.setOnAction((adicio) ->{
                    encomendas.remove(liv);
                    livItem.setStyle("-fx-background-color: #ff0000");
                });

                altEncos.getChildren().add(livItem);

            }

            ButtonType confirm = new ButtonType("Alterar", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            tela.getButtonTypes().addAll(confirm, cancelar);

            this.setResultConverter(dialogButton ->{
                try {
                    if(dialogButton == confirm && altClientes != null && altMetodos != null){
                        return new Pedido(
                            Sessao.getUser(), 
                            altClientes.getValue(), 
                            altMetodos.getValue(),
                            encomendas
                        );
                    }

                } catch (Exception e1) {
                    alert.setContentText("Erro por falta de dados, cadastro cancelado");
                    alert.showAndWait();
                    return null;
                }
                return null;
            });

        }catch(Exception e){
            alert.setContentText( e.getMessage() + ", alteraÇão cancelada");
            alert.showAndWait();
        }


    }

}
