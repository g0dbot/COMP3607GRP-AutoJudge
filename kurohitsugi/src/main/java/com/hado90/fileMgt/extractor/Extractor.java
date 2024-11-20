package com.hado90.fileMgt.extractor;

import java.io.IOException;

/**
 * Interface for file extractors that define methods for extracting content
 * from compressed files. Implementations of this interface will support
 * different file formats (e.g., ZIP, TAR, etc.) and provide functionality
 * for extracting files and validating supported file types.
 */
public interface Extractor {

    /**
     * Extracts the contents of a compressed file to a default location.
     *
     * @param inputPath the path to the compressed file to extract.
     * @return the path to the extracted content.
     * @throws IOException if an error occurs during the extraction process.
     */
    public String extract(String inputPath) throws IOException;

    /**
     * Extracts the contents of a compressed file to a specified output location.
     *
     * @param inputPath the path to the compressed file to extract.
     * @param outputPath the path where the extracted content will be stored.
     * @return the path to the extracted content.
     * @throws IOException if an error occurs during the extraction process.
     */
    public String extract(String inputPath, String outputPath) throws IOException;

    /**
     * Retrieves the name of the compressed file (without its extension) from the given path.
     *
     * @param input the path to the compressed file.
     * @return the name of the compressed file without its extension.
     */
    public String getCompressedFileName(String input);

    /**
     * Validates if the given file path points to a supported compressed file type.
     *
     * @param input the path to the file to validate.
     * @return true if the file is a valid compressed file type, false otherwise.
     */
    public boolean isValidCompressedFileType(String input);
}
