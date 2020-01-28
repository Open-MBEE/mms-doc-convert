package org.openmbee.mmsdocconvert.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.openmbee.mmsdocconvert.objects.ConvertRequest;
import org.openmbee.mmsdocconvert.services.ConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller("/convert")
public class ConvertController {
    private static final Logger logger = LoggerFactory.getLogger(ConvertController.class);

    protected final ConvertService convertService;

    public ConvertController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @RequestMapping(value = "/**", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(operationId = "convertRequest", summary = "Request document conversion", description = "Converts requested HTML and CSS into request output format")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(type="object")), description = "User, if found in specified group and list of groups the user belongs to")
    @ApiResponse(responseCode = "500", description = "Convert service is offline")
    public @ResponseBody byte[] index(@RequestBody ConvertRequest convertRequest) throws IOException {
        logger.info(convertRequest.getUser() + " " + convertRequest.getHtml());
        return convertService.convert(convertRequest.getHtml(), convertRequest.getCss(), convertRequest.getFormat());
    }
}
