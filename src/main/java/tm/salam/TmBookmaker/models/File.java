package tm.salam.TmBookmaker.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import tm.salam.TmBookmaker.dtoes.models.FileDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "file")
public class File extends BaseEntity {

    @Column(name = "name")
    @Size(max = 256, message = "file's name length should be less than 256")
    private String name;
    @Column(name = "path")
    @NotNull(message = "file's path don't be null")
    @NotEmpty(message = "file's path don't be empty")
    @Size(min = 1, max = 300, message = "file's path length should be less than 301")
    private String path;
    @Column(name = "extension")
    @Size(max = 50, message = "file's extension length should be less than 50")
    private String extension;
    @Column(name = "size")
    private long size;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    public static FileDTO toFileDTO(final File file){

        return file!=null ?
                FileDTO.builder()
                        .uuid(file.getUuid())
                        .name(file.getName())
                        .path(file.getPath())
                        .extension(file.getExtension())
                        .size(file.getSize())
                        .build()
                : null;
    }

}
