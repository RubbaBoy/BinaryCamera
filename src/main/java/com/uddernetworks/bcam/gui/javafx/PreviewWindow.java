package com.uddernetworks.bcam.gui.javafx;

import com.uddernetworks.bcam.camera.CameraHandler;
import com.uddernetworks.bcam.gui.UserInterface;
import com.uddernetworks.bcam.image.sectioner.ImageSection;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class PreviewWindow implements UserInterface, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewWindow.class);

    @FXML
    private ImageView preview;

    @FXML
    private Button lightsOn;

    @FXML
    private Button lightsOff;

    @FXML
    private Label toggleStatus;

    private final Stage stage;
    private final Parent root;
    private final CameraHandler handler;
    private final SectionManager sectionManager;
    private final Runnable lightsOnCallback;
    private final Runnable lightsOffCallback;
    private final Set<ImageSection> sectionBuffer = new HashSet<>();
    private int listenerId;
    private BufferedImage lastImage;

    public PreviewWindow(Stage stage, CameraHandler handler, SectionManager sectionManager, Runnable lightsOnCallback, Runnable lightsOffCallback) throws IOException {
        this.stage = stage;
        this.handler = handler;
        this.sectionManager = sectionManager;
        this.lightsOnCallback = lightsOnCallback;
        this.lightsOffCallback = lightsOffCallback;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SetupInterface.fxml"));
        loader.setController(this);
        root = loader.load();

        var scene = new Scene(root);

        root.setOnKeyTyped(event -> {
            if (event.getCharacter().charAt(0) == 'i') {
                sectionBuffer.clear();
                sectionManager.getSections().stream().map(ImageSection::getIndex)
                        .forEach(sectionManager::toggleSection);
                updateImage();
            }
        });

        stage.setScene(scene);
        stage.setTitle("BinaryCamera Setup");
    }

    @Override
    public void showInterface() {
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initted!");

        setupMouse();

        stage.setOnCloseRequest(event -> System.exit(0));

        Map.of(lightsOn, lightsOnCallback, lightsOff, lightsOffCallback).forEach((button, callback) ->
                button.setOnAction(event -> {
                    button.setDisable(true);
                    callback.run();
                }));


        listenerId = handler.listenCamera(image -> {
            lastImage = image;
            updateImage();
        });
    }

    private void updateImage() {
        preview.setImage(SwingFXUtils.toFXImage(sectionManager.displayVisibleSections(lastImage), null));
    }

    public void close() {
        stage.close();
        stage.hide();
        handler.removeListener(listenerId);
    }

    private void setupMouse() {
        preview.setOnMouseClicked(this::handleEvent);
        preview.setOnMouseDragged(this::handleEvent);
    }

    private void handleEvent(MouseEvent event) {
        sectionManager.getSection((int) event.getX(), (int) event.getY()).ifPresent(this::addSection);
    }

    private void addSection(ImageSection section) {
        if (sectionBuffer.add(section)) {
            sectionManager.toggleSection(section.getIndex());
            updateImage();
        }
    }
}
