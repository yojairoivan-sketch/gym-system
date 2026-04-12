package com.gym.ui;

import com.gym.dao.EntrenadorDAO;
import com.gym.model.Entrenador;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class EntrenadorUI {

    // DAO para acceder directamente a los entrenadores
    private EntrenadorDAO entrenadorDAO = new EntrenadorDAO();

    // Campos del formulario de registro / edicion
    private TextField        tfNombre;
    private TextField        tfApellido;
    private TextField        tfCedula;
    private TextField        tfTelefono;
    private TextField        tfCorreo;
    private TextField        tfEspecialidad;
    private TextField        tfHorario;
    private ComboBox<String> cbEstado;

    // Tabla del listado de entrenadores
    private TableView<Entrenador>         tablaEntrenadores;
    private ObservableList<Entrenador>    listaEntrenadores = FXCollections.observableArrayList();

    // Entrenador que se esta editando (null cuando es registro nuevo)
    private Entrenador entrenadorEnEdicion = null;

    // Boton cuyo texto cambia entre "Guardar" y "Actualizar"
    private Button btnGuardar;

    private Label lblMensaje;

    // Construye y retorna la vista completa de la pantalla de entrenadores
    public VBox getVista() {
        VBox vista = new VBox(20);
        vista.setPadding(new Insets(30));

        // Carga estilo css
        vista.getStylesheets().add(
            getClass().getResource("/com/gym/ui/CSS/entrenadores.css").toExternalForm()
        );
        vista.getStyleClass().add("entrenador-root");

        // Titulo de la seccion
        Label titulo = new Label("Entrenadores");
        titulo.getStyleClass().add("entrenador-titulo");

        VBox seccionFormulario = crearFormularioRegistro();
        VBox seccionListado    = crearListadoEntrenadores();

        vista.getChildren().addAll(titulo, seccionFormulario, seccionListado);
        return vista;
    }

    // Crea la tarjeta con el formulario de registro o edicion
    private VBox crearFormularioRegistro() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.getStyleClass().add("entrenador-card");

        Label subtitulo = new Label("REGISTRAR ENTRENADOR");
        subtitulo.getStyleClass().add("entrenador-subtitulo");

        // Fila 1: nombre y apellido 
        tfNombre   = crearInput("Nombre");
        tfApellido = crearInput("Apellido");
        HBox fila1 = crearFila(
            crearGrupo("Nombre",   tfNombre),
            crearGrupo("Apellido", tfApellido)
        );

        // Fila 2: cedula y telefono 
        tfCedula   = crearInput("000-0000000-0");
        tfTelefono = crearInput("+1 809");
        HBox fila2 = crearFila(
            crearGrupo("Cedula",   tfCedula),
            crearGrupo("Telefono", tfTelefono)
        );

        // Fila 3: correo electronico 
        tfCorreo = crearInput("correo@email.com");
        HBox fila3 = crearFila(crearGrupo("Correo", tfCorreo));

        // Fila 4: especialidad y horario de trabajo 
        tfEspecialidad = crearInput("Ej: Musculacion, CrossFit");
        tfHorario      = crearInput("Ej: L-V 6AM-2PM");
        HBox fila4 = crearFila(
            crearGrupo("Especialidad", tfEspecialidad),
            crearGrupo("Horario",      tfHorario)
        );

        // Fila 5: estado del entrenador 
        cbEstado = new ComboBox<>(FXCollections.observableArrayList(
            "Activo", "Inactivo", "Descanso"
        ));
        cbEstado.setValue("Activo");
        cbEstado.setMaxWidth(Double.MAX_VALUE);
        cbEstado.getStyleClass().add("entrenador-combo");
        HBox fila5 = crearFila(crearGrupo("Estado", cbEstado));

        // Mensaje del usuario
        lblMensaje = new Label("");

        // Boton principal: texto cambia entre Guardar y Actualizar
        btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().add("entrenador-btn-primary");
        btnGuardar.setOnAction(e -> guardarEntrenador());

        // Boton para cancelar la edicion y limpiar el formulario
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("entrenador-btn-cancelar");
        btnCancelar.setVisible(false); // Solo visible al editar
        btnCancelar.setOnAction(e -> {
            entrenadorEnEdicion = null;
            btnGuardar.setText("Guardar");
            btnCancelar.setVisible(false);
            limpiarFormulario();
            lblMensaje.setText("");
        });

        HBox botonesBox = new HBox(10, btnGuardar, btnCancelar);
        botonesBox.setAlignment(Pos.BOTTOM_LEFT);

        // Guarda referencia al boton cancelar para mostrarlo al editar
        card.setUserData(btnCancelar);

        card.getChildren().addAll(
            subtitulo, fila1, fila2, fila3, fila4, fila5, lblMensaje, botonesBox
        );
        return card;
    }

    // Crea la tarjeta con la tabla del listado de entrenadores
    private VBox crearListadoEntrenadores() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.getStyleClass().add("entrenador-card");

        Label subtitulo = new Label("LISTADO DE ENTRENADORES");
        subtitulo.getStyleClass().add("entrenador-subtitulo");

        // Columna nombre completo 
        TableColumn<Entrenador, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getNombreCompleto())
        );
        colNombre.setPrefWidth(170);

        // Columna especialidad
        TableColumn<Entrenador, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEspecialidad())
        );
        colEspecialidad.setPrefWidth(180);

        // Columna horario 
        TableColumn<Entrenador, String> colHorario = new TableColumn<>("Horario");
        colHorario.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getHorario())
        );
        colHorario.setPrefWidth(140);

        // Columna telefono
        TableColumn<Entrenador, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTelefono())
        );
        colTelefono.setPrefWidth(120);

        // Columna estado segun valor 
        TableColumn<Entrenador, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().isActivo() ? "Activo" : "Inactivo")
        );
        colEstado.setPrefWidth(90);
        colEstado.setCellFactory(col -> new TableCell<Entrenador, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                getStyleClass().removeAll(
                    "entrenador-estado-activo",
                    "entrenador-estado-inactivo",
                    "entrenador-estado-descanso"
                );
                if (empty || estado == null) {
                    setText(null);
                } else {
                    setText(estado);
                    // estilo segun el estado del entrenador
                    switch (estado) {
                        case "Activo"    -> getStyleClass().add("entrenador-estado-activo");
                        case "Descanso"  -> getStyleClass().add("entrenador-estado-descanso");
                        default          -> getStyleClass().add("entrenador-estado-inactivo");
                    }
                }
            }
        });

        // Columna de acciones: Editar y Eliminar 
        TableColumn<Entrenador, Void> colAcciones = new TableColumn<>("");
        colAcciones.setPrefWidth(150);
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar   = crearBotonTabla("Editar",   "entrenador-btn-editar");
            private final Button btnEliminar = crearBotonTabla("Eliminar", "entrenador-btn-eliminar");
            private final HBox   caja        = new HBox(6, btnEditar, btnEliminar);

            {
                // Editar: carga los datos del entrenador en el formulario
                btnEditar.setOnAction(e -> {
                    Entrenador ent = getTableView().getItems().get(getIndex());
                    cargarEnFormulario(ent);
                });

                // Eliminar: borrado logico (activo = 0 en la BD)
                btnEliminar.setOnAction(e -> {
                    Entrenador ent = getTableView().getItems().get(getIndex());
                    entrenadorDAO.eliminar(ent.getId());
                    mostrarMensaje("Entrenador eliminado.", true);
                    cargarEntrenadores();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : caja);
            }
        });

        tablaEntrenadores = new TableView<>(listaEntrenadores);
        tablaEntrenadores.getColumns().addAll(
            colNombre, colEspecialidad, colHorario, colTelefono, colEstado, colAcciones
        );
        tablaEntrenadores.getStyleClass().add("entrenador-tabla");
        tablaEntrenadores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        cargarEntrenadores();

        card.getChildren().addAll(subtitulo, tablaEntrenadores);
        return card;
    }

    // Valida y guarda o actualiza el entrenador segun si hay edicion activa
    private void guardarEntrenador() {
        if (tfNombre.getText().isBlank()) {
            mostrarMensaje("El nombre es obligatorio.", false); return;
        }
        if (tfApellido.getText().isBlank()) {
            mostrarMensaje("El apellido es obligatorio.", false); return;
        }
        if (tfCedula.getText().isBlank()) {
            mostrarMensaje("La cedula es obligatoria.", false); return;
        }

        if (entrenadorEnEdicion == null) {
            // Registro de un entrenador nuevo
            Entrenador nuevo = new Entrenador(
                tfNombre.getText().trim(),
                tfApellido.getText().trim(),
                tfCedula.getText().trim(),
                null, // fecha de nacimiento no se captura en esta pantalla
                tfTelefono.getText().trim(),
                tfCorreo.getText().trim(),
                tfEspecialidad.getText().trim(),
                tfHorario.getText().trim()
            );
            entrenadorDAO.guardar(nuevo);
            mostrarMensaje("Entrenador registrado correctamente.", true);
        } else {
            // Actualizacion del entrenador cargado en edicion
            entrenadorEnEdicion.setNombre(tfNombre.getText().trim());
            entrenadorEnEdicion.setApellido(tfApellido.getText().trim());
            entrenadorEnEdicion.setCedula(tfCedula.getText().trim());
            entrenadorEnEdicion.setTelefono(tfTelefono.getText().trim());
            entrenadorEnEdicion.setCorreo(tfCorreo.getText().trim());
            entrenadorEnEdicion.setEspecialidad(tfEspecialidad.getText().trim());
            entrenadorEnEdicion.setHorario(tfHorario.getText().trim());
            entrenadorDAO.actualizar(entrenadorEnEdicion);
            mostrarMensaje("Entrenador actualizado correctamente.", true);
            entrenadorEnEdicion = null;
            btnGuardar.setText("Guardar");
        }

        limpiarFormulario();
        cargarEntrenadores();
    }

    // Carga los datos del entrenador seleccionado en el formulario para editar
    private void cargarEnFormulario(Entrenador ent) {
        entrenadorEnEdicion = ent;
        tfNombre.setText(ent.getNombre());
        tfApellido.setText(ent.getApellido());
        tfCedula.setText(ent.getCedula());
        tfTelefono.setText(ent.getTelefono()     != null ? ent.getTelefono()     : "");
        tfCorreo.setText(ent.getCorreo()         != null ? ent.getCorreo()       : "");
        tfEspecialidad.setText(ent.getEspecialidad() != null ? ent.getEspecialidad() : "");
        tfHorario.setText(ent.getHorario()       != null ? ent.getHorario()      : "");
        cbEstado.setValue(ent.isActivo() ? "Activo" : "Inactivo");
        btnGuardar.setText("Actualizar");
    }

    // Recarga la lista de entrenadores activos desde la base de datos
    private void cargarEntrenadores() {
        List<Entrenador> lista = entrenadorDAO.listarTodos();
        listaEntrenadores.setAll(lista);
    }

    // Limpia todos los campos del formulario
    private void limpiarFormulario() {
        tfNombre.clear();
        tfApellido.clear();
        tfCedula.clear();
        tfTelefono.clear();
        tfCorreo.clear();
        tfEspecialidad.clear();
        tfHorario.clear();
        cbEstado.setValue("Activo");
    }

    private void mostrarMensaje(String texto, boolean exito) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().removeAll(
            "entrenador-mensaje-exito", "entrenador-mensaje-error"
        );
        lblMensaje.getStyleClass().add(
            exito ? "entrenador-mensaje-exito" : "entrenador-mensaje-error"
        );
    }


    // Crea un TextField con placeholder
    private TextField crearInput(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.getStyleClass().add("entrenador-input");
        return tf;
    }

    // Crea una fila horizontal con todos los grupos recibidos
    private HBox crearFila(VBox... grupos) {
        HBox fila = new HBox(14);
        for (VBox g : grupos) {
            HBox.setHgrow(g, Priority.ALWAYS);
            fila.getChildren().add(g);
        }
        return fila;
    }

    // Crea un grupo etiqueta + control apilados verticalmente
    private VBox crearGrupo(String etiqueta, javafx.scene.Node control) {
        Label lbl = new Label(etiqueta.toUpperCase());
        lbl.getStyleClass().add("entrenador-label");
        VBox grupo = new VBox(6, lbl, control);
        VBox.setVgrow(control, Priority.ALWAYS);
        return grupo;
    }

    // Crea un boton de tabla 
    private Button crearBotonTabla(String texto, String cssClass) {
        Button btn = new Button(texto);
        btn.getStyleClass().add(cssClass);
        return btn;
    }
}