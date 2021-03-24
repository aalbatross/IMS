package org.aalbatross.ims.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aalbatross.ims.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ims/api/v1/import")
public class ImportController {

  @Autowired private ImportService importService;

  @PostMapping(value = "/csv")
  @ApiOperation(
      value = "Make a POST request to upload the file",
      produces = "application/json",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> importProducts(
      @ApiParam(name = "file", value = "Select the file to Upload", required = true)
          @RequestPart("file")
          MultipartFile file) {
    return ResponseEntity.ok(importService.importAllProductAsCSV(file));
  }
}
