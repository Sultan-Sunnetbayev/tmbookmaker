package tm.salam.TmBookmaker.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static ResponseTransfer<FileDTO> uploadFile(final Path directoryPath, final String fileName, final MultipartFile file){

        ResponseTransfer<FileDTO> responseTransfer;

        try(InputStream inputStream=file.getInputStream()) {
            Path filePath=directoryPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            responseTransfer=ResponseTransfer.<FileDTO>builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.<FileDTO>builder()
                            .message("accept file successful uploaded")
                            .data(FileDTO.builder()
                                    .name(file.getOriginalFilename())
                                    .path(filePath.toString())
                                    .size(file.getSize())
                                    .build())
                            .build())
                    .build();
            int lastIndexDotFile=file.getOriginalFilename().lastIndexOf('.');
            if(lastIndexDotFile>0){
                responseTransfer.getResponseBody().getData().setExtension(file.getOriginalFilename().substring(lastIndexDotFile + 1));
            }
        } catch (IOException ioException) {
            responseTransfer=ResponseTransfer.<FileDTO>builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .responseBody(ResponseBody.<FileDTO>builder()
                            .message(ioException.getMessage())
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    public static ResponseTransfer<Path>getOrCreateDirectoryByStrPath(final String path){

        final ResponseTransfer<Path>responseTransfer;
        final Path directoryPath=Paths.get(path);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                responseTransfer=ResponseTransfer.<Path>builder()
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .responseBody(ResponseBody.<Path>builder()
                                .message("error couldn't create upload file directory")
                                .build())
                        .build();

                return responseTransfer;
            }
        }
        responseTransfer= ResponseTransfer.<Path>builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(ResponseBody.<Path>builder()
                        .data(directoryPath)
                        .build())
                .build();

        return responseTransfer;
    }

}
