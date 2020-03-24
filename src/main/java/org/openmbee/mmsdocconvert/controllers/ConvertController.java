package org.openmbee.mmsdocconvert.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.openmbee.mmsdocconvert.objects.ConvertRequest;
import org.openmbee.mmsdocconvert.services.ConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller("/")
public class ConvertController {
    private static final Logger logger = LoggerFactory.getLogger(ConvertController.class);

    protected final ConvertService convertService;

    public ConvertController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    @Operation(operationId = "convertRequest", summary = "Request document conversion", description = "Converts requested HTML and CSS into request output format")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/latex"), description = "Latex File Response")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), description = "Word Document File Response")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/pdf"), description = "PDF File Response")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public @ResponseBody HttpEntity<byte[]> index(@RequestBody ConvertRequest convertRequest) {
        logger.debug(convertRequest.getUser() + " " + convertRequest.getHtml());
        String fileName = ConvertService.getFileName(convertRequest.getHtml(), convertRequest.getFormat());
        byte[] document = convertService.convert(convertRequest.getHtml(), convertRequest.getCss(), convertRequest.getFormat());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", convertRequest.getFormat().getFormatName()));
        header.set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        header.setContentLength(document.length);

        return new HttpEntity<>(document, header);
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<?> getPing() {
        Map<String, String> response = new HashMap<>();
        response.put("status", HttpStatus.OK.name());
        return ResponseEntity.ok(response);
    }
}
