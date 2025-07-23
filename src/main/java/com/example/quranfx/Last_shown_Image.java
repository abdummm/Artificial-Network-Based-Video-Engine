package com.example.quranfx;

public class Last_shown_Image {
    private String image_id;
    private Type_of_Image typeOfImage;

    public Last_shown_Image(String image_id, Type_of_Image typeOfImage) {
        this.image_id = image_id;
        this.typeOfImage = typeOfImage;
    }

    public String getImage_id() {
        return image_id;
    }

    public Type_of_Image getTypeOfImage() {
        return typeOfImage;
    }
}
