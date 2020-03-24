package org.openmbee.mmsdocconvert.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConvertRequest")
public class ConvertRequest {
    private String user;
    private String html;
    private String css;
    private OutputFormat format;

    public static final String APPLICATION_PDF = "pdf";
    public static final String APPLICATION_DOCX = "vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String APPLICATION_LATEX = "latex";

    @JsonCreator
    public ConvertRequest(@JsonProperty("user") String user, @JsonProperty("html") String html, @JsonProperty("css") String css, @JsonProperty("format") String format)  {
        setUser(user);
        setHtml(html);
        setCss(css);
        setFormat(format);
    }

    @Schema(description = "User initiating request", required = false)
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Schema(description = "HTML to convert", required = true)
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Schema(description = "CSS to convert", required = false)
    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    @Schema(description = "Target format for output", required = true)
    public OutputFormat getFormat() {
        return format;
    }

    public void setFormat(String formatString) {
        this.format = OutputFormat.valueOf(formatString.toLowerCase());
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum OutputFormat {
        docx(ConvertRequest.APPLICATION_DOCX), pdf(ConvertRequest.APPLICATION_PDF), latex(ConvertRequest.APPLICATION_LATEX);

        private String formatName;

        OutputFormat(String name) {
            this.formatName = name;
        }

        @JsonValue
        public String getFormatName() {
            return formatName;
        }

        public static String getContentType(String find) {

            for (OutputFormat c : OutputFormat.values()) {
                if (c.getFormatName().equals(find)) {
                    return c.toString();
                }
            }
            return null;
        }

    }
}
