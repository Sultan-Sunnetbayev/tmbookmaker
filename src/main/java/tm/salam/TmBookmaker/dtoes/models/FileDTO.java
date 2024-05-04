package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private UUID uuid;
    private String name;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extension;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long size;

    @Override
    public String toString() {
        return "FileDTO{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

}