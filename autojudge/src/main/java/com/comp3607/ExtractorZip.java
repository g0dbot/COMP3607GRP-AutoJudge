package com.comp3607;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractorZip implements Extractor {

    public static void extract(String input, String output) throws IOException {
        String zipName = new File(input).getName().replaceFirst("[.][^.]+$", "");

        String outputDir = output + File.separator + zipName;

        try (ZipInputStream zipin = new ZipInputStream(new FileInputStream(input))) {
            byte[] buffer = new byte[1024];
            int len;

            ZipEntry entry;

            while ((entry = zipin.getNextEntry()) != null) {
                File filePath = new File(outputDir, entry.getName());

                if (entry.isDirectory()) {
                    filePath.mkdirs();
                }

                else {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                        while ((len = zipin.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                }

                zipin.closeEntry();
            }
        }
    }

}