package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;
import tm.salam.TmBookmaker.models.File;

@Component
public class FileDTOSerializer {

    public FileDTO toFileDTOGeneral(final File file){

        if(file==null){

            return null;
        }

        return FileDTO.builder()
                .uuid(file.getUuid())
                .name(file.getName())
                .path(file.getPath())
                .build();
    }

}
