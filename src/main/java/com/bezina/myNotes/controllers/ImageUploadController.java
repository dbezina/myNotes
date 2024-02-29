package com.bezina.myNotes.controllers;

import com.bezina.myNotes.entities.ImageModel;
import com.bezina.myNotes.payload.response.MessageResponse;
import com.bezina.myNotes.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {

        imageUploadService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @PostMapping("/{noteId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToNote(@PathVariable("noteId") String noteId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToNote(file, principal, Long.parseLong(noteId));
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel userImage = imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{noteId}/image")
    public ResponseEntity<ImageModel> getImageToNote(@PathVariable("noteId") String noteId) {
        ImageModel noteImage = imageUploadService.getImageToNote(Long.parseLong(noteId));
        return new ResponseEntity<>(noteImage, HttpStatus.OK);
    }

}