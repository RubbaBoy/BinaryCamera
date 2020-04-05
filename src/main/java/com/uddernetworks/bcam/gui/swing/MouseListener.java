package com.uddernetworks.bcam.gui.swing;

import com.uddernetworks.bcam.image.sectioner.ImageSection;
import com.uddernetworks.bcam.image.sectioner.SectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MouseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MouseListener.class);

    private final SectionManager sectionManager;
    private Consumer<ImageSection> listener = $ -> {};
    private Set<ImageSection> sectionBuffer = new HashSet<>();

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
