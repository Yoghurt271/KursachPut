//region ImportLibraris
package com.example.kursachput;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;

import java.sql.Connection;

import static java.nio.file.Files.write;
//endregion

public class HelloController implements Initializable {

    //region ConnectToBD
    String url = "jdbc:mysql://192.168.0.179:3306/CursachPutOdMi";

    String user = "Yoghurt";
    String password = "Kaka228";

    private Connection connection;

    public File selectedFile;

    public HelloController() {
    }

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

    //endregion

    //region CreateElements
    @FXML
    private BorderPane changePane;

    @FXML
    private Pane upMenu, analit, product, sales, comapny, CreateElemPane, paneEdit;

    @FXML
    private GridPane gridNap;

    @FXML
    private Button analitButMenu, rpoductButMenu, salesButMenu, сompanyButMenu,
            openCreatePane, addImage, bb, openEditPane, loadPr, addImageEdit, editForBD, deleteBut;

    @FXML
    private ImageView imageCreatePane, imageEditPane;

    @FXML
    private TextField TFcreateElemName, TFcreateElemSize, TFcreateElemPrise, TFcreateElemKolVo,
            TFeditElemName, TFeditElemSize, TFeditElemPrise, TFeditElemKolVo;

    @FXML
    private ScrollPane scrolPaneNap, scrolPaneZak;

    @FXML
    private AnchorPane anchorPaneNap;

    @FXML
    private ComboBox ComBox, CBTypeTov, CBProduct;

    ArrayList<ObjectProd> list = new ArrayList<ObjectProd>();

    ArrayList<ObjectProd> listZak = new ArrayList<ObjectProd>();

    //endregion

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
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);

    }

    @FXML
    protected void openMenuProduct() {

        analit.setVisible(false);
        product.setVisible(true);
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuSales() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(true);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuCompany() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(false);
        comapny.setVisible(true);
        CreateElemPane.setVisible(false);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuCreateElem() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(true);
        changePane.setVisible(false);
    }

    @FXML
    protected void openMenuEditElem() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);
        changePane.setVisible(true);
    }

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

    public void SendNapOrZak(String tableName){

        String prodName = null;
        double prodSize = 0;
        double prodPrise = 0;
        int prodKolVo = 0;
        FileInputStream fis = null;


        try {
            prodName = TFcreateElemName.getText();
            prodSize = Double.parseDouble(TFcreateElemSize.getText());
            prodPrise = Double.parseDouble(TFcreateElemPrise.getText());
            prodKolVo = Integer.parseInt(TFcreateElemKolVo.getText());
            fis = new FileInputStream(selectedFile);
        } catch (Exception e) {
            System.out.println("Ошибка в записи");
        }


        try {
            String quary = "insert " + tableName + " values (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(quary);
            preparedStatement.setString(1, prodName);
            preparedStatement.setDouble(2, prodSize);
            preparedStatement.setDouble(3, prodPrise);
            preparedStatement.setBlob(4, fis);
            preparedStatement.setInt(5, prodKolVo);


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных успешно установлено!");
            uploadElements();
            fiilGridTable();
            fillCB();
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
            printSQLException(e);
        }
    }

    public void fillCB(){
        ComBox.getItems().addAll("Напиток", "Закуска");
        ComBox.getSelectionModel().select("Напиток");
        CBTypeTov.getItems().addAll("Напиток", "Закуска");
        CBTypeTov.getSelectionModel().select("Напиток");

        for (int i = 0; i < list.size(); i++) {
            CBProduct.getItems().addAll(list.get(i).name);
        }

    }

    @FXML
    protected void choiceFillCB() {
        if(CBTypeTov.getValue().equals("Напиток")) {
            CBProduct.getItems().clear();
            for (int i = 0; i < list.size(); i++) {
                CBProduct.getItems().addAll(list.get(i).name);

            }
        }else {
            CBProduct.getItems().clear();
            for (int i = 0; i < listZak.size(); i++) {
                CBProduct.getItems().addAll(listZak.get(i).name);

            }
        }
    }

    public void uploadElements() {
        String query = "SELECT * FROM ProductNap";
        String queryZak = "SELECT * FROM ProductZak";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new ObjectProd(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3),
                        resultSet.getBlob(4), resultSet.getInt(5)));
            }

            ResultSet rs = statement.executeQuery(queryZak);
            while (rs.next()) {
                listZak.add(new ObjectProd(rs.getString(1), rs.getDouble(2), rs.getDouble(3),
                        rs.getBlob(4), rs.getInt(5)));
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
        uploadElements();
        fiilGridTable();
        choiceFillCB();
    }
    @FXML
    protected void valueTFSize() {
        if(ComBox.getValue().equals("Напиток")){
            TFcreateElemSize.setPromptText("Объём: 0.25");
        }
        else {
            TFcreateElemSize.setPromptText("Вес в кг: 0.25");
        }
    }

    @FXML
    protected void loadProduct() {

        String nameProd = CBProduct.getValue().toString();
        double sizeProd = 0;
        double priseProd = 0;
        int kolVoProd = 0;
        Blob imageProd = null;
        if(CBTypeTov.getValue().equals("Напиток")){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).name.equals(nameProd)){
                    sizeProd = list.get(i).size;
                    priseProd = list.get(i).prise;
                    kolVoProd = list.get(i).kolVo;
                    imageProd = list.get(i).image;
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

        paneEdit.setDisable(false);
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

    @FXML
    protected void editProdBut() {
        String prodName = null;
        String lastName = CBProduct.getValue().toString();
        double prodSize = 0;
        double prodPrise = 0;
        int prodKolVo = 0;
        FileInputStream fis = null;
        Blob imageProd = null;

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

            

        } catch (Exception e) {
            System.out.println("Ошибка в записи");
        }

        try {
            fis = new FileInputStream(selectedFile);
        } catch (Exception e) {
        }

        try {

            if (CBTypeTov.getValue().equals("Напиток")) {
                String quary = "UPDATE "+table+" SET nameNap = ?, sizeNap = ?, priseNap = ?, imageNap = ?, kolvo = ?  WHERE nameNap = '" + lastName + "'";
                preparedStatement = connection.prepareStatement(quary);
            } else {
                String quary2 = "UPDATE "+table+" SET nameZak = ?, sizeZak = ?, priseZak = ?, imageZak = ?, kolvoZak = ?  WHERE nameZak = '" + lastName + "'";
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

}
