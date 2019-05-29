/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zackatoo.splineanimator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.zackatoo.splineanimator.animation.Point;
import com.zackatoo.splineanimator.animation.curves.PiecewiseCurve;
import com.zackatoo.splineanimator.utils.DraggableCircle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

/**
 *
 * @author Zackatoo
 */
public class Controller implements Initializable
{
    private static final double DEFAULT_STEP = 0.03;
    private static final double SLIDER_POWER = 0.4;

    private static final int MOVIE_WIDTH = 1920;
    private static final int MOVIE_HEIGHT = 1080;
    private static PointConverter pointConverter;

    @FXML private AnchorPane anchorPane;

    @FXML private TextField txt_stepField;
    @FXML private Slider sldr_stepSlider;
    private double currentStep = DEFAULT_STEP;
    private ArrayList<Node> pointSpecificNodes = new ArrayList<>();

    @FXML private ImageView imgv_showImage;
    @FXML private Label lbl_renderTime;
    private MarvinImage currentImage;

    private PiecewiseCurve curve;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pointConverter = new PointConverter();
        txt_stepField.setText(Double.toString(DEFAULT_STEP));
        sldr_stepSlider.adjustValue(DEFAULT_STEP);
        curve = ImageTest.getDefaultCurve();
        drawDefaultImage();
        createPointFields();
    }

    private void createPointFields()
    {
        anchorPane.getChildren().removeAll(pointSpecificNodes);
        pointSpecificNodes = new ArrayList<>();

        ArrayList<Point> points = curve.getPoints();
        Point start = new Point(1000, 50, 1);
        Point fieldDimensions = new Point(74, 26 ,1);
        Point fieldSpacing = new Point(30, 10, 1);

        for (int i = 0; i < points.size(); i++)
        {
            int x = points.get(i).x;
            int y = points.get(i).y;

            Circle currentCircle = new Circle();
            CheckBox currentCheckBox = new CheckBox();
            TextField currentX = new TextField();
            TextField currentY = new TextField();
            currentX.setText(Integer.toString(x));
            currentY.setText(Integer.toString(y));
            currentCircle.setCenterX(pointConverter.movie2screenX(x));
            currentCircle.setCenterY(pointConverter.movie2screenY(y));

            currentX.setLayoutX(start.x);
            currentX.setLayoutY(start.y + i * (fieldDimensions.y + fieldSpacing.y));
            currentY.setLayoutX(start.x + fieldDimensions.x + fieldSpacing.x);
            currentY.setLayoutY(currentX.getLayoutY());
            currentCheckBox.setLayoutX(start.x + 2 * (fieldDimensions.x + fieldSpacing.x));
            currentCheckBox.setLayoutY(start.y + i * (fieldDimensions.y + fieldSpacing.y));

            currentX.setPrefWidth(fieldDimensions.x);
            currentX.setPrefHeight(fieldDimensions.y);
            currentY.setPrefWidth(fieldDimensions.x);
            currentY.setPrefHeight(fieldDimensions.y);
            currentCircle.setRadius(5);

            Integer index = i;
            currentX.setOnKeyTyped(event -> handle_modifyPointFields(currentX, currentCircle, index, true));
            currentY.setOnKeyTyped(event -> handle_modifyPointFields(currentY, currentCircle, index, false));
            DraggableCircle draggableCircle = new DraggableCircle(currentCircle);
            draggableCircle.setDragUpdate((_x, _y) -> handle_modifyCircle(currentX, currentY, _x, _y, index));
            currentCheckBox.setOnAction(event -> handle_updatePointCheckBox(index));

            anchorPane.getChildren().addAll(currentX, currentY, currentCircle, currentCheckBox);

            pointSpecificNodes.add(currentX);
            pointSpecificNodes.add(currentY);
            pointSpecificNodes.add(currentCheckBox);
            pointSpecificNodes.add(currentCircle);
        }
    }

    private void drawDefaultImage()
    {
        long start = System.currentTimeMillis();
        currentImage = ImageTest.getDefaultImage();
        curve.draw(currentImage, currentStep);
        currentImage.update();
        Image fxImage = SwingFXUtils.toFXImage(currentImage.getBufferedImage(), null);
        imgv_showImage.setImage(fxImage);
        lbl_renderTime.setText("Render time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @FXML public void handle_updateStepSlider()
    {
        currentStep = Math.pow(sldr_stepSlider.getValue(), 1 / SLIDER_POWER);
        txt_stepField.setText(Double.toString(currentStep));
        drawDefaultImage();
    }

    @FXML public void handle_updateStepField()
    {
        currentStep = Double.parseDouble(txt_stepField.getText());
        if (currentStep > sldr_stepSlider.getMax())
        {
            currentStep = sldr_stepSlider.getMax();
            txt_stepField.setText(Double.toString(currentStep));
        }
        if (currentStep < sldr_stepSlider.getMin())
        {
            currentStep = sldr_stepSlider.getMin();
            txt_stepField.setText(Double.toString(currentStep));
        }

        sldr_stepSlider.adjustValue(Math.pow(currentStep, SLIDER_POWER));
        drawDefaultImage();
    }

    @FXML public void handle_importFromFile()
    {
        TextInputDialog getFileName = new TextInputDialog();
        getFileName.setTitle("Import from File");
        getFileName.setHeaderText("Enter file name");
        getFileName.setContentText("");
        Optional<String> fileName = getFileName.showAndWait();
        if (fileName.isPresent())
        {
            try
            {
                curve = new PiecewiseCurve(fileName.get());
                createPointFields();
                drawDefaultImage();
            }
            catch (Exception e)
            {
                Main.alertUser(e.getMessage());
            }
        }
    }

    @FXML public void handle_exportToFile()
    {
        MarvinImageIO.saveImage(currentImage, "export.png");
    }

    private void handle_modifyPointFields(TextField field, Circle circle, int index, boolean x)
    {
        Point point = curve.getPoints().get(index);
        int val;

        try
        {
            val = Integer.parseInt(field.getText());
        }
        catch (NumberFormatException e)
        {
            try
            {
                val = (int) Double.parseDouble(field.getText());
            }
            catch (NumberFormatException ex)
            {
                return;
            }
            field.setText(Integer.toString(val));
        }

        if (x)
        {
            point.x = val;
            circle.setCenterX(pointConverter.movie2screenX(val));
        }
        else
        {
            point.y = val;
            circle.setCenterY(pointConverter.movie2screenY(val));
        }

        curve.updateSplines();
        drawDefaultImage();
    }

    private void handle_modifyCircle(TextField fieldX, TextField fieldY, int x, int y, int index)
    {
        Point point = curve.getPoints().get(index);
        point.x = pointConverter.screen2movieX(x);
        point.y = pointConverter.screen2movieY(y);
        fieldX.setText(Integer.toString(point.x));
        fieldY.setText(Integer.toString(point.y));

        curve.updateSplines();
        drawDefaultImage();

    }

    private void handle_updatePointCheckBox(int index)
    {
        curve.toggleSharpness(index);
        drawDefaultImage();
    }

    private class PointConverter
    {
        Point movie2screen(Point moviePoint)
        {
            int x = (int)(moviePoint.x * MOVIE_WIDTH / imgv_showImage.getFitWidth() + imgv_showImage.getX());
            int y = (int)(moviePoint.y * MOVIE_HEIGHT / imgv_showImage.getFitHeight() + imgv_showImage.getY());
            return new Point(x, y, 1);
        }

        Point screen2movie(Point screenPoint)
        {
            int x = (int)((screenPoint.x - imgv_showImage.getX()) * MOVIE_WIDTH / imgv_showImage.getFitWidth());
            int y = (int)((screenPoint.y - imgv_showImage.getX()) * MOVIE_HEIGHT / imgv_showImage.getFitHeight());
            return new Point(x,y, 1);
        }

        int movie2screenX(int x)
        {
            return (int)(x * imgv_showImage.getFitWidth() / MOVIE_WIDTH + imgv_showImage.getLayoutX());
        }

        int movie2screenY(int y)
        {
            return (int)(y * imgv_showImage.getFitHeight() / MOVIE_HEIGHT + imgv_showImage.getLayoutY());
        }

        int screen2movieX(int x)
        {
            return (int)((x - imgv_showImage.getLayoutX()) * MOVIE_WIDTH / imgv_showImage.getFitWidth());
        }

        int screen2movieY(int y)
        {
            return (int)((y - imgv_showImage.getLayoutY()) * MOVIE_HEIGHT / imgv_showImage.getFitHeight());
        }
    }
}
