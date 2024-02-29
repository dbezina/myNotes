package com.bezina.myNotes.DAO;

import com.bezina.myNotes.entities.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findImageModelByUserId(Long userId);
    Optional<ImageModel> findImageModelByNoteId(Long noteId);
}
