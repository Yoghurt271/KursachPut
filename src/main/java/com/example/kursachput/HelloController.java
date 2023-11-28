//region ImportLibraris
package com.example.kursachput;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executor;
import java.lang.Object;
import java.util.Base64;
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
    private Pane upMenu, analit, product, sales, comapny, CreateElemPane;

    @FXML
    private GridPane gridNap;

    @FXML
    private Button analitButMenu, rpoductButMenu, salesButMenu, сompanyButMenu, openCreatePane, addImage, bb, writinForBD;

    @FXML
    private ImageView imageCreatePane;

    @FXML
    private TextField TFcreateElemName, TFcreateElemSize, TFcreateElemPrise, TFcreateElemKolVo;

    @FXML
    private ScrollPane scrolPaneNap;

    @FXML
    private AnchorPane anchorPaneNap;

    ArrayList<ObjectProd> list = new ArrayList<ObjectProd>();

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

    }

    @FXML
    protected void openMenuProduct() {

        analit.setVisible(false);
        product.setVisible(true);
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);

    }

    @FXML
    protected void openMenuSales() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(true);
        comapny.setVisible(false);
        CreateElemPane.setVisible(false);

    }

    @FXML
    protected void openMenuCompany() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(false);
        comapny.setVisible(true);
        CreateElemPane.setVisible(false);
    }

    @FXML
    protected void openMenuCreateElem() {

        analit.setVisible(false);
        product.setVisible(false);
        sales.setVisible(false);
        comapny.setVisible(false);
        CreateElemPane.setVisible(true);
    }

    //endregion

    @FXML
    protected void SendToServ() {

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
            String quary = "insert ProductNap values (?,?,?,?,?);";
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
            imageCreatePane.setImage(image);

            Image img = imageCreatePane.getImage();
            if (img != null) {
                double w = 0;
                double h = 0;

                double ratioX = imageCreatePane.getFitWidth() / img.getWidth();
                double ratioY = imageCreatePane.getFitHeight() / img.getHeight();

                double reducCoeff = 0;
                if (ratioX >= ratioY) {
                    reducCoeff = ratioY;
                } else {
                    reducCoeff = ratioX;
                }

                w = img.getWidth() * reducCoeff;
                h = img.getHeight() * reducCoeff;

                imageCreatePane.setX((imageCreatePane.getFitWidth() - w) / 2);
                imageCreatePane.setY((imageCreatePane.getFitHeight() - h) / 2);


            }
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных успешно установлено!");
            uploadElements();
//            fiilGridTable();
        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к базе данных:");
            printSQLException(e);
        }
    }

    public void uploadElements() {
        String query = "SELECT * FROM ProductNap";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new ObjectProd(resultSet.getString(1), resultSet.getDouble(2), resultSet.getDouble(3),
                        resultSet.getBlob(4), resultSet.getInt(5)));
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    @FXML
    protected void bbb() {
        fiilGridTable();
    }

    public void fiilGridTable() {

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(987, 1534);
        int j = 0;
        int k = 0;

        for (int i = 1; i <= list.size(); i++) {

            String name = list.get(i - 1).name;
            double size = list.get(i - 1).size;
            double prise = list.get(i - 1).prise;
            Blob image = list.get(i - 1).image;
            int kolVo = list.get(i - 1).kolVo;
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
            sizeNap.setText("Объём: " + size);
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

            byte[] bytes = new byte[0];
            try {
                bytes = image.getBytes(1, (int) image.length());
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

            StackPane paneForImage = new StackPane();
            paneForImage.setPrefSize(244,174);

            ImageView imView = new ImageView(wr);
            imView.setPreserveRatio(true);
            imView.setFitWidth(244);
            imView.setFitHeight(174);

            paneForImage.getChildren().add(imView);
            paneForImage.setAlignment(imView, Pos.TOP_CENTER);

            paneMain.getChildren().addAll(nameNap, sizeNap, KolVoNap, priseNap, paneForImage);

            paneBack.getChildren().add(paneMain);

            if (i % 5 != 0) {
                gridPane.add(paneBack, k, j);
                k++;
            } else if (i % 5 == 0) {
                j++;
                k = 0;
                gridPane.add(paneBack, k, j);

            }


        }

        scrolPaneNap.setContent(gridPane);

    }

}
