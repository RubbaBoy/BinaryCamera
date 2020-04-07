package com.uddernetworks.bcam.gui;

import com.uddernetworks.bcam.camera.CameraHandler;
import com.uddernetworks.bcam.gui.swing.SwingPanel;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.function.Consumer;

public class SwingInterface implements UserInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingInterface.class);

    private final CameraHandler handler;
    private final SectionManager sectionManager;
    private final Consumer<SwingInterface> callback;

    private JFrame frame;
    private int listenerId;

    public SwingInterface(CameraHandler handler, SectionManager sectionManager, Consumer<SwingInterface> callback) {
        this.handler = handler;
        this.sectionManager = sectionManager;
        this.callback = callback;
    }

    @Override
    public void showInterface() {
        frame = new JFrame("BinaryCamera - RubbaBoy");

        setupFrame(frame);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.getSize(new Dimension(1920 / 2, 1080 / 2 + 30));
        frame.setPreferredSize(new Dimension(1920 / 2, 1080 / 2 + 30));

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.toFront();
    }

    private void setupFrame(JFrame frame) {
        var panel = new SwingPanel(sectionManager, () -> callback.accept(this));
        frame.getContentPane().add(panel);

        listenerId = handler.listenCamera(image -> {
            panel.setImage(image);
            panel.updateUI();
        });
    }

    public void close() {
        handler.removeListener(listenerId);
        frame.dispose();
    }
}
