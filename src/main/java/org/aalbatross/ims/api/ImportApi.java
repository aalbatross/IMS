package org.aalbatross.ims.api;

import java.util.List;
import org.aalbatross.ims.model.ImportProductIssue;
import org.springframework.web.multipart.MultipartFile;

public interface ImportApi {

  List<ImportProductIssue> importAllProductAsCSV(MultipartFile csvFile);
}
