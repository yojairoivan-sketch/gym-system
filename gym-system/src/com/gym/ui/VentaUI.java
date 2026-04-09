package com.gym.ui;

import com.gym.model.Producto;
import com.gym.model.Venta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.LocalTime;

public class VentaUI {

    // ── Colores del tema oscuro ──────────────────────────────
    private static final String COLOR_FONDO     = "#1a1a2e"; // fondo general
    private static final String COLOR_PANEL     = "#16213e"; // fondo de paneles
    private static final String COLOR_CAMPO     = "#0f3460"; // fondo de inputs
    private static final String COLOR_BOTON     = "#7c3aed"; // color botón principal
    private static final String COLOR_TEXTO     = "#ffffff"; // texto blanco
    private static final String COLOR_SUBTITULO = "#a78bfa"; // morado claro para títulos
    private static final String COLOR_ALERTA    = "#ef4444"; // rojo para alertas

    // Lista de productos disponibles para vender (viene de ProductoUI)
    private ObservableList<Producto> listaProductos;

    // Lista observable de ventas registradas
    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();

    // Tabla que muestra el historial de ventas
    private TableView<Venta> tabla = new TableView<>();

    // Campos del formulario de venta
    private ComboBox<Producto> cmbProducto  = new ComboBox<>(); // selector de producto
    private TextField txtCantidad           = new TextField();  // cantidad a vender
    private ComboBox<String> cmbMetodoPago  = new ComboBox<>(); // método de pago
    private Label lblTotal                  = new Label("$0.00"); // total calculado
    private Label lblStockDisponible        = new Label("Stock disponible: -"); // stock actual

    // Constructor que recibe la lista de productos de ProductoUI
    public VentaUI(ObservableList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    // Método principal que devuelve toda la vista de Ventas
    public VBox getVista() {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        root.getChildren().addAll(
            crearFormulario(),
            crearTabla()
        );

        return root;
    }

    // Crea el panel superior con el formulario de venta
    private VBox crearFormulario() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        // Título del formulario
        Label titulo = new Label("REGISTRAR VENTA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // ── Selector de producto ──
        // Conecta la lista de productos al ComboBox
        cmbProducto.setItems(listaProductos);
        cmbProducto.setMaxWidth(Double.MAX_VALUE);
        cmbProducto.setStyle(estiloInput());
        cmbProducto.setPromptText("Seleccionar producto...");

        // Muestra el nombre del producto en el ComboBox
        cmbProducto.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Producto p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNombre() + " — $" + p.getPrecio());
            }
        });
        cmbProducto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Producto p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNombre() + " — $" + p.getPrecio());
            }
        });

        // Al seleccionar un producto actualiza el stock disponible y el total
        cmbProducto.setOnAction(e -> actualizarInfo());

        // Estilo del label de stock disponible
        lblStockDisponible.setTextFill(Color.web(COLOR_TEXTO));

        VBox productoBox = new VBox(4);
        Label lblProducto = new Label("Producto");
        lblProducto.setTextFill(Color.web(COLOR_TEXTO));
        productoBox.getChildren().addAll(lblProducto, cmbProducto, lblStockDisponible);

        // ── Fila: Cantidad y Método de pago ──
        HBox fila = new HBox(15);
        txtCantidad.setPromptText("1");
        // Al cambiar la cantidad recalcula el total
        txtCantidad.textProperty().addListener((obs, old, nuevo) -> calcularTotal());

        cmbMetodoPago.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
        cmbMetodoPago.setValue("Efectivo");
        cmbMetodoPago.setStyle(estiloInput());
        cmbMetodoPago.setMaxWidth(Double.MAX_VALUE);

        fila.getChildren().addAll(
            crearCampo("Cantidad", txtCantidad),
            crearCampoCombo("Método de pago", cmbMetodoPago)
        );

        // ── Total calculado automáticamente ──
        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_LEFT);
        Label lblTotalTexto = new Label("Total:");
        lblTotalTexto.setTextFill(Color.web(COLOR_TEXTO));
        lblTotalTexto.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTotal.setTextFill(Color.web(COLOR_BOTON)); // morado para destacar el total
        totalBox.getChildren().addAll(lblTotalTexto, lblTotal);

        // ── Botón registrar venta ──
        Button btnVender = new Button("Registrar Venta");
        btnVender.setMaxWidth(Double.MAX_VALUE);
        btnVender.setStyle(
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;"
        );
        // Al hacer click procesa la venta
        btnVender.setOnAction(e -> registrarVenta());

        panel.getChildren().addAll(titulo, productoBox, fila, totalBox, btnVender);
        return panel;
    }

    // Crea el panel inferior con el historial de ventas
    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("HISTORIAL DE VENTAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // ── Columnas de la tabla ──
        // Columna de producto — muestra el nombre del producto vendido
        TableColumn<Venta, Void> colProducto = new TableColumn<>("PRODUCTO");
        colProducto.setPrefWidth(150);
        colProducto.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Venta v = getTableView().getItems().get(getIndex());
                    setText(v.getProducto().getNombre());
                }
            }
        });

        TableColumn<Venta, Integer> colCantidad = new TableColumn<>("CANTIDAD");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setPrefWidth(90);

        TableColumn<Venta, Double> colTotal = new TableColumn<>("TOTAL");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setPrefWidth(100);

        TableColumn<Venta, String> colMetodo = new TableColumn<>("MÉTODO");
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        colMetodo.setPrefWidth(110);

        TableColumn<Venta, LocalDate> colFecha = new TableColumn<>("FECHA");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setPrefWidth(110);

        tabla.getColumns().addAll(colProducto, colCantidad, colTotal, colMetodo, colFecha);
        tabla.setItems(listaVentas); // conecta la lista a la tabla
        tabla.setStyle("-fx-background-color: " + COLOR_CAMPO + ";");
        tabla.setPrefHeight(280);

        panel.getChildren().addAll(titulo, tabla);
        return panel;
    }

    // Actualiza el stock disponible y el total cuando se selecciona un producto
    private void actualizarInfo() {
        Producto p = cmbProducto.getValue();
        if (p != null) {
            // Muestra el stock disponible con alerta si está bajo
            if (p.stockBajo()) {
                lblStockDisponible.setText("⚠ Stock bajo: " + p.getStock() + " unidades");
                lblStockDisponible.setTextFill(Color.web(COLOR_ALERTA));
            } else {
                lblStockDisponible.setText("Stock disponible: " + p.getStock() + " unidades");
                lblStockDisponible.setTextFill(Color.web("#22c55e")); // verde
            }
            calcularTotal();
        }
    }

    // Calcula el total multiplicando precio por cantidad
    private void calcularTotal() {
        Producto p = cmbProducto.getValue();
        if (p == null) return;
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double total = cantidad * p.getPrecio();
            lblTotal.setText("$" + String.format("%.2f", total));
        } catch (NumberFormatException e) {
            lblTotal.setText("$0.00"); // si la cantidad no es válida muestra 0
        }
    }

    // Lógica para registrar una venta
    private void registrarVenta() {
        Producto producto = cmbProducto.getValue();
        String cantidadTexto = txtCantidad.getText().trim();
        String metodoPago = cmbMetodoPago.getValue();

        // Validación: debe seleccionar un producto
        if (producto == null) {
            mostrarAlerta("Selecciona un producto para continuar.");
            return;
        }

        // Validación: la cantidad debe ser un número válido
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta("La cantidad debe ser un número mayor a 0.");
            return;
        }

        // Validación: verifica si hay suficiente stock
        if (!producto.hayStock(cantidad)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock insuficiente");
            alert.setContentText("Solo hay " + producto.getStock() + " unidades disponibles.");
            alert.showAndWait();
            return;
        }

        // Descuenta el stock del producto
        producto.descontarStock(cantidad);

        // Crea la venta con fecha y hora actual
        Venta venta = new Venta(
            LocalDate.now(),
            LocalTime.now(),
            cantidad,
            metodoPago,
            producto
        );

        listaVentas.add(venta); // agrega la venta al historial

        // Si el stock quedó bajo del mínimo muestra alerta visual
        if (producto.stockBajo()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("⚠ Stock bajo");
            alert.setContentText("El producto '" + producto.getNombre() +
                "' tiene stock bajo: " + producto.getStock() + " unidades restantes.");
            alert.showAndWait();
        }

        limpiar();
    }

    // Muestra un mensaje de advertencia
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Crea un campo de texto con su etiqueta
    private VBox crearCampo(String etiqueta, TextField campo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        campo.setStyle(estiloInput());
        campo.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, campo);
        return box;
    }

    // Crea un campo con etiqueta y ComboBox
    private VBox crearCampoCombo(String etiqueta, ComboBox<String> combo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        box.getChildren().addAll(lbl, combo);
        return box;
    }

    // Estilo CSS reutilizable para inputs
    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + ";" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #6b7280;" +
               "-fx-background-radius: 6;" +
               "-fx-padding: 8;";
    }

    // Limpia el formulario después de registrar una venta
    private void limpiar() {
        cmbProducto.setValue(null);
        txtCantidad.clear();
        cmbMetodoPago.setValue("Efectivo");
        lblTotal.setText("$0.00");
        lblStockDisponible.setText("Stock disponible: -");
        lblStockDisponible.setTextFill(Color.web(COLOR_TEXTO));
    }
}