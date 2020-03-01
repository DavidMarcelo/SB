package sb.administrador;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sb.SQL.SQLConnection;
import sb.clases.Cliente;

/**
 * FXML Controller class
 *
 * @author L745
 */
public class AdministradorController implements Initializable {

    @FXML
    private TableView<Cliente> table_clientes;
    @FXML
    private TableColumn<Cliente,String>col_nombre,col_ap,col_am,col_tel,col_comentario;
    @FXML
    private TableColumn<Cliente,Date>col_inicio,col_vencimiento;
    @FXML
    private TableColumn<Cliente,Integer>col_id,col_edad;
    @FXML
    private TableColumn<Cliente,File>col_foto_usuario;    
    
//    ******************Datos********************************************
    public TextField tf_nombre,tf_apellido_materno,tf_apellido_paterno,tf_edad,tf_telefono,ta_comentario,tf_buscar;
    //public Label vNombre, vApellidoM, vApellidoP, vEdad, vTelefono, vCantidad, vFechaI,vFechaV;
    public DatePicker inicio,fin;
    //public TextArea ta_comentario;
    Connection connection;
    
//    ******************Datos********************************************
    
    ObservableList<Cliente> lista = FXCollections.observableArrayList();
    //--------------------
    public FileInputStream fis; 
    public Image image;
    public ImageView usuario;
    File file;
    byte[] bytearray;
    boolean imagen = false;
    //-------------------
    
    
    //Funcion para validar campos de texto
    public boolean validar(){
        boolean bandera;
        String ini = inicio.getEditor().getText();
        String fi = fin.getEditor().getText();
        if(tf_nombre.getText().equals("") || tf_apellido_materno.getText().equals("") || ini.equals("") || fi.equals("")
                || tf_apellido_paterno.getText().equals("") || tf_edad.getText().equals("") || tf_telefono.getText().equals("")
                || ta_comentario.getText().equals("") || usuario.getImage()==null){
            bandera = false;
        }else{
            bandera = true;
        }
        return bandera;
    }
    
    public void seleccionar() throws SQLException{
//        Me falta establecer en textfield las coas de la tabla
        Cliente listita = table_clientes.getSelectionModel().getSelectedItem();
        System.out.println(listita.getApellido_materno());
        tf_nombre.setText(listita.getNombre());
        tf_apellido_paterno.setText(listita.getApellido_paterno());
        tf_apellido_materno.setText(listita.getApellido_materno());
        tf_edad.setText(String.valueOf(listita.getEdad()));
        tf_telefono.setText(listita.getTelefono());
        inicio.setValue(listita.getInicio().toLocalDate());
        fin.setValue(listita.getFin().toLocalDate());
        ta_comentario.setText(listita.getComentarios());
        //////////////////////////////////////////////////////
        Blob blob = listita.getUsuario();
        InputStream ist = blob.getBinaryStream();
        image = new Image(ist);
        usuario.setImage(image);
        
        
    }
    
    public void cambiarFoto() throws SQLException, FileNotFoundException{
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(null);
        fis = new FileInputStream(file);// file is selected using filechooser which is in last tutorial
        if(file != null){
            image = new Image(file.toURI().toString());
            usuario.setImage(null);
            usuario.setImage(image);
            imagen = true;            
        }else{
            System.out.println("vacio!");
        }
        
        Cliente listita = table_clientes.getSelectionModel().getSelectedItem();
        int id = listita.getId();
        System.out.print("id: "+id);
        if (connection != null ){
            String query = "UPDATE cliente SET foto=? WHERE id='"+id+"'";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setBinaryStream(1, (InputStream) fis, (int)file.length());
            preparedStmt.executeUpdate();
            System.out.println("Foto actualizada!");
            //listita.setUsuario((Blob) fis);
            //this.table_clientes.refresh();
            //new Alert(Alert.AlertType.INFORMATION, "actualizado").show();
        }
    }
    
    public void editar() throws SQLException{
        if (validar()==true){
            Cliente listita = table_clientes.getSelectionModel().getSelectedItem();
            int id = listita.getId();
            String nombre = tf_nombre.getText();
            String paterno = tf_apellido_paterno.getText();
            String materno = tf_apellido_materno.getText();
            int edad = Integer.valueOf(tf_edad.getText());
            String telefono = tf_telefono.getText();
            LocalDate i = inicio.getValue();
            LocalDate f = fin.getValue();
            String observacion = ta_comentario.getText();
            if (connection != null) {
                String query = "UPDATE cliente SET nombre = ? ,apellido_paterno = ?,apellido_materno= ? ,edad = ? ,telefono= ? ,inicio=? ,vencimiento=? ,observaciones=? WHERE id = '"+id+"' ";
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, nombre);
                preparedStmt.setString(2, paterno);
                preparedStmt.setString(3, materno);
                preparedStmt.setInt(4, edad);
                preparedStmt.setString(5, telefono);
                preparedStmt.setDate(6,Date.valueOf(i));
                preparedStmt.setDate(7,Date.valueOf(f));
                preparedStmt.setString(8,observacion);
                preparedStmt.executeUpdate();      
                
                listita.setNombre(nombre);
                listita.setApellido_paterno(paterno);
                listita.setApellido_materno(materno);
                listita.setEdad(edad);
                listita.setTelefono(telefono);
                listita.setInicio(java.sql.Date.valueOf(i));
                listita.setFin(java.sql.Date.valueOf(f));
                listita.setComentarios(observacion);            
                this.table_clientes.refresh();
                new Alert(Alert.AlertType.INFORMATION, "actualizado").show();

                tf_nombre.setText(null); 
                tf_apellido_materno.setText(null);
                tf_apellido_paterno.setText(null);
                tf_edad.setText(null);
                tf_telefono.setText(null);
                ta_comentario.setText(null);
                tf_buscar.setText(null);
                fin.getEditor().setText(null);
                inicio.getEditor().setText(null);

            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Campos vacios!").show();
        }
        //connection.close();
       
    }
    
    public void eliminar() throws SQLException{
        Cliente listita = table_clientes.getSelectionModel().getSelectedItem();
        int id = listita.getId();
        String nombre = tf_nombre.getText();
        String paterno = tf_apellido_paterno.getText();
        String materno = tf_apellido_materno.getText();
        int edad = Integer.parseInt(tf_edad.getText());
        String telefono = tf_telefono.getText();
        LocalDate i = inicio.getValue();
        LocalDate f = fin.getValue();
        String observacion = ta_comentario.getText();
        String query = " DELETE FROM cliente WHERE id ='"+listita.getId()+ "';";        
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.execute();             
        lista.remove(listita);
        table_clientes.refresh();
        new Alert(Alert.AlertType.INFORMATION, "eliminado").show();
    }
    
        
    public static int restar(String fecha, String fechaV){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fechaInicial = null;
        java.util.Date fechaFinal = null;
        try {
            fechaInicial = dateFormat.parse(fecha);
            fechaFinal = dateFormat.parse(fechaV);
        } catch (ParseException ex) {
            Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);
        System.out.println("Hay "+dias+" dias de diferencia");
        return dias;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
                //this.connection = new SQLConnection("andres", "contagiate", "sb").getConnection();
                this.connection = new SQLConnection("root", "", "sb").getConnection();

                System.out.println("Conexion Lista");

            } catch (ClassNotFoundException | SQLException e) {

                System.out.println("Conexión no realizada"+e.getMessage());
            }
        
        try {
            ResultSet rs = connection.createStatement().executeQuery(
                    "select id,nombre,apellido_paterno,apellido_materno,edad,telefono,inicio,vencimiento,observaciones,foto from cliente"
            );
            /*DateFormat ff = new SimpleDateFormat("YYYY-MM-dd");
            String fecha = String.valueOf(ff.format(new java.util.Date()));
            int[] total = new int[10000];
            int cont = 0;*/
            while(rs.next()){
//                OutputStream  out = new FileOutputStream
                //String fechaV = String.valueOf(rs.getDate("vencimiento"));
                //System.out.println("Fechav: "+fechaV);
                //System.out.println("FechaA: "+fecha);
                lista.add(new Cliente(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getInt("edad"), 
                        rs.getString("telefono"), 
                        rs.getDate("inicio"),
                        rs.getDate("vencimiento"), 
                        rs.getString("observaciones"), 
                        rs.getBlob("foto")));
                /*total[cont] = restar(fecha, fechaV);
                //int tot = restar(fecha, fechaV);
                cont++;
                table_clientes.setRowFactory(tv -> new TableRow<Cliente>() {
                @Override
                public void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item.getFin());
                    for (int i = 0; i < lista.size(); i++) {
                        if (total[i]<=7) {
                            System.out.println("rojo\n");
                            setStyle("-fx-background-color: red;");
                        } else {
                            System.out.println("verde\n");
                            setStyle("-fx-background-color: green;");
                        }
                    }
                    }
                });*/
            }
            
            
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            col_ap.setCellValueFactory(new PropertyValueFactory<>("apellido_paterno"));
            col_am.setCellValueFactory(new PropertyValueFactory<>("apellido_materno"));
            col_edad.setCellValueFactory(new PropertyValueFactory<>("edad"));
            col_tel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            col_inicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
            col_vencimiento.setCellValueFactory(new PropertyValueFactory<>("fin"));
            col_comentario.setCellValueFactory(new PropertyValueFactory<>("comentarios"));
            col_foto_usuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
            table_clientes.setItems(lista);
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        FilteredList<Cliente> filtro = new FilteredList<>(lista,e->true);
        tf_buscar.textProperty().addListener((observableValue,oldValue,newValue)->{
            filtro.setPredicate((Predicate<? super Cliente>) cliente -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }                
                String filtro_minusculas = newValue.toLowerCase();                
                if(cliente.getNombre().toLowerCase().contains(filtro_minusculas)){
                    return true;
                }else if(cliente.getApellido_paterno().toLowerCase().contains(filtro_minusculas)){
                    return true;
                }                
                return false;
            });
            //aqui vamos
            SortedList<Cliente> lista_orden =  new SortedList<>(filtro);
            lista_orden.comparatorProperty().bind(table_clientes.comparatorProperty());
            table_clientes.setItems(lista_orden);            
        });
        
        
    }
    
}
