package nl.miwnn.cohort19.DeExparts.Overview.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

/**
 * Author: Anouk de Vos
 * An Image, used as a profile picture for an Participant or Instructor
 */
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    private String contentType;

    public Image(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }
}
