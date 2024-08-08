package com.crudmvc.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLVBoxMainController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private MenuItem menuItemCadastrosCliente;
    @FXML
    private MenuItem menuItemProcessosVendas;
    @FXML
    private MenuItem menuItemGraficosVendasPorMes;
    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutosEstoque;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void handleMenuItemCadastrosClientes() throws IOException {
        AnchorPane clientRegistrationPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/FXMLAnchorPaneCadastroClientes.fxml"));
        anchorPane.getChildren().setAll(clientRegistrationPane);
    }

    @FXML
    public void handleMenuItemProcessosVendas() throws IOException {
        AnchorPane clientRegistrationPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/FXMLAnchorPaneProcessosVendas.fxml"));
        anchorPane.getChildren().setAll(clientRegistrationPane);
    }

    @FXML
    public void handleMenuItemGraficosVendasPorMes() throws IOException {
        AnchorPane clientRegistrationPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/FXMLAnchorPaneGraficosVendasPorMes.fxml"));
        anchorPane.getChildren().setAll(clientRegistrationPane);
    }

    @FXML
    public void handleMenuItemRelatoriosQuantidadeProdutos() throws IOException {
        AnchorPane clientRegistrationPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/FXML/FXMLAnchorPaneRelatoriosQuantidadeProdutos.fxml"));
        anchorPane.getChildren().setAll(clientRegistrationPane);
    }
}
