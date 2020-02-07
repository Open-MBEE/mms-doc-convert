package org.openmbee.mmsdocconvert.services;

import org.apache.commons.lang3.StringUtils;
import org.openmbee.mmsdocconvert.objects.ConvertRequest.OutputFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
public class ConvertService {

    private OutputFormat outputFormat;

    @Value("${pandoc.exec:pandoc}")
    private String pandocExec;

    @Value("${pandoc.pdfengine:weasyprint}")
    private String pdfEngine;

    @Value("${pandoc.princeexec:}")
    private String princeExec;

    @Value("${pandoc.output.dir:/tmp}")
    public String PANDOC_DATA_DIR;

    @Value("${pandoc.output.csstmp:css}")
    private String PANDOC_OUTPUT_CSSTMP;

    @Value("${pandoc.output.file:output}")
    private String PANDOC_OUTPUT_FILE;

    public ConvertService() {
    }

    public static String getFileName(String inputString, OutputFormat outputFormat) {
        String title = StringUtils.substringBetween(inputString, "<title>", "</title>");
        if (title == null) {
            title = "Untitled";
        }
        return String.format("%s.%s", title.replaceAll("\\W+", "_"), outputFormat.name());
    }

    public byte[] convert(String inputString, String cssString, OutputFormat outputFormat) {
        File tempFile = null;
        String outputFile = String.format("%s/%s", PANDOC_DATA_DIR, getFileName(inputString, outputFormat));

        int status = 0;
        StringBuilder command = new StringBuilder();

        command.append(String.format("%s --mathml --variable=title: --from=html+raw_html+simple_tables", this.pandocExec));
        if (!cssString.isEmpty()) {
            try {
                tempFile = new File(PANDOC_DATA_DIR + File.separator + PANDOC_OUTPUT_CSSTMP);
                OutputStream out = new FileOutputStream(tempFile);
                out.write(cssString.getBytes());
                out.close();
                command.append(String.format(" --css=%s", tempFile.toPath().toString()));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        if (outputFormat.name().equals("pdf")) {
            command.append(String.format(" --pdf-engine=%s", this.pdfEngine));
        }
        command.append(String.format(" -o %s", outputFile));

        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            OutputStream out = process.getOutputStream();
            out.write(inputString.getBytes());
            out.flush();
            out.close();
            status = process.waitFor();
            File pdfFile = new File(outputFile);
            return Files.readAllBytes(pdfFile.toPath());
        } catch (InterruptedException ex) {
            throw new RuntimeException("Could not execute: " + command.toString(), ex);
        } catch (IOException ex) {
            throw new RuntimeException("Could not execute. Maybe pandoc is not in PATH?: " + command.toString(), ex);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null && !tempFile.delete()) {
                throw new RuntimeException("Could not delete temporary css file");
            }
            if (status != 0) {
                throw new RuntimeException(
                        "Conversion failed with status code: " + status + ". Command executed: " + command.toString());
            }
        }
    }
}
