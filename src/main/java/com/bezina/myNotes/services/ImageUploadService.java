package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.ImageRepository;
import com.bezina.myNotes.DAO.NoteRepository;
import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.entities.ImageModel;
import com.bezina.myNotes.entities.Note;
import com.bezina.myNotes.entities.User;
import com.bezina.myNotes.exceptions.ImageNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {
    public static final Logger LOG =  LoggerFactory.getLogger(ImageUploadService.class);
    private final UserRepository userRepo;
    private final NoteRepository noteRepo;
    private final ImageRepository imageRepo;
    @Autowired
    public ImageUploadService(UserRepository userRepo, NoteRepository noteRepo, ImageRepository imageRepo) {
        this.userRepo = userRepo;
        this.noteRepo = noteRepo;
        this.imageRepo = imageRepo;
    }
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        LOG.info("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = ServiceFunctions.getUserByPrincipal(userRepo,principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());

        ImageModel userProfileImage = imageRepo.findImageModelByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepo.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepo.save(imageModel);
    }

    public ImageModel uploadImageToNote(MultipartFile file, Principal principal, Long noteId) throws IOException {
        User user = ServiceFunctions.getUserByPrincipal(userRepo, principal);
        Note note = user.getNotes()
                .stream()
                .filter(p -> p.getId().equals(noteId))
                .collect(toSingleNoteCollector());

        ImageModel imageModel = new ImageModel();
        imageModel.setNoteId(note.getId());
        imageModel.setImageBytes(file.getBytes());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        imageModel.setUserId(user.getId());
        LOG.info("Uploading image to Note {}", note.getId());

        return imageRepo.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal) {
        User user = ServiceFunctions.getUserByPrincipal(userRepo, principal);

        ImageModel imageModel = imageRepo.findImageModelByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    public ImageModel getImageToNote(Long noteId) {
        ImageModel imageModel = imageRepo.findImageModelByNoteId(noteId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Note: " + noteId));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }
    private <T> Collector<T, ?, T> toSingleNoteCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
