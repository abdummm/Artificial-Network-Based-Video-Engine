# Artificial Neural Network-Based Video Engine

## Overview

The **Artificial-Neural-Network-Based-Video-Engine** is a Java application that utilizes artificial neural networks to produce videos based on a text input. The video engine creates images by by processing text prompts entered in the app and utilizing OpenAI's API to bring them to life. Once the API returns the images, they're loaded into the engine, where users can customize them before finalizing their video. Not only that, the video engine allows you to add/edit audio and text. The engine gives you the ability to edit various aspects of text added such as position, color, font and size. Additionally, You have the ability to add multiple texts on top of the video at the same time. Finally, once the user completes their edits, the video engine integrates the audio and images to create the final video.  

## Features

- **Text-to-Image Generation**: the video engine leverages OpenAI's DALL-E 3 for creating images based on prompts in configurable aspect ratios of 9:16, 16:9, 1:1.
- **Audio Integration**: The video engine supports various audio file formats: MP3, WAV, FLAC. It also allows users to overlay audio onto video clips.
- **Multilingual Support**: Generate videos with captions in different languages.
- **User Interface**: Includes an intuitive JavaFX-based interface for ease of use.
- **Customization**: Configure font styles, colors, and positions for text overlays.

## Technologies Used

- **Programming Languages used**: Java, JavaFX and FXML
- **API Usage**: OpenAI API was used to generate the images based on the text input
- **Video Processing Library**: FFmpegFrameRecorder for Java to handle video input/output, frame capture, and manipulation
- **File Formats Supported**: MP3, WAV and FLAC for audio, MP4 for videos and JPG for images
- **Libraries Used**: OkHTTP was used to handle API requests and Jackson was used For JSON processing

## Future Improvements

- **Hardware Acceleration**: Further optimizations for NVIDIA CUDA and other GPU frameworks to speed up video processing.
- **Cloud Integration**: Introduce cloud-based storage for handling larger video files and improving accessibility across devices.
- **Mobile App Version**: Develop a mobile version of the app to allow users to edit videos on the mobile for improved accessibility.

## Contribution

Contributions are welcome! Feel free to fork this repository, submit a pull request, or raise issues for improvements. The project is open to enhancements in both the neural network models and video processing capabilities.

## Here is a picture showcasing the design of the app.
![app preview](https://imgur.com/fcJvPKW.jpg)
