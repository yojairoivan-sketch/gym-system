package com.gym.ui;

import com.gym.model.Producto;
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

public class ProductoUI {

    // ── Colores del tema oscuro ──────────────────────────────
    private static final String COLOR_FONDO     = "#1a1a2e"; // fondo general
    private static final String COLOR_PANEL     = "#16213e"; // fondo de paneles
    private static final String COLOR_CAMPO     = "#0f3460"; // fondo de inputs
    private static final String COLOR_BOTON     = "#7c3aed"; // color botón principal
    private static final String COLOR_TEXTO     = "#ffffff"; // texto blanco
    private static final String COLOR_SUBTITULO = "#a78bfa"; // morado claro para títulos
    private static final String COLOR_ALERTA    = "#ef4444"; // rojo para stock bajo

    // Lista observable de productos — la tabla se actualiza automáticamente
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // Tabla que muestra los productos registrados
    private TableView<Producto> tabla = new TableView<>();

    // Campos del formulario
    private TextField txtNombre      = new TextField();
    private TextField txtDescripcion = new TextField();
    private ComboBox<String> cmbCategoria = new ComboBox<>();
    private TextField txtPrecio      = new TextField();
    private TextField txtStock       = new TextField();
    private TextField txtStockMinimo = new TextField();

    // Referencia al producto que se está editando (null si es nuevo)
    private Producto productoEditando = null;

    // Método principal que devuelve toda la vista de Productos
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

    // Devuelve la lista de productos para usarla en VentaUI
    public ObservableList<Producto> getListaProductos() {
        return listaProductos;
    }

    // Crea el panel superior con el formulario de registro
    private VBox crearFormulario() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        // Título del formulario
        Label titulo = new Label("REGISTRO DE PRODUCTO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // ── Fila 1: Nombre y Categoría ──
        HBox fila1 = new HBox(15);
        txtNombre.setPromptText("Nombre del producto");
        cmbCategoria.getItems().addAll("Suplemento", "Bebida", "Accesorio", "Ropa", "Otro");
        cmbCategoria.setValue("Suplemento");
        cmbCategoria.setStyle(estiloInput());
        cmbCategoria.setMaxWidth(Double.MAX_VALUE);
        fila1.getChildren().addAll(
            crearCampo("Nombre", txtNombre),
            crearCampoCombo("Categoría", cmbCategoria)
        );

        // ── Fila 2: Descripción ──
        HBox fila2 = new HBox(15);
        txtDescripcion.setPromptText("Descripción del producto");
        fila2.getChildren().add(crearCampo("Descripción", txtDescripcion));

        // ── Fila 3: Precio, Stock y Stock Mínimo ──
        HBox fila3 = new HBox(15);
        txtPrecio.setPromptText("0.00");
        txtStock.setPromptText("0");
        txtStockMinimo.setPromptText("5");
        fila3.getChildren().addAll(
            crearCampo("Precio ($)", txtPrecio),
            crearCampo("Stock", txtStock),
            crearCampo("Stock Mínimo", txtStockMinimo)
        );

        // ── Botones Guardar y Limpiar ──
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle(
            "-fx-background-color: #374151;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );
        // Al hacer click limpia el formulario
        btnLimpiar.setOnAction(e -> limpiar());

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle(
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );
        // Al hacer click guarda o edita el producto
        btnGuardar.setOnAction(e -> guardarProducto());

        botones.getChildren().addAll(btnLimpiar, btnGuardar);
        panel.getChildren().addAll(titulo, fila1, fila2, fila3, botones);
        return panel;
    }

    // Crea el panel inferior con la tabla de productos
    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("LISTADO DE PRODUCTOS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // ── Columnas de la tabla ──
        TableColumn<Producto, String> colNombre      = columna("NOMBRE", "nombre", 150);
        TableColumn<Producto, String> colCategoria   = columna("CATEGORÍA", "categoria", 110);
        TableColumn<Producto, Double> colPrecio      = new TableColumn<>("PRECIO");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(90);

        // Columna de stock con alerta visual en rojo si está bajo
        TableColumn<Producto, Integer> colStock = new TableColumn<>("STOCK");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(80);
        colStock.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                if (empty || stock == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(stock.toString());
                    Producto p = getTableView().getItems().get(getIndex());
                    // Si el stock está bajo del mínimo, muestra en rojo
                    if (p.stockBajo()) {
                        setStyle("-fx-text-fill: " + COLOR_ALERTA + "; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
                }
            }
        });

        // Columna de alerta visual cuando el stock es bajo
        TableColumn<Producto, Void> colAlerta = new TableColumn<>("ALERTA");
        colAlerta.setPrefWidth(90);
        colAlerta.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Producto p = getTableView().getItems().get(getIndex());
                    // Muestra ⚠ si el stock está bajo del mínimo
                    if (p.stockBajo()) {
                        setText("⚠ Stock bajo");
                        setStyle("-fx-text-fill: " + COLOR_ALERTA + "; -fx-font-weight: bold;");
                    } else {
                        setText("✓ OK");
                        setStyle("-fx-text-fill: #22c55e;");
                    }
                }
            }
        });

        // Columna con botón Editar
        TableColumn<Producto, Void> colEditar = new TableColumn<>("ACCIÓN");
        colEditar.setPrefWidth(90);
        colEditar.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Editar");
            {
                btn.setStyle(
                    "-fx-background-color: #374151;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 4 10;"
                );
                // Al hacer click carga el producto en el formulario para editarlo
                btn.setOnAction(e -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    cargarProductoEnFormulario(p);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tabla.getColumns().addAll(colNombre, colCategoria, colPrecio, colStock, colAlerta, colEditar);
        tabla.setItems(listaProductos); // conecta la lista a la tabla
        tabla.setStyle("-fx-background-color: " + COLOR_CAMPO + ";");
        tabla.setPrefHeight(280);

        panel.getChildren().addAll(titulo, tabla);
        return panel;
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

    // Crea una columna de tabla genérica
    private <T> TableColumn<Producto, T> columna(String titulo, String propiedad, double ancho) {
        TableColumn<Producto, T> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        return col;
    }

    // Estilo CSS reutilizable para inputs
    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + ";" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #6b7280;" +
               "-fx-background-radius: 6;" +
               "-fx-padding: 8;";
    }

    // Lógica para guardar o editar un producto
    private void guardarProducto() {
        String nombre      = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String categoria   = cmbCategoria.getValue();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto  = txtStock.getText().trim();
        String stockMinTexto = txtStockMinimo.getText().trim();

        // Validación de campos obligatorios
        if (nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obligatorios");
            alert.setContentText("Nombre, precio y stock son obligatorios.");
            alert.showAndWait();
            return;
        }

        // Convierte los valores numéricos con manejo de error
        double precio;
        int stock, stockMinimo;
        try {
            precio     = Double.parseDouble(precioTexto);
            stock      = Integer.parseInt(stockTexto);
            stockMinimo = Integer.parseInt(stockMinTexto.isEmpty() ? "5" : stockMinTexto);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Valor inválido");
            alert.setContentText("Precio y stock deben ser números válidos.");
            alert.showAndWait();
            return;
        }

        if (productoEditando != null) {
            // Si está editando, actualiza el producto existente
            productoEditando.setNombre(nombre);
            productoEditando.setDescripcion(descripcion);
            productoEditando.setCategoria(categoria);
            productoEditando.setPrecio(precio);
            productoEditando.setStock(stock);
            productoEditando.setStockMinimo(stockMinimo);
            tabla.refresh(); // refresca la tabla para mostrar los cambios
            productoEditando = null;
        } else {
            // Si es nuevo, crea el objeto y lo agrega a la lista
            Producto p = new Producto(nombre, descripcion, categoria, precio, stock, stockMinimo);
            listaProductos.add(p);
        }
        limpiar();
    }

    // Carga un producto existente en el formulario para editarlo
    private void cargarProductoEnFormulario(Producto p) {
        productoEditando = p;
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        cmbCategoria.setValue(p.getCategoria());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
    }

    // Limpia todos los campos del formulario
    private void limpiar() {
        txtNombre.clear();
        txtDescripcion.clear();
        cmbCategoria.setValue("Suplemento");
        txtPrecio.clear();
        txtStock.clear();
        txtStockMinimo.clear();
        productoEditando = null;
    }
}