package com.gym.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String COLOR_FONDO  = "#1a1a2e";
    private static final String COLOR_NAVBAR = "#0f3460";
    private static final String COLOR_BOTON  = "#7c3aed";

    @Override
    public void start(Stage stage) {

        // Instanciamos las pantallas
        ClienteUI clienteUI         = new ClienteUI();
        MembresiaUI membresiaUI     = new MembresiaUI();
        InscripcionUI inscripcionUI = new InscripcionUI();
        ProductoUI productoUI       = new ProductoUI();
        
        // VentaUI suele necesitar la lista de productos cargada
         VentaUI ventaUI = new VentaUI();

        HBox navbar = new HBox(15);
        navbar.setStyle("-fx-background-color: " + COLOR_NAVBAR + ";");
        navbar.setPadding(new Insets(12, 20, 12, 20));

        Label logo = new Label("💪 GymPro");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        logo.setTextFill(Color.web(COLOR_BOTON));

        Button btnInscripcion = crearBotonNav("Inscripción");
        Button btnClientes    = crearBotonNav("Clientes");
        Button btnMembresias  = crearBotonNav("Membresías");
        Button btnProductos   = crearBotonNav("Productos");
        Button btnVentas      = crearBotonNav("Ventas");

        navbar.getChildren().addAll(logo, btnInscripcion, btnClientes, btnMembresias, btnProductos, btnVentas);

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Pantalla inicial
        scroll.setContent(inscripcionUI.getVista());

        // Lógica de navegación con refresco de datos
        btnInscripcion.setOnAction(e -> {
            scroll.setContent(inscripcionUI.getVista());
            resaltarBoton(btnInscripcion, btnClientes, btnMembresias, btnProductos, btnVentas);
        });

        btnClientes.setOnAction(e -> {
            // OPCIONAL: Podrías llamar a clienteUI.cargarDatosDesdeBD() aquí si quieres refresco total
            scroll.setContent(clienteUI.getVista());
            resaltarBoton(btnClientes, btnInscripcion, btnMembresias, btnProductos, btnVentas);
        });

        btnMembresias.setOnAction(e -> {
            scroll.setContent(membresiaUI.getVista());
            resaltarBoton(btnMembresias, btnInscripcion, btnClientes, btnProductos, btnVentas);
        });

        btnProductos.setOnAction(e -> {
            scroll.setContent(productoUI.getVista());
            resaltarBoton(btnProductos, btnInscripcion, btnClientes, btnMembresias, btnVentas);
        });

        btnVentas.setOnAction(e -> {
            scroll.setContent(ventaUI.getVista());
            resaltarBoton(btnVentas, btnInscripcion, btnClientes, btnMembresias, btnProductos);
        });

        resaltarBoton(btnInscripcion, btnClientes, btnMembresias, btnProductos, btnVentas);

        VBox root = new VBox(navbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Scene scene = new Scene(root, 950, 700);
        stage.setTitle("GymPro - Sistema de Gestión");
        stage.setScene(scene);
        stage.show();
    }

    private Button crearBotonNav(String texto) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 13; -fx-padding: 6 12; -fx-background-radius: 6;");
        return btn;
    }

    private void resaltarBoton(Button activo, Button... otros) {
        activo.setStyle("-fx-background-color: " + COLOR_BOTON + "; -fx-text-fill: white; -fx-font-size: 13; -fx-padding: 6 12; -fx-background-radius: 6;");
        for (Button btn : otros) {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 13; -fx-padding: 6 12; -fx-background-radius: 6;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}