//region ImportLibraris
package com.example.kursachput;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;

import java.sql.Connection;

//endregion

public class HelloController implements Initializable {

    //region ConnectToBD
    String url = "jdbc:mysql://192.168.0.179:3306/CursachPutOdMi";
    String user = "Yoghurt";
    String password = "Kaka228";

    private Connection connection;

    public File selectedFile;

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLException: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных успешно установлено!");
            uploadElements();
            fiilGridTable();
            fillCB();
            fillVidForProductCreatePane();
            fillTableOrderFinish(listOrderFinish, tbViewFinish, tbCollumnID, tbDateAndTimeOrder, tbMidPriseOrder, tbGenPrise);
            for (int i = 0; i < listVidNap.size(); i++){
                CBcreteElemVid.getItems().add(listVidNap.get(i));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
            printSQLException(e);
        }
    }
    //endregion

    //region CreateElements
    @FXML
    private BorderPane changePane;
    @FXML
    private Pane analit, product, comapny, CreateElemPane, paneEdit, paneForCreateElements,paneForCreateVid;
    @FXML
    private Button analitButMenu, rpoductButMenu, salesButMenu, сompanyButMenu;
    @FXML
    private ImageView imageCreatePane, imageEditPane, imgAddFromOrder;
    @FXML
    private TextField TFcreateElemName, TFcreateElemSize, TFcreateElemPrise, TFcreateElemKolVo,
            TFeditElemName, TFeditElemSize, TFeditElemPrise, TFeditElemKolVo,TFCreateVidNap, tfSearchProd,
            tfAddToOrderkolVo, tfIDdelite;
    @FXML
    private ScrollPane scrolPaneNap, scrolPaneZak;
    @FXML
    private AnchorPane salePane;
    @FXML
    private Label resultSearch, nameFromOrder, priseFromOrder, kolVoFromOrder;
    @FXML
    private ComboBox ComBox, CBTypeTov, CBProduct, CBcreteElemVid, choiceWhatsCreate,
            CBVidProdFromEdit, VidDeleteEdit, CBTypeTov1, CBsalesPaneType, CBsalesPaneVid, CBsalesPaneProd;
    @FXML
    private TableView  tbViewOrderStr;
    @FXML
    private TableView tbViewFinish;
    @FXML
    private TableColumn<ObjectProd,Integer> tbCollumnKolVo, tbCollumnPrise, tbCollumnID, tbNumOrder, tbMidPriseOrder, tbGenPrise;
    @FXML
    private TableColumn<ObjectProd,String> tbCollumnName, tbDateAndTimeOrder;

    ArrayList<ObjectProd> list = new ArrayList<ObjectProd>();
    ArrayList<ObjectProd> listZak = new ArrayList<ObjectProd>();
    ArrayList<String> listVidNap = new ArrayList<>();
    ArrayList<String> listVidZak = new ArrayList<>();
    ObservableList<ObjectOrder> listOrderStruct = FXCollections.observableArrayList();
    ObservableList<ObjectFinishOrder> listOrderFinish = FXCollections.observableArrayList();
    String name = null;

    //endregion
    @FXML
    protected void SendToServ() {
        String x = String.valueOf(ComBox.getValue());
        if(x.equals("Напиток") && CreateElemPane.isVisible()){
            SendNapOrZak("ProductNap");
        }
        else{
            SendNapOrZak("ProductZak");
        }


    }
    @FXML
    protected void fillSearchResult(){
        String find = tfSearchProd.getText();
        try {
            String quary = "SELECT nameNap FROM ProductNap WHERE nameNap LIKE \""+ find +"%\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(quary);
            while(resultSet.next()){
                name = resultSet.getString(1);
            }
            resultSearch.setText(name);
        } catch (SQLException ignored) { }
        try {
            String quary = "SELECT nameZak FROM ProductZak WHERE nameZak LIKE \""+ find +"%\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(quary);
            while(resultSet.next()){
                name = resultSet.getString(1);
            }
            resultSearch.setText(name);
        } catch (SQLException ignored) { }

    }

    public void SendNapOrZak(String tableName){

        String prodName = null;
        double prodSize = 0;
        double prodPrise = 0;
        int prodKolVo = 0;
        String prodVid = null;
        FileInputStream fis = null;


        try {
            prodName = TFcreateElemName.getText();
            prodSize = Double.parseDouble(TFcreateElemSize.getText());
            prodPrise = Double.parseDouble(TFcreateElemPrise.getText());
            prodKolVo = Integer.parseInt(TFcreateElemKolVo.getText());
            fis = new FileInputStream(selectedFile);
            prodVid = CBcreteElemVid.getValue().toString();

        } catch (Exception e) {
            System.out.println("Ошибка в записи");
        }
        try {
            String quary = "insert " + tableName + " values (?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(quary);
            preparedStatement.setString(1, prodName);
            preparedStatement.setDouble(2, prodSize);
            preparedStatement.setDouble(3, prodPrise);
            preparedStatement.setBlob(4, fis);
            preparedStatement.setInt(5, prodKolVo);
            preparedStatement.setString(6, prodVid);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

    }
    @FXML
    protected void addImageForCreatePane() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            if(changePane.isVisible())
            {
                imageEditPane.setImage(image);
                editImagePozition(imageEditPane);
            }else {
                imageCreatePane.setImage(image);
                editImagePozition(imageCreatePane);
            }

        }


    }
    public void fillCB(){
        ComBox.getItems().addAll("Напиток", "Закуска");
        ComBox.getSelectionModel().select("Напиток");
        CBTypeTov.getItems().addAll("Напиток", "Закуска");
        CBTypeTov.getSelectionModel().select("Напиток");
        CBTypeTov1.getItems().addAll("Напиток", "Закуска");
        CBTypeTov1.getSelectionModel().select("Напиток");
        CBsalesPaneType.getItems().addAll("Напиток", "Закуска");
        CBsalesPaneType.getSelectionModel().select("Напиток");

        for (int i = 0; i < list.size(); i++) {
            CBProduct.getItems().addAll(list.get(i).name);
        }

        choiceWhatsCreate.getItems().addAll("Товар","Вид напитка", "Вид закуски");
        choiceWhatsCreate.getSelectionModel().select("Товар");
    }
    @FXML
    protected  void loadProdFromOrder(){
        loadProdFromOrderAll(resultSearch, null);
    }
    @FXML
    protected  void loadProdFromOrderByComBox(){
        loadProdFromOrderAll(null, CBsalesPaneProd);
    }
    public void loadProdFromOrderAll(Label resLabel, ComboBox resComBox){

        try {
            String nameProd = null;
            if(resLabel != null){
                nameProd = resLabel.getText();
            }else {
                nameProd = resComBox.getValue().toString();
            }
            double priseProd = 0;
            int kolVoProd = 0;
            Blob imageProd = null;

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).name.equals(nameProd)) {
                    priseProd = list.get(i).prise;
                    kolVoProd = list.get(i).kolVo;
                    imageProd = list.get(i).image;
                    break;
                }
            }

            for (int i = 0; i < listZak.size(); i++) {
                if (listZak.get(i).name.equals(nameProd)) {
                    priseProd = listZak.get(i).prise;
                    kolVoProd = listZak.get(i).kolVo;
                    imageProd = listZak.get(i).image;
                    break;
                }
            }


            nameFromOrder.setText(nameProd);
            priseFromOrder.setText("Цена: " + priseProd);
            kolVoFromOrder.setText("Осталось на складе: " + kolVoProd);
            imgAddFromOrder.setImage(decodeImage(imageProd));
            editImagePozition(imgAddFromOrder);
        } catch (Exception ignored) { }

    }
    @FXML
    protected void choiceFillCB() {

        if(CBTypeTov.getValue().equals("Напиток")) {
            CBProduct.getItems().clear();
            for (int i = 0; i < list.size(); i++) {
                CBProduct.getItems().addAll(list.get(i).name);
            }
        }
        else {
            CBProduct.getItems().clear();
            for (int i = 0; i < listZak.size(); i++) {
                    CBProduct.getItems().addAll(listZak.get(i).name);
            }
        }
    }
    @FXML
    public void choiceFillCBUp() {
        fillVidCBfromCBProd(CBTypeTov1, VidDeleteEdit);
    }
    public void fillVidCBfromCBProd(ComboBox typeTov, ComboBox vidTov){
        if(typeTov.getValue().equals("Напиток")) {
            vidTov.getItems().clear();
            for (int i = 0; i < listVidNap.size(); i++) {
                vidTov.getItems().add(listVidNap.get(i));
            }
        }
        else {
            vidTov.getItems().clear();
            for (int i = 0; i < listVidZak.size(); i++) {
                vidTov.getItems().add(listVidZak.get(i));
            }
        }
    }
    @FXML
    protected void salesPaneFillCBVid(){
        fillVidCBfromCBProd(CBsalesPaneType, CBsalesPaneVid);
    }
    @FXML
    protected void salesPaneFillCBProd(){
        String vid = null;
        try {
            vid = CBsalesPaneVid.getValue().toString();
        } catch (Exception ignored) {}

        if (CBsalesPaneType.getValue().equals("Напиток")) {
            CBsalesPaneProd.getItems().clear();
            for (int i = 0; i<list.size(); i++) {

                while (i<list.size()) {
                    if (!list.get(i).vid.equals(vid)){
                        break;
                    }
                    CBsalesPaneProd.getItems().add(list.get(i).name);
                    i++;
                }

            }
        }else {
            CBsalesPaneProd.getItems().clear();
            for (int i = 0; i<listZak.size(); i++) {

                while (i<listZak.size()) {
                    if (!listZak.get(i).vid.equals(vid)){
                        break;
                    }
                    CBsalesPaneProd.getItems().add(listZak.get(i).name);
                    i++;
                }

            }
        }

    }
    public void fillVidForProductCreatePane(){
        String query = "SELECT * FROM vidNapTable";
        String queryZak = "SELECT * FROM vidZakTable";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                listVidNap.add(resultSet.getString(1));
            }

            ResultSet rs = statement.executeQuery(queryZak);
            while (rs.next()) {
                listVidZak.add(rs.getString(1));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }


    }
    @FXML
    protected void createVid() {
        if(choiceWhatsCreate.getValue().equals("Товар")) {
            paneForCreateElements.setVisible(true);
            paneForCreateVid.setVisible(false);
        }else if(choiceWhatsCreate.getValue().equals("Вид напитка")){
            paneForCreateElements.setVisible(false);
            paneForCreateVid.setVisible(true);
            TFCreateVidNap.setPromptText("Вид напитка");
        }else {
            paneForCreateElements.setVisible(false);
            paneForCreateVid.setVisible(true);
            TFCreateVidNap.setPromptText("Вид закуски");
        }

    }
    @FXML
    protected void createVidNapBut() {
        String prodName = null;
        String tableName = null;

        if(choiceWhatsCreate.getValue().equals("Вид напитка")){
            tableName = "vidNapTable";
        }else {
            tableName = "vidZakTable";
        }

        try {
            prodName = TFCreateVidNap.getText();

        } catch (Exception e) {
            System.out.println("Ошибка в записи");
        }


        try {
            String quary = "insert " + tableName + " values (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(quary);
            preparedStatement.setString(1, prodName);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

    }
    public void uploadElements() {
        String query = "SELECT * FROM ProductNap";
        String queryZak = "SELECT * FROM ProductZak";
        String queryOrder = "SELECT * FROM orderFinish";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new ObjectProd(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3),
                        resultSet.getBlob(4), resultSet.getInt(5), resultSet.getString(6)));
            }

            ResultSet rs = statement.executeQuery(queryZak);
            while (rs.next()) {
                listZak.add(new ObjectProd(rs.getString(1), rs.getDouble(2), rs.getDouble(3),
                        rs.getBlob(4), rs.getInt(5), rs.getString(6)));
            }
            ResultSet resultSet1 = statement.executeQuery(queryOrder);
            while (resultSet1.next()) {
                listOrderFinish.add(new ObjectFinishOrder(resultSet1.getInt(1), resultSet1.getDate(2), resultSet1.getDouble(3),
                        resultSet1.getDouble(4)));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
    @FXML
    protected void bbb() {
        list.clear();
        listZak.clear();
        listVidNap.clear();
        listVidZak.clear();
        uploadElements();
        fiilGridTable();
        choiceFillCB();
        fillVidForProductCreatePane();
    }
    @FXML
    protected void valueTFSize() {
        if(ComBox.getValue().equals("Напиток")){
            CBcreteElemVid.getItems().clear();
            TFcreateElemSize.setPromptText("Объём: 0.25");
            for (int i = 0; i < listVidNap.size(); i++){
                CBcreteElemVid.getItems().add(listVidNap.get(i));
            }
        }
        else {
            TFcreateElemSize.setPromptText("Вес в кг: 0.25");
            CBcreteElemVid.getItems().clear();
            for (int i = 0; i < listVidZak.size(); i++){
                CBcreteElemVid.getItems().add(listVidZak.get(i));
            }
        }
    }
    @FXML
    protected void loadProduct() {

        String nameProd = CBProduct.getValue().toString();
        double sizeProd = 0;
        double priseProd = 0;
        int kolVoProd = 0;
        Blob imageProd = null;
        String vidProd = null;

        for (int i = 0; i < listVidNap.size(); i++)
        {
            CBVidProdFromEdit.getItems().add(listVidNap.get(i));
        }

        for (int i = 0; i < listVidZak.size(); i++)
        {
            CBVidProdFromEdit.getItems().add(listVidZak.get(i));
        }



        if(CBTypeTov.getValue().equals("Напиток")){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).name.equals(nameProd)){
                    sizeProd = list.get(i).size;
                    priseProd = list.get(i).prise;
                    kolVoProd = list.get(i).kolVo;
                    imageProd = list.get(i).image;
                    vidProd = list.get(i).vid;
                    break;
                }
            }
        }
        else {
            for(int i = 0; i < listZak.size(); i++){
                if(listZak.get(i).name.equals(nameProd)){
                    sizeProd = listZak.get(i).size;
                    priseProd = listZak.get(i).prise;
                    kolVoProd = listZak.get(i).kolVo;
                    imageProd = listZak.get(i).image;
                    vidProd = listZak.get(i).vid;
                    break;
                }
            }
        }

        TFeditElemName.setText(nameProd);
        TFeditElemSize.setText(String.valueOf(sizeProd));
        TFeditElemPrise.setText(String.valueOf(priseProd));
        TFeditElemKolVo.setText(String.valueOf(kolVoProd));
        imageEditPane.setImage(decodeImage(imageProd));
        editImagePozition(imageEditPane);
        CBVidProdFromEdit.setValue(vidProd);

        paneEdit.setDisable(false);
    }
    @FXML
    protected void editProdBut() {
        String prodName = null;
        String lastName = CBProduct.getValue().toString();
        double prodSize = 0;
        double prodPrise = 0;
        int prodKolVo = 0;
        FileInputStream fis = null;
        Blob imageProd = null;
        String prodVid = null;

        if(CBTypeTov.getValue().equals("Напиток")){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).name.equals(lastName)){
                    imageProd = list.get(i).image;
                    break;
                }
            }
        }
        else {
            for(int i = 0; i < listZak.size(); i++){
                if(listZak.get(i).name.equals(lastName)){
                    imageProd = listZak.get(i).image;
                    break;
                }
            }
        }


        String table = "";
        if(CBTypeTov.getValue().equals("Напиток")){
            table = "ProductNap";
        }else {
            table = "ProductZak";
        }
        PreparedStatement preparedStatement = null;

        try {
            prodName = TFeditElemName.getText();
            prodSize = Double.parseDouble(TFeditElemSize.getText());
            prodPrise = Double.parseDouble(TFeditElemPrise.getText());
            prodKolVo = Integer.parseInt(TFeditElemKolVo.getText());
            prodVid = CBVidProdFromEdit.getValue().toString();

        } catch (Exception e) {
            System.out.println("Ошибка в записи");
        }

        try {
            fis = new FileInputStream(selectedFile);
        } catch (Exception e) {
        }

        try {

            if (CBTypeTov.getValue().equals("Напиток")) {
                String quary = "UPDATE "+table+" SET nameNap = ?, sizeNap = ?, priseNap = ?, imageNap = ?, kolvo = ?, vidNap = ?  WHERE nameNap = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary);
            } else {
                String quary2 = "UPDATE "+table+" SET nameZak = ?, sizeZak = ?, priseZak = ?, imageZak = ?, kolvoZak = ?, vidZak = ?  WHERE nameZak = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary2);
            }
            preparedStatement.setString(1, prodName);
            preparedStatement.setDouble(2, prodSize);
            preparedStatement.setDouble(3, prodPrise);
            if(fis != null){
                preparedStatement.setBlob(4, fis);
            }else {
                preparedStatement.setBlob(4, imageProd);
            }
            preparedStatement.setInt(5, prodKolVo);
            preparedStatement.setString(6, prodVid);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

        paneEdit.setDisable(true);
    }
    @FXML
    protected void deleteBut() {

        String lastName = CBProduct.getValue().toString();
        String table = "";

        if(CBTypeTov.getValue().equals("Напиток")){
            table = "ProductNap";
        }else {
            table = "ProductZak";
        }

        PreparedStatement preparedStatement = null;

        try {

            if (CBTypeTov.getValue().equals("Напиток")) {
                String quary = "DELETE FROM "+table+" WHERE nameNap = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary);
            } else {
                String quary2 = "DELETE FROM "+table+" WHERE nameZak = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary2);
            }


            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

        paneEdit.setDisable(true);
    }
    @FXML
    protected void deleteBut2() {

        String lastName = VidDeleteEdit.getValue().toString();
        String table = "";

        if(CBTypeTov1.getValue().equals("Напиток")){
            table = "vidNapTable";
        }else {
            table = "vidZakTable";
        }

        PreparedStatement preparedStatement = null;

        try {

            if (CBTypeTov1.getValue().equals("Напиток")) {
                String quary = "DELETE FROM "+table+" WHERE vidNapChoice = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary);
            } else {
                String quary2 = "DELETE FROM "+table+" WHERE vidZakChoice = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary2);
            }


            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

        paneEdit.setDisable(true);
    }
    @FXML
    protected void fillTableOrderBut() {
        String poduct;
        int kolVo = Integer.parseInt(tfAddToOrderkolVo.getText());
        int num = listOrderStruct.size() + 1;
        LocalDate date = LocalDate.now();
        try {
            for (int i = 0; i < list.size(); i++) {
                poduct = list.get(i).name;
                if (poduct.equals(nameFromOrder.getText())) {
                    listOrderStruct.add(new ObjectOrder(num, poduct, kolVo,kolVo * list.get(i).prise, date));
                    fillTable(listOrderStruct, tbViewOrderStr,tbCollumnID, tbCollumnName, tbCollumnKolVo, tbCollumnPrise);
                    break;
                }
            }
            for (int i = 0; i < listZak.size(); i++) {
                poduct = listZak.get(i).name;
                if (poduct.equals(nameFromOrder.getText())) {
                    listOrderStruct.add(new ObjectOrder(num, poduct, kolVo, kolVo * listZak.get(i).prise, date));
                    fillTable(listOrderStruct, tbViewOrderStr,tbCollumnID, tbCollumnName, tbCollumnKolVo, tbCollumnPrise);
                    break;
                }
            }
        } catch (Exception ignored) {}


    }
    @FXML
    protected void delToMenuBut(){

        int idTime = Integer.parseInt(tfIDdelite.getText());
        listOrderStruct.remove(idTime - 1);
        while (idTime < listOrderStruct.size() + 1){
            int idNew = idTime - 1;
            listOrderStruct.set(idTime - 1, new ObjectOrder(idTime, listOrderStruct.get(idNew).name, listOrderStruct.get(idNew).kolVo,listOrderStruct.get(idNew).prise,listOrderStruct.get(idNew).date));
            idTime++;
        }

        fillTable(listOrderStruct, tbViewOrderStr,tbCollumnID ,tbCollumnName, tbCollumnKolVo, tbCollumnPrise);

    }

    public void finishOrderAddElem(){
        int num = listOrderFinish.size() + 1;
        double priseGet = 0;
        double priseMid = 0;
        java.util.Date dt = new java.util.Date();
        Date date = new Date(dt.getTime());
        try {
            for (int i = 0; i < listOrderStruct.size(); i++) {
                priseGet = priseGet + listOrderStruct.get(i).prise;
            }
            priseMid = priseGet/listOrderStruct.size();;

            listOrderFinish.add(new ObjectFinishOrder(num, date, priseMid, priseGet));
            fillTableOrderFinish(listOrderFinish, tbViewFinish, tbNumOrder, tbDateAndTimeOrder, tbMidPriseOrder, tbGenPrise);
        } catch (Exception ignored) {}

    }
    @FXML
    protected void updateOrderTable(){
        fillOrderTableToSql();
    }

    public void fillOrderTableToSql(){
        try {
            String quary = "DELETE FROM orderFinish;";
            PreparedStatement preparedStatement = connection.prepareStatement(quary);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Успешно");
        } catch (SQLException e) {
            System.out.println("Неуспешно");
            printSQLException(e);
        }

        String lastName = null;
        double prodSize = 0;
        double prodPrise = 0;
        int prodKolVo = 0;
        Blob imageProd = null;
        String prodVid = null;
        String tableName = null;
        PreparedStatement preparedStatement = null;
        try {
            for(int i = 0; i < list.size(); i++){
                for (int j = 0; j< listOrderStruct.size(); j++) {
                    if(list.get(i).name.equals(listOrderStruct.get(j).name)){
                        lastName = list.get(i).name;
                        imageProd = list.get(i).image;
                        prodSize = list.get(i).size;
                        prodPrise = list.get(i).prise;
                        prodKolVo = list.get(i).kolVo - listOrderStruct.get(j).kolVo;
                        prodVid = list.get(i).vid;
                        tableName = "ProductNap";

                        try {
                            String quary = "UPDATE " + tableName + " SET nameNap = ?, sizeNap = ?, priseNap = ?, " +
                                        "imageNap = ?, kolvo = ?, vidNap = ?  WHERE nameNap = '" + lastName + "'";
                            preparedStatement = connection.prepareStatement(quary);
                            preparedStatement.setString(1, lastName);
                            preparedStatement.setDouble(2, prodSize);
                            preparedStatement.setDouble(3, prodPrise);
                            preparedStatement.setBlob(4, imageProd);
                            preparedStatement.setInt(5, prodKolVo);
                            preparedStatement.setString(6, prodVid);

                            try {
                                int rows = preparedStatement.executeUpdate();
                                list.clear();
                                listZak.clear();
                                uploadElements();

                            } catch (SQLException e) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Не хватает товара");
                                alert.showAndWait();
                            }
                            if (prodKolVo > 0) {
                                ccc(j);
                                finishOrderAddElem();
                                fillTableOrderFinishToSql();
                            }
                        } catch (SQLException e) {
                            System.out.println("Неуспешно");
                            printSQLException(e);
                        }
                    }
                }
            }
        } catch (Exception ignored) { }

        try {
            for(int i = 0; i < listZak.size(); i++){
                for (int j = 0; j< listOrderStruct.size(); j++) {
                    if(listZak.get(i).name.equals(listOrderStruct.get(j).name)){
                        lastName = listZak.get(i).name;
                        imageProd = listZak.get(i).image;
                        prodSize = listZak.get(i).size;
                        prodPrise = listZak.get(i).prise;
                        prodKolVo = listZak.get(i).kolVo - listOrderStruct.get(j).kolVo;
                        prodVid = listZak.get(i).vid;
                        tableName = "ProductZak";

                        try {
                            String quary2 = "UPDATE " + tableName + " SET nameZak = ?, sizeZak = ?, priseZak = ?," +
                                        " imageZak = ?, kolvoZak = ?, vidZak = ?  WHERE nameZak = '" + lastName + "'";
                            preparedStatement = connection.prepareStatement(quary2);
                            preparedStatement.setString(1, lastName);
                            preparedStatement.setDouble(2, prodSize);
                            preparedStatement.setDouble(3, prodPrise);
                            preparedStatement.setBlob(4, imageProd);
                            preparedStatement.setInt(5, prodKolVo);
                            preparedStatement.setString(6, prodVid);

                            try {
                                int rows = preparedStatement.executeUpdate();
                                list.clear();
                                listZak.clear();
                                uploadElements();
                            } catch (SQLException e) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Не хватает товара");
                                alert.showAndWait();
                            }
                            if (prodKolVo>0) {
                                ccc(j);
                                finishOrderAddElem();
                                fillTableOrderFinishToSql();
                            }
                        } catch (SQLException e) {
                            System.out.println("Неуспешно");
                            printSQLException(e);
                        }
                    }
                }
            }
        } catch (Exception ignored) { }


    }
    public void ccc(int j){
        int idOrderProd = 0;
        String nameOrderProd = null;
        int kolVoOrderProd = 0;
        double priseOrderProd = 0;
        LocalDate dateOrderProd = null;
            try {
                idOrderProd = listOrderStruct.get(j).id;
                nameOrderProd = listOrderStruct.get(j).name;
                kolVoOrderProd = listOrderStruct.get(j).kolVo;
                priseOrderProd = listOrderStruct.get(j).prise;
                dateOrderProd = listOrderStruct.get(j).date;
            } catch (Exception e) {
                System.out.println("Ошибка в записи");
            }
            try {
                String quary = "insert orderStructure values (?,?,?,?,?);";
                PreparedStatement preparedStatement1 = connection.prepareStatement(quary);
                preparedStatement1.setInt(1, idOrderProd);
                preparedStatement1.setString(2, nameOrderProd);
                preparedStatement1.setDouble(3, priseOrderProd);
                preparedStatement1.setInt(4, kolVoOrderProd);
                preparedStatement1.setDate(5, Date.valueOf(dateOrderProd));

                int rows = preparedStatement1.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Неуспешно");
                printSQLException(e);
            }

    }

    //region StyleButton
    @FXML
    protected void AnalitButMenuStyle() {
        StyleButton(analitButMenu);
    }

    @FXML
    protected void ProductButMenuStyle() {
        StyleButton(rpoductButMenu);
    }

    @FXML
    protected void SaleButMenuStyle() {
        StyleButton(salesButMenu);
    }

    @FXML
    protected void CompanyButMenuStyle() {
        StyleButton(сompanyButMenu);
    }

    public void StyleButton(Button nameBut) {
        nameBut.setOnMouseEntered(event -> {
            nameBut.setStyle("-fx-background-color: #0097A3");
        });
        nameBut.setOnMouseExited(event -> nameBut.setStyle("-fx-background-color: #F5F5F5"));
    }

    //endregion

    //region SwitchMenuElements
    @FXML
    protected void openMenuAnalit() {

        analit.setVisible(true);
        product.setVisible(false);
        salePane.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);

    }

    @FXML
    protected void openMenuProduct() {

        analit.setVisible(false);
        product.setVisible(true);
        salePane.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuSales() {

        analit.setVisible(false);
        product.setVisible(false);
        salePane.setVisible(true);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuCompany() {

        analit.setVisible(false);
        product.setVisible(false);
        salePane.setVisible(false);
        comapny.setVisible(true);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuCreateElem() {

        analit.setVisible(false);
        product.setVisible(false);
        salePane.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(true);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuEditElem() {

        analit.setVisible(false);
        product.setVisible(false);
        salePane.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(true);
    }

    //endregion

    //region Не трогать
    public void fillTable(ObservableList obslist,TableView tableView,TableColumn tableColumnId,TableColumn tableColumnName,
                          TableColumn tableColumnKolVo,TableColumn tableColumnPrise){
        try {
            tableColumnId.setCellValueFactory(new PropertyValueFactory<ObjectOrder, String>("Id"));
            tableColumnName.setCellValueFactory(new PropertyValueFactory<ObjectOrder, String>("Name"));
            tableColumnPrise.setCellValueFactory(new PropertyValueFactory<ObjectOrder, Integer>("Prise"));
            tableColumnKolVo.setCellValueFactory(new PropertyValueFactory<ObjectOrder, Integer>("KolVo"));

            tableView.setItems(obslist);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }
    public void fillTableOrderFinish(ObservableList obslist,TableView tableView,TableColumn tableColumnId,TableColumn tableColumnDate, TableColumn tableColumnPriseMid,
                          TableColumn tableColumnPriseGen){
        try {
            tableColumnId.setCellValueFactory(new PropertyValueFactory<ObjectFinishOrder, Integer>("Id"));
            tableColumnDate.setCellValueFactory(new PropertyValueFactory<ObjectFinishOrder, Date>("Date"));
            tableColumnPriseMid.setCellValueFactory(new PropertyValueFactory<ObjectFinishOrder, Double>("PriseMid"));
            tableColumnPriseGen.setCellValueFactory(new PropertyValueFactory<ObjectFinishOrder, Double>("PriseGen"));

            tableView.setItems(obslist);
        } catch (Exception e) {
            System.out.println("Ошибка");
        }

    }

    public void fillTableOrderFinishToSql(){

        double genPrise = 0;
        double midPrise = 0;
        Date date = null;

        PreparedStatement preparedStatement = null;
        try {
            for (int i = 0; i < listOrderFinish.size(); i++) {

                genPrise = listOrderFinish.get(i).priseGen;
                midPrise = listOrderFinish.get(i).priseMid;
                date = listOrderFinish.get(i).date;
                try {
                    String quary = "insert orderFinish values (?,?,?,?);";
                    preparedStatement = connection.prepareStatement(quary);
                    preparedStatement.setInt(1, i+1);
                    preparedStatement.setDate(2, date);
                    preparedStatement.setDouble(3, midPrise);
                    preparedStatement.setDouble(4, genPrise);

                    int rows = preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("Неуспешно");
                    printSQLException(e);
                }


            }
        } catch (Exception e) {
            System.out.println("Неуспешно");
        }
    }
    public void fiilGridTable() {

        for (int i = 0; i <= 1; i++){
            if(i == 0){
                scrolPaneNap.setContent(fillTableNapAndZak(list,"Объём: "));
            }else {
                scrolPaneZak.setContent(fillTableNapAndZak(listZak,"Вес в кг: "));
            }
        }


    }

    public GridPane fillTableNapAndZak(List listForChoice, String sizeNapOrZak){
        GridPane gridPane = new GridPane();
        gridPane.getChildren().clear();
        gridPane.setPrefSize(987, 1534);
        int j = 0;
        int k = 0;

        for (int i = 1; i <= listForChoice.size(); i++) {

            String name;
            double size;
            double prise;
            Blob image;
            int kolVo;
            if (listForChoice == list) {
                name = list.get(i - 1).name;
                size = list.get(i - 1).size;
                prise = list.get(i - 1).prise;
                image = list.get(i - 1).image;
                kolVo = list.get(i - 1).kolVo;
            } else {
                name = listZak.get(i - 1).name;
                size = listZak.get(i - 1).size;
                prise = listZak.get(i - 1).prise;
                image = listZak.get(i - 1).image;
                kolVo = listZak.get(i - 1).kolVo;
            }
            Pane paneBack = new Pane();
            paneBack.setPrefSize(247, 307);

            Pane paneMain = new Pane();
            paneMain.setPrefSize(232, 289);
            paneMain.setLayoutY(10);
            paneMain.setLayoutX(8);
            paneMain.setStyle(
                    "-fx-padding: 0;" +
                            "-fx-background-color: #E0E0E0;" +
                            "-fx-border-color: #CFCFCF;"
            );

            Label nameNap = new Label();
            nameNap.setPrefSize(208, 30);
            nameNap.setStyle("-fx-font-size: 20px;" +
                    "-fx-alignment: BOTTOM_CENTER;");
            nameNap.setText(name);
            nameNap.setLayoutX(10);
            nameNap.setLayoutY(185);

            Label sizeNap = new Label();
            sizeNap.setPrefSize(129, 17);
            sizeNap.setStyle("-fx-font-size: 12px;" +
                    "-fx-opacity: 0.46;" +
                    "-fx-alignment: BOTTOM_CENTER;");
            sizeNap.setText(sizeNapOrZak + size);
            sizeNap.setLayoutX(50);
            sizeNap.setLayoutY(215);

            Label KolVoNap = new Label();
            KolVoNap.setPrefSize(154, 17);
            KolVoNap.setStyle("-fx-font-size: 12px;" +
                    "-fx-opacity: 0.46;" +
                    "-fx-alignment: BOTTOM_CENTER;");
            KolVoNap.setText("Количество: " + kolVo);
            KolVoNap.setLayoutX(40);
            KolVoNap.setLayoutY(235);

            Label priseNap = new Label();
            priseNap.setPrefSize(191, 27);
            priseNap.setStyle("-fx-font-size: 18px;" +
                    "-fx-alignment: BOTTOM_CENTER;");
            priseNap.setText("Цена: " + prise);
            priseNap.setLayoutX(20);
            priseNap.setLayoutY(250);

            StackPane paneForImage = new StackPane();
            paneForImage.setPrefSize(244,174);

            ImageView imView = new ImageView(decodeImage(image));
            imView.setPreserveRatio(true);
            imView.setFitWidth(244);
            imView.setFitHeight(174);

            paneForImage.getChildren().add(imView);
            StackPane.setAlignment(imView, Pos.TOP_CENTER);

            paneMain.getChildren().addAll(nameNap, sizeNap, KolVoNap, priseNap, paneForImage);

            paneBack.getChildren().add(paneMain);

            if (i % 5 != 0) {
                gridPane.add(paneBack, k, j);
                k++;
            } else {
                j++;
                k = 0;
                gridPane.add(paneBack, k, j);

            }


        }

        return gridPane;
    }

    public WritableImage decodeImage(Blob imageForDecode){

        byte[] bytes = new byte[0];
        try {
            bytes = imageForDecode.getBytes(1, (int) imageForDecode.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedImage img;
        try {
            img = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WritableImage wr = null;
        if (img != null) {
            wr = new WritableImage(img.getWidth(), img.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    pw.setArgb(x, y, img.getRGB(x, y));
                }
            }
        }

        return wr;
    }

    public void editImagePozition(ImageView imageViewEdit){

        Image img = imageViewEdit.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageViewEdit.getFitWidth() / img.getWidth();
            double ratioY = imageViewEdit.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageViewEdit.setX((imageViewEdit.getFitWidth() - w) / 2);
            imageViewEdit.setY((imageViewEdit.getFitHeight() - h) / 2);
        }
    }
    //endregion
}
