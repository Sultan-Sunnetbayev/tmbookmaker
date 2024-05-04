package tm.salam.TmBookmaker.daoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tm.salam.TmBookmaker.models.File;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO file(name, path, extension, size) " +
            "VALUES(:name, :path, :extension, :size) ON CONFLICT DO NOTHING RETURNING CAST(uuid AS VARCHAR)")
    UUID addFile(@Param("name")String name, @Param("path")String path, @Param("extension")String extension,
                 @Param("size")long size);

    @Query("SELECT file FROM File file WHERE file.uuid = :uuid")
    File getFileByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT file FROM File file WHERE file.isConfirmed = :isConfirmed")
    List<File> getFilesByConfirmedValue(@Param("isConfirmed")boolean isConfirmed);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM file WHERE uuid = :uuid")
    void  deleteFileByUuid(@Param("uuid")UUID uuid);

    @Query("SELECT COUNT(file) FROM File file WHERE file.uuid IN :fileUuids")
    int getAmountFilesByUuids(@RequestParam("fileUuids") List<UUID> fileUuids);

}
