package com.uddernetworks.bcam.gui.swing;

import com.uddernetworks.bcam.image.sectioner.ImageSection;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class SwingPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingPanel.class);

    private final SectionManager sectionManager;
    private BufferedImage image = null;

    public SwingPanel(SectionManager sectionManager, Runnable callback) {
        this.sectionManager = sectionManager;

        getSize(new Dimension(1920 / 2, 1080 / 2));
        setPreferredSize(new Dimension(1920 / 2, 1080 / 2));

        var listener = new MouseListener(this, sectionManager);
        listener.addToggleListener(section -> updateUI());

        requestFocus();

        new Timer(50, event -> {
            var inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            var actionMap = getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "invert");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "continue");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "continue");

            actionMap.put("invert", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    sectionManager.getSections().stream().map(ImageSection::getIndex)
                            .forEach(sectionManager::toggleSection);
                }
            });

            actionMap.put("continue", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    LOGGER.info("Continuing");
                    callback.run();
                }
            });
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null) {
            return;
        }

        g.drawImage(sectionManager.displayVisibleSections(image), 0, 0, this);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
