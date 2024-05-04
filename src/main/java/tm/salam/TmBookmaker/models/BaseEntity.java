package tm.salam.TmBookmaker.models;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID uuid;
    @Column(name = "created")
    @Expose(deserialize = false, serialize = false)
    @CreationTimestamp
    protected Date created;
    @Column(name = "updated")
    @Expose(deserialize = false, serialize = false)
    @UpdateTimestamp
    protected Date updated;

}
