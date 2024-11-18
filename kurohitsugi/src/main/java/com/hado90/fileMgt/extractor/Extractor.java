package com.hado90.fileMgt.extractor;

import java.io.IOException;

public interface Extractor {
    public String extract(String inputPath) throws IOException;
    public String extract(String inputPath, String outputPath) throws IOException;
    public String getCompressedFileName(String input);
    public boolean isValidCompressedFileType(String input);
}
