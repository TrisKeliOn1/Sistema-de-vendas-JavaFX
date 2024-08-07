package com.crudmvc.Controllers;

import com.crudmvc.Models.dao.ClienteDAO;
import com.crudmvc.Models.dao.ProdutoDAO;
import com.crudmvc.Models.database.Database;
import com.crudmvc.Models.database.DatabaseFactory;
import com.crudmvc.Models.domain.Cliente;
import com.crudmvc.Models.domain.ItemDeVenda;
import com.crudmvc.Models.domain.Produto;
import com.crudmvc.Models.domain.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {

    @FXML
    private ComboBox comboBoxVendaCliente;

    @FXML
    private DatePicker datePickerVendaData;

    @FXML
    private CheckBox checkBoxVendaPago;

    @FXML
    private ComboBox comboBoxVendaProduto;

    @FXML
    private TableView<ItemDeVenda> tableViewItensDeVenda;

    @FXML
    private TableColumn<ItemDeVenda, Produto> tableColumnItemDeVendaProduto;

    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnItemDeVendaQuantidade;

    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnItemDeVendaValor;

    @FXML
    private TextField textFieldVendaValor;

    @FXML
    private TextField textFieldVendaItemDeVendaQuantidade;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonAdicionar;

    private List<Cliente> listClientes;
    private List<Produto> listProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarComboBoxClientes();
        carregarComboBoxProdutos();
        tableColumnItemDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    public void carregarComboBoxClientes() {
        listClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listClientes);
        comboBoxVendaCliente.setItems(observableListClientes);
    }

    public void carregarComboBoxProdutos() {
        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        comboBoxVendaProduto.setItems(observableListProdutos);
    }

    @FXML
    private void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        if (comboBoxVendaProduto.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem();
            if (produto.getQuantidade() >= Integer.parseInt(textFieldVendaItemDeVendaQuantidade.getText())) {
                itemDeVenda.setProduto((Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldVendaItemDeVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tableViewItensDeVenda.setItems(observableListItensDeVenda);
                textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque");
                alert.show();
            }
        }
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxVendaPago.isSelected());
            venda.setData(datePickerVendaData.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        getDialogStage().close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido! \n";
        }
        if (datePickerVendaData.getValue() == null) {
            errorMessage += "Data Inválida! \n";
        }
        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de Venda inválidos!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija..");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
