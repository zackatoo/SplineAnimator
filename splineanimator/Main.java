/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zackatoo.splineanimator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application
{
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception
    {
        this.mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Document.fxml"));
        
        Scene scene = new Scene(root);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static int getStageWidth()
    {
        return (int)mainStage.getWidth();
    }

    public static int getStageHeight()
    {
        return (int)mainStage.getHeight();
    }

    public static void alertUser(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
