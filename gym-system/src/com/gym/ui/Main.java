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

Label logo = new Label("💪 GymPro");
logo.setFont(Font.font("Impact", FontWeight.BOLD, 18));
logo.setTextFill(Color.WHITE);

Button btnInscripcion = crearBotonNav("Inscripcion");
Button btnClientes = crearBotonNav("Clientes");
Button btnMembresias = crearBotonNav("Membresias");
Button btnProductos = crearBotonNav("Productos");
Button btnVentas = crearBotonNav("Ventas");
Button btnPagos = crearBotonNav("Pagos");
Button btnAsistencia = crearBotonNav("Asistencia");
Button btnEntrenadores= crearBotonNav("Entrenadores");

Button btnLogout = crearBotonNav("Cerrar Sesion");

btnLogout.setStyle(
"-fx-background-color: #4c1987;" +
"-fx-text-fill: white;" +
"-fx-font-weight: bold;" +
"-fx-padding: 6 14;" +
"-fx-background-radius: 8;"
);

// usando ENUM
Usuario.Rol rol = usuario.getRol();

// CONTROL POR ROLES
HBox izquierda = new HBox(logo);

HBox centro = new HBox(10);
centro.setAlignment(Pos.CENTER);

HBox derecha = new HBox(btnLogout);
derecha.setAlignment(Pos.CENTER_RIGHT);

// 🔐 agregar botones según rol
switch (rol) {

case ENTRENADOR:
centro.getChildren().addAll(
btnClientes,
btnAsistencia
);
break;

case RECEPCIONISTA:
centro.getChildren().addAll(
btnInscripcion,
btnClientes,
btnMembresias,
btnVentas,
btnPagos,
btnAsistencia
);
break;

case ADMINISTRADOR:
centro.getChildren().addAll(
btnInscripcion,
btnClientes,
btnMembresias,
btnProductos,
btnVentas,
btnPagos,
btnAsistencia,
btnEntrenadores
);
break;
}

// espaciadores para centrar
Region espacioIzq = new Region();
Region espacioDer = new Region();
HBox.setHgrow(espacioIzq, Priority.ALWAYS);
HBox.setHgrow(espacioDer, Priority.ALWAYS);

// agregar todo al navbar
navbar.getChildren().addAll(
izquierda,
espacioIzq,
centro,
espacioDer,
derecha
);

ScrollPane scroll = new ScrollPane();
scroll.setFitToWidth(true);
scroll.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

// vista inicial según rol
switch (rol) {
case ENTRENADOR:
scroll.setContent(asistenciaUI.getVista());
break;
default:
scroll.setContent(inscripcionUI.getVista());
break;
}

Button[] todosBotones = {
btnInscripcion, btnClientes, btnMembresias,
btnProductos, btnVentas,
btnPagos, btnAsistencia, btnEntrenadores
};

btnInscripcion.setOnAction(e ->
cambiarVista(scroll, btnInscripcion, todosBotones, inscripcionUI.getVista())
);

btnClientes.setOnAction(e ->
cambiarVista(scroll, btnClientes, todosBotones, clienteUI.getVista())
);

btnMembresias.setOnAction(e ->
cambiarVista(scroll, btnMembresias, todosBotones, membresiaUI.getVista())
);

btnProductos.setOnAction(e ->
cambiarVista(scroll, btnProductos, todosBotones, productoUI.getVista())
);

btnVentas.setOnAction(e ->
cambiarVista(scroll, btnVentas, todosBotones, ventaUI.getVista())
);

btnPagos.setOnAction(e ->
cambiarVista(scroll, btnPagos, todosBotones, pagoUI.getVista())
);

btnAsistencia.setOnAction(e ->
cambiarVista(scroll, btnAsistencia, todosBotones, asistenciaUI.getVista())
);

btnEntrenadores.setOnAction(e ->
cambiarVista(scroll, btnEntrenadores, todosBotones, entrenadorUI.getVista())
);

btnLogout.setOnAction(e -> {
LoginUI loginUI = new LoginUI(usuarioLogin -> {
cargarSistema(stage, usuarioLogin);
});

Scene scene = new Scene(loginUI.getVista(), 400, 350);
stage.setScene(scene);
stage.setTitle("Login - GymPro");
});

VBox root = new VBox(navbar, scroll);
VBox.setVgrow(scroll, Priority.ALWAYS);

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

private void resaltarBoton(Button activo, Button[] todos) {
for (Button btn : todos) {
if (btn == activo) {
btn.setStyle(
"-fx-background-color: #7c3aed;" +
"-fx-text-fill: white;"
);
} else {
btn.setStyle(
"-fx-background-color: transparent;" +
"-fx-text-fill: white;"
);
}
}
}

public static void main(String[] args) {
launch(args);
}
}