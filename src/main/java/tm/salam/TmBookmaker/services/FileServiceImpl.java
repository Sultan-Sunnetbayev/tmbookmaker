package tm.salam.TmBookmaker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.TmBookmaker.daoes.FileRepository;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;
import tm.salam.TmBookmaker.helpers.FileUploadUtil;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> uploadFile(final MultipartFile file){

        final ResponseTransfer<Path>pathResponseTransfer=FileUploadUtil.getOrCreateDirectoryByStrPath(fileUploadPath);

        if(!pathResponseTransfer.getHttpStatus().is2xxSuccessful()) {
            return pathResponseTransfer;
        }
        final String fileName= UUID.randomUUID() + "_" + file.getOriginalFilename();
        ResponseTransfer<FileDTO> responseTransfer= FileUploadUtil.uploadFile(pathResponseTransfer.getResponseBody().getData(),
                fileName, file);

        if(!responseTransfer.getHttpStatus().is2xxSuccessful()){
            return responseTransfer;
        }
        FileDTO uploadedFileDTO=responseTransfer.getResponseBody().getData();
        final UUID savedFileUuid=fileRepository.addFile(uploadedFileDTO.getName(),
                uploadedFileDTO.getPath(), uploadedFileDTO.getExtension(), uploadedFileDTO.getSize());

            if(savedFileUuid!=null){
                responseTransfer.getResponseBody().getData().setUuid(savedFileUuid);
            }else{
                responseTransfer= ResponseTransfer.<FileDTO>builder()
                        .httpStatus(HttpStatus.EXPECTATION_FAILED)
                        .responseBody(ResponseBody.<FileDTO>builder()
                                .message("error file don't added")
                                .build())
                        .build();
            }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<FileDTO> getFileDTOByUuid(final UUID uuid){

        final ResponseTransfer<FileDTO> responseTransfer;
        final File file=fileRepository.getFileByUuid(uuid);

        if(file==null){
            responseTransfer=ResponseTransfer.<FileDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<FileDTO>builder()
                            .message("error file not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<FileDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<FileDTO>builder()
                            .message("accept file successful returned")
                            .data(File.toFileDTO(file))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Transactional
    @Async
    @Scheduled(cron = "0 01 02 * * *")
    void removeNotConfirmedFiles() throws IOException {

        List<File> files=fileRepository.getFilesByConfirmedValue(false);

        if(files==null){
            return;
        }
        Date currentDate=new Date();

        for(File file:files){
            long diff=TimeUnit.MILLISECONDS.toHours(currentDate.getTime()-file.getCreated().getTime());

            if(diff>=24){
                Files.deleteIfExists(Paths.get(file.getPath()));
                fileRepository.deleteFileByUuid(file.getUuid());
            }
        }
    }

    @Override
    public ResponseTransfer<?> isFilesExistsByUuids(UUID... fileUuids){

        final ResponseTransfer<?> responseTransfer;
        List<UUID> uploadFileUuids=new LinkedList<>();

        for(UUID fileUuid:fileUuids){
            if(fileUuid!=null){
                uploadFileUuids.add(fileUuid);
            }
        }
        int amountFiles=fileRepository.getAmountFilesByUuids(uploadFileUuids);
        if(amountFiles!=uploadFileUuids.size()){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.builder()
                            .message("error with upload files not found")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .build();
        }

        return responseTransfer;
    }

}
