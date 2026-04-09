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

// Clase principal que inicia la aplicación y maneja la navegación
public class Main extends Application {

    // Colores del tema oscuro
    private static final String COLOR_FONDO  = "#1a1a2e";
    private static final String COLOR_NAVBAR = "#0f3460";
    private static final String COLOR_BOTON  = "#7c3aed";
    private static final String COLOR_TEXTO  = "#ffffff";

    @Override
    public void start(Stage stage) {

        // ── Barra de navegación superior ──
        HBox navbar = new HBox(15);
        navbar.setStyle("-fx-background-color: " + COLOR_NAVBAR + ";");
        navbar.setPadding(new Insets(12, 20, 12, 20));

        // Logo del sistema
        Label logo = new Label("💪 GymPro");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        logo.setTextFill(Color.web(COLOR_BOTON));

        // Botones de navegación
        Button btnInscripcion = crearBotonNav("Inscripción");
        Button btnClientes    = crearBotonNav("Clientes");
        Button btnMembresias  = crearBotonNav("Membresías");

        navbar.getChildren().addAll(logo, btnInscripcion, btnClientes, btnMembresias);

        // ── Área de contenido con scroll ──
        // Aquí se muestra la pantalla seleccionada
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // ── Crear las vistas de cada pantalla ──
        ClienteUI clienteUI       = new ClienteUI();
        MembresiaUI membresiaUI   = new MembresiaUI();
        InscripcionUI inscripcionUI = new InscripcionUI();

        // Mostrar la pantalla de Inscripción por defecto al iniciar
        scroll.setContent(inscripcionUI.getVista());

        // ── Navegación: cada botón cambia el contenido del scroll ──
        btnInscripcion.setOnAction(e -> {
            scroll.setContent(inscripcionUI.getVista());
            resaltarBoton(btnInscripcion, btnClientes, btnMembresias);
        });

        btnClientes.setOnAction(e -> {
            scroll.setContent(clienteUI.getVista());
            resaltarBoton(btnClientes, btnInscripcion, btnMembresias);
        });

        btnMembresias.setOnAction(e -> {
            scroll.setContent(membresiaUI.getVista());
            resaltarBoton(btnMembresias, btnInscripcion, btnClientes);
        });

        // Resaltar el botón de Inscripción por defecto
        resaltarBoton(btnInscripcion, btnClientes, btnMembresias);

        // ── Layout principal: navbar arriba + contenido abajo ──
        VBox root = new VBox(navbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS); // el scroll ocupa todo el espacio restante

        // ── Configuración de la ventana ──
        Scene scene = new Scene(root, 950, 700);
        stage.setTitle("GymPro - Sistema de Gestión");
        stage.setScene(scene);
        stage.show();
    }

    // Crea un botón de navegación con estilo base
    private Button crearBotonNav(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13;" +
            "-fx-padding: 6 12;" +
            "-fx-background-radius: 6;"
        );
        return btn;
    }

    // Resalta el botón activo y quita el resaltado de los otros
    private void resaltarBoton(Button activo, Button... otros) {
        // Estilo del botón activo (morado)
        activo.setStyle(
            "-fx-background-color: #7c3aed;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13;" +
            "-fx-padding: 6 12;" +
            "-fx-background-radius: 6;"
        );
        // Estilo normal para los otros botones
        for (Button btn : otros) {
            btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13;" +
                "-fx-padding: 6 12;" +
                "-fx-background-radius: 6;"
            );
        }
    }

    // Método main que lanza la aplicación
    public static void main(String[] args) {
        launch(args);
    }
}