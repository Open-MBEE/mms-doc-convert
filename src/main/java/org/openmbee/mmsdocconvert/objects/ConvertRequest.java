package org.openmbee.mmsdocconvert.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConvertRequest")
public class ConvertRequest {
    private String user;
    private String html;
    private String css;
    private OutputFormat format;

    @JsonCreator
    public ConvertRequest(@JsonProperty("user") String user, @JsonProperty("html") String html, @JsonProperty("css") String css, @JsonProperty("format") OutputFormat format)  {
        this.user = user;
        this.html = html;
        this.css = css;
        this.format = format;
    }

    @Schema(description = "User initiating request", required = true)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Schema(description = "Base64 Encoded HTML to convert", required = true)
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Schema(description = "Base64 Encoded CSS to convert", required = false)
    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    @Schema(description = "Target format for output", required = false)
    public OutputFormat getFormat() {
        return format;
    }

    public void setFormat(OutputFormat format) {
        this.format = format;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum OutputFormat {
        docx("application/pdf"), pdf("application/pdf"), latex("application/latex");

        private String formatName;

        OutputFormat(String name) {
            this.formatName = name;
        }

        public String getFormatName() {
            return formatName;
        }

        public static boolean exists(String find) {

            for (OutputFormat c : OutputFormat.values()) {
                if (c.getFormatName().equals(find)) {
                    return true;
                }
            }

            return false;
        }

    }
}
