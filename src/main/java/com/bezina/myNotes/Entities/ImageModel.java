package com.bezina.myNotes.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonIgnore
    private Long noteId;
    @JsonIgnore
    private Long userId;
    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }
}
