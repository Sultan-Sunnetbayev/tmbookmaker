package tm.salam.TmBookmaker.controllers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.services.FileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload-file", produces = "application/json")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){

        final ResponseTransfer<?> responseTransfer=fileService.uploadFile(file);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-file", params = {"fileUuid"}, produces = "application/json")
    public ResponseEntity<?> getFileByUuid(@RequestParam("fileUuid") UUID fileUuid){

        final ResponseTransfer<FileDTO> responseTransfer=fileService.getFileDTOByUuid(fileUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
