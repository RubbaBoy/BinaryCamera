package com.uddernetworks.bcam.gui.swing;

import com.uddernetworks.bcam.image.sectioner.ImageSection;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MouseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MouseListener.class);

    private final SectionManager sectionManager;
    private final Set<ImageSection> sectionBuffer = new HashSet<>();
    private Consumer<ImageSection> listener = $ -> {};

    public MouseListener(JPanel panel, SectionManager sectionManager) {
        this.sectionManager = sectionManager;

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                handleEvent(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                sectionBuffer.clear();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                handleEvent(event);
            }
        });
    }

    private void handleEvent(MouseEvent event) {
        sectionManager.getSection(event.getX(), event.getY()).ifPresent(this::addSection);
    }

    private void addSection(ImageSection section) {
        if (sectionBuffer.add(section)) {
            sectionManager.toggleSection(section.getIndex());
            listener.accept(section);
        }
    }

    public void addToggleListener(Consumer<ImageSection> listener) {
        this.listener = listener;
    }

}
