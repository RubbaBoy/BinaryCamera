package com.uddernetworks.bcam.image.sectioner;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public interface SectionManager {

    BufferedImage displayVisibleSections(BufferedImage image);

    void removeSections(List<Integer> cells);

    void removeAllButSections(List<Integer> cells);

    void toggleSection(int index);

    List<ImageSection> getSections();

    List<ImageSection> getSections(boolean removed);

    Optional<ImageSection> getSection(int x, int y);

    Optional<ImageSection> getSection(int x, int y, boolean removed);
}
