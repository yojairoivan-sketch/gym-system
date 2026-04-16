package com.gym.ui;

import com.gym.model.Usuario;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class Main extends Application {

private static final String COLOR_FONDO = "#1a1a2e";
private static final String COLOR_NAVBAR = "#0f3460";
private static final String COLOR_BOTON = "#7c3aed";

    @Override
    public void start(Stage stage) {

LoginUI loginUI = new LoginUI(usuario -> {
cargarSistema(stage, usuario);
});

Scene scene = new Scene(loginUI.getVista(), 400, 350);
stage.setScene(scene);
stage.setTitle("Login - GymPro");
stage.show();
}

private void cargarSistema(Stage stage, Usuario usuario) {

// modulos
ClienteUI clienteUI = new ClienteUI();
MembresiaUI membresiaUI = new MembresiaUI();
InscripcionUI inscripcionUI = new InscripcionUI();
ProductoUI productoUI = new ProductoUI();
VentaUI ventaUI = new VentaUI(productoUI.getListaProductos());

PagoUI pagoUI = new PagoUI();
AsistenciaUI asistenciaUI = new AsistenciaUI();
EntrenadorUI entrenadorUI = new EntrenadorUI();

HBox navbar = new HBox(8);
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

Scene scene = new Scene(root, 1050, 700);
stage.setScene(scene);
stage.setTitle("GymPro - Sistema de Gestion");
}

private void cambiarVista(ScrollPane scroll, Button activo, Button[] todos, Pane vista) {
scroll.setContent(vista);
resaltarBoton(activo, todos);
}

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

public static void main(String[] args) {
launch(args);
}
}