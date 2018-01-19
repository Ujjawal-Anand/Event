package uscool.io.event.photoPicker;

/**
 * Created by andy1729
 */
public interface Constants {

    String DEFAULT_FOLDER_NAME = "Event";

    interface RequestCodes {
        int EASYIMAGE_IDENTIFICATOR = 0b1101101100; //876
        int SOURCE_CHOOSER = 1 << 14;

        int PICK_PICTURE_FROM_DOCUMENTS = EASYIMAGE_IDENTIFICATOR + (1 << 11);
        int PICK_PICTURE_FROM_GALLERY = EASYIMAGE_IDENTIFICATOR + (1 << 12);
        int TAKE_PICTURE = EASYIMAGE_IDENTIFICATOR + (1 << 13);
    }

    interface BundleKeys {
        String FOLDER_NAME = "uscool.io.event.images";
        String ALLOW_MULTIPLE = "uscool.io.event.allow_multiple";
        String COPY_TAKEN_PHOTOS = "uscool.io.event.copy_taken_photos";
        String COPY_PICKED_IMAGES = "uscool.io.event.copy_picked_images";
    }
}
