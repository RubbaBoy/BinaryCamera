package com.uddernetworks.bcam.image.sectioner;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

/**
 * Splits up the image into squared sections, allowing for brightness to only be recognised in certain areas as to
 * improve accuracy.
 */
public interface SectionManager {

    /**
     * Gets the original image with colored sections over them, used in the GUI to display enabled and disabled
     * sections.
     *
     * @param image The original {@link BufferedImage} from the camera
     * @return The resulting {@link BufferedImage}
     */
    BufferedImage displayVisibleSections(BufferedImage image);

    /**
     * Removes all given sections by their IDs.
     *
     * @param sections A list of the section IDs to remove
     */
    void removeSections(List<Integer> sections);

    /**
     * Removes all sections except the IDs given.
     *
     * @param sections The section IDs not to remove.
     */
    void removeAllButSections(List<Integer> sections);

    /**
     * Toggles the status of the given section ID.
     *
     * @param section The section ID
     */
    void toggleSection(int section);

    /**
     * Gets all sections (both enabled and disabled) in the image.
     *
     * @return An immutable list of all sections
     */
    List<ImageSection> getSections();

    /**
     * Gets all sections with the given removal status.
     *
     * @param removed If the fetched sections should only be removed
     * @return An immutable list of the sections with the given removal status
     */
    List<ImageSection> getSections(boolean removed);

    /**
     * Gets a section with the given image pixel coordinates, if any.
     *
     * @param x The pixel X value
     * @param y The pixel Y value
     * @return The {@link ImageSection} found, if any
     */
    Optional<ImageSection> getSection(int x, int y);

    /**
     * Gets a section with the given image pixel coordinates matching the given removal status, if any.
     *
     * @param x The pixel X value
     * @param y The pixel Y value
     * @param removed The removal status the section should match
     * @return The {@link ImageSection} found, if any
     */
    Optional<ImageSection> getSection(int x, int y, boolean removed);
}
