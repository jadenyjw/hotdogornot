package gui;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import ai.*;
import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;

/**
 * Created by jaden on 5/10/17.
 */
public class GameController implements Initializable{

    @FXML protected Canvas surface;
    @FXML protected Label label;
    protected GraphicsContext gc;
    protected NeuralNet net;

    @FXML
    public void clear(){
        gc.clearRect(0, 0, surface.getWidth(), surface.getHeight());
    }


    public void judge() {
        Platform.runLater(new Runnable() {
            public void run() {
                WritableImage img = surface.snapshot(null, null);
                BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);


                try {
                    if(net.checkDrawing(resize(bImage, 225, 225), 108)){

                        label.setText("Hot Dog!");
                    }
                    else{
                        label.setText("Not hot dog...");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        gc = surface.getGraphicsContext2D();
        surface.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        if(e.isPrimaryButtonDown()){
                            gc.fillOval(e.getX(), e.getY(), 10, 10);
                        }
                        else if(e.isSecondaryButtonDown()){
                            gc.clearRect(e.getX(), e.getY(), 10, 10);
                        }

                    }
                });
        surface.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e) {
                        if(e.isPrimaryButtonDown()){
                            gc.fillOval(e.getX(), e.getY(), 10, 10);
                        }
                        else if(e.isSecondaryButtonDown()){
                            gc.clearRect(e.getX(), e.getY(), 10, 10);
                        }

                    }
                });
        surface.getParent().addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        if(e.getCode() == KeyCode.C) {
                            clear();
                        }

                    }
                });
        try{
            net = new NeuralNet();
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                            judge();

                    }
                }, 0, 3000);

    }
}
