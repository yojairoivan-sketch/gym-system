package com.gym.ui;

import com.gym.model.Producto;
import com.gym.dao.ProductoDAO; // Importamos el DAO
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

    //Colores del tema oscuro
    private static final String COLOR_FONDO     = "#1a1a2e";
    private static final String COLOR_PANEL     = "#16213e";
    private static final String COLOR_CAMPO     = "#0f3460";
    private static final String COLOR_BOTON     = "#7c3aed";
    private static final String COLOR_TEXTO     = "#ffffff";
    private static final String COLOR_SUBTITULO = "#a78bfa";
    private static final String COLOR_ALERTA    = "#ef4444";

    // --- CONEXIÓN CON BASE DE DATOS ---
    private ProductoDAO productoDAO = new ProductoDAO();
    // ----------------------------------

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private TableView<Producto> tabla = new TableView<>();

    private TextField txtNombre      = new TextField();
    private TextField txtDescripcion = new TextField();
    private ComboBox<String> cmbCategoria = new ComboBox<>();
    private TextField txtPrecio      = new TextField();
    private TextField txtStock       = new TextField();
    private TextField txtStockMinimo = new TextField();

    private Producto productoEditando = null;

    public VBox getVista() {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        // --- CARGAR DATOS AL INICIAR ---
        cargarDatosDesdeBD();
        // -------------------------------

        root.getChildren().addAll(
            crearFormulario(),
            crearTabla()
        );

        return root;
    }

    private void cargarDatosDesdeBD() {
        try {
            listaProductos.setAll(productoDAO.listarTodos());
        } catch (Exception e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
    }

    public ObservableList<Producto> getListaProductos() {
        return listaProductos;
    }

    private VBox crearFormulario() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("REGISTRO DE PRODUCTO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        HBox fila1 = new HBox(15);
        cmbCategoria.getItems().addAll("Suplemento", "Bebida", "Accesorio", "Ropa", "Otro");
        cmbCategoria.setValue("Suplemento");
        cmbCategoria.setStyle(estiloInput());
        cmbCategoria.setMaxWidth(Double.MAX_VALUE);
        fila1.getChildren().addAll(
            crearCampo("Nombre", txtNombre),
            crearCampoCombo("Categoría", cmbCategoria)
        );

        HBox fila2 = new HBox(15);
        txtDescripcion.setPromptText("Descripción del producto");
        fila2.getChildren().add(crearCampo("Descripción", txtDescripcion));

        HBox fila3 = new HBox(15);
        txtPrecio.setPromptText("0.00");
        txtStock.setPromptText("0");
        txtStockMinimo.setPromptText("5");
        fila3.getChildren().addAll(
            crearCampo("Precio ($)", txtPrecio),
            crearCampo("Stock", txtStock),
            crearCampo("Stock Mínimo", txtStockMinimo)
        );

        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER_RIGHT);

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 8 20;");
        btnLimpiar.setOnAction(e -> limpiar());

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: " + COLOR_BOTON + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20;");
        btnGuardar.setOnAction(e -> guardarProducto());

        botones.getChildren().addAll(btnLimpiar, btnGuardar);
        panel.getChildren().addAll(titulo, fila1, fila2, fila3, botones);
        return panel;
    }

    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("LISTADO DE PRODUCTOS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        TableColumn<Producto, String> colNombre = columna("NOMBRE", "nombre", 150);
        TableColumn<Producto, String> colCategoria = columna("CATEGORÍA", "categoria", 110);
        
        TableColumn<Producto, Double> colPrecio = new TableColumn<>("PRECIO");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(90);

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
                    if (p.stockBajo()) {
                        setStyle("-fx-text-fill: " + COLOR_ALERTA + "; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
                }
            }
        });

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

        TableColumn<Producto, Void> colEditar = new TableColumn<>("ACCIÓN");
        colEditar.setPrefWidth(90);
        colEditar.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Editar");
            {
                btn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 4 10;");
                btn.setOnAction(e -> cargarProductoEnFormulario(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
tabla.getColumns().clear();

tabla.getColumns().add(colNombre);
tabla.getColumns().add(colCategoria);
tabla.getColumns().add(colPrecio);
tabla.getColumns().add(colStock);
tabla.getColumns().add(colAlerta);
tabla.getColumns().add(colEditar);

        tabla.setItems(listaProductos);
        tabla.setStyle("-fx-background-color: " + COLOR_CAMPO + ";");
        tabla.setPrefHeight(280);

        panel.getChildren().addAll(titulo, tabla);
        return panel;
    }

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

    private VBox crearCampoCombo(String etiqueta, ComboBox<String> combo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        box.getChildren().addAll(lbl, combo);
        return box;
    }

    private <T> TableColumn<Producto, T> columna(String titulo, String propiedad, double ancho) {
        TableColumn<Producto, T> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        return col;
    }

    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + "; -fx-text-fill: white; -fx-prompt-text-fill: #6b7280; -fx-background-radius: 6; -fx-padding: 8;";
    }

    private void guardarProducto() {
        String nombre      = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String categoria   = cmbCategoria.getValue();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto  = txtStock.getText().trim();
        String stockMinTexto = txtStockMinimo.getText().trim();

        if (nombre.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Nombre, precio y stock son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);
            int stockMinimo = Integer.parseInt(stockMinTexto.isEmpty() ? "5" : stockMinTexto);

            if (productoEditando != null) {
                productoEditando.setNombre(nombre);
                productoEditando.setDescripcion(descripcion);
                productoEditando.setCategoria(categoria);
                productoEditando.setPrecio(precio);
                productoEditando.setStock(stock);
                productoEditando.setStockMinimo(stockMinimo);
                
                // Actualizar en BD
                productoDAO.actualizar(productoEditando);
                
                tabla.refresh();
                productoEditando = null;
            } else {
                Producto p = new Producto(nombre, descripcion, categoria, precio, stock, stockMinimo);
                
                // Guardar en BD
                productoDAO.guardar(p);
                
                listaProductos.add(p);
            }
            limpiar();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al procesar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarProductoEnFormulario(Producto p) {
        productoEditando = p;
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        cmbCategoria.setValue(p.getCategoria());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
    }

    private void limpiar() {
        txtNombre.clear();
        txtDescripcion.clear();
        cmbCategoria.setValue("Suplemento");
        txtPrecio.clear();
        txtStock.clear();
        txtStockMinimo.clear();
        productoEditando = null;
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}