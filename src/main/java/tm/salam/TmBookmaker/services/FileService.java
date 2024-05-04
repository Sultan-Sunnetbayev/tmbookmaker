package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;

import java.util.UUID;

public interface FileService {
    @Transactional
    ResponseTransfer<?> uploadFile(MultipartFile file);

    ResponseTransfer<FileDTO> getFileDTOByUuid(UUID uuid);

    ResponseTransfer<?> isFilesExistsByUuids(UUID... fileUuids);
}
