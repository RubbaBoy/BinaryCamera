package com.uddernetworks.bcam.image.sectioner;

import com.uddernetworks.bcam.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultSectionManager implements SectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSectionManager.class);

    private static final Color SECTION_COLOR = Color.GREEN;
    private static final Color REMOVED_SECTION_COLOR = Color.RED;

    private final List<Integer> removed = new ArrayList<>();
    private final List<ImageSection> sections = new ArrayList<>();

    private final int xCount = 19;
    private final int yCount = 10;

    public DefaultSectionManager(int imageWidth, int imageHeight) {
        sections.addAll(getAllSections(imageWidth, imageHeight).keySet());
    }

    @Override
    public BufferedImage displayVisibleSections(BufferedImage image) {
        var copy = Utility.resizeImage(image, image.getWidth() / 2, image.getHeight() / 2);

        var graphics = copy.getGraphics();

        graphics.setColor(SECTION_COLOR);
        sections.forEach(section -> {
            var isRemoved = removed.contains(section.getIndex());

            if (isRemoved) {
                graphics.setColor(REMOVED_SECTION_COLOR);
            }

            section.drawTo(graphics, true);

            if (isRemoved) {
                graphics.setColor(SECTION_COLOR);
            }
        });

        return copy;
    }

    private List<ImageSection> getIncludedSections(int width, int height) {
        return getAllSections(width, height)
                .entrySet()
                .stream()
                .filter(Predicate.not(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Gets a map of {@link ImageSection}s from a given image, and if they have been removed or not.
     * This may be cached for
     *
     * @param width The width of the image
     * @param height The height of the image
     * @return A map of the {@link ImageSection}s and if they have been removed or not
     */
    public Map<ImageSection, Boolean> getAllSections(int width, int height) {
        width = width / xCount / 2;
        height = height / yCount / 2;

        var map = new HashMap<ImageSection, Boolean>();

        for (int x = 0; x < xCount; x++) {
            for (int y = 0; y < yCount; y++) {
                map.put(new ImageSection(x * width, y * height, width - 1, height - 1, x + (y * xCount)), removed.contains(x + (y * xCount)));
            }
        }

        return map;
    }

    @Override
    public void removeSections(List<Integer> cells) {
        removed.clear();
        removed.addAll(cells);
    }

    @Override
    public void removeAllButSections(List<Integer> cells) {
        removed.clear();
        IntStream.range(0, xCount * yCount).forEach(removed::add);
        removed.removeAll(cells);
    }

    @Override
    public void toggleSection(int index) {
        if (removed.contains(index)) {
            removed.remove((Integer) index);
        } else {
            removed.add(index);
        }
    }

    @Override
    public List<ImageSection> getSections() {
        return sections;
    }

    @Override
    public List<ImageSection> getSections(boolean removed) {
        return sections.stream().filter(section -> this.removed.contains(section.getIndex()) == removed).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<ImageSection> getSection(int x, int y) {
        return sections.stream().filter(section -> section.isWithin(x, y)).findFirst();
    }

    @Override
    public Optional<ImageSection> getSection(int x, int y, boolean removed) {
        return sections.stream().filter(section -> section.isWithin(x, y)).filter(section -> this.removed.contains(section.getIndex()) == removed).findFirst();
    }
}
