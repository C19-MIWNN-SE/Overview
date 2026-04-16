package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.transaction.Transactional;
import nl.miwnn.cohort19.DeExparts.Overview.model.Image;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Optional<Image> showImage(Long id){
        return imageRepository.findById(id);
    }
}
