# Artificial Neural Network-Based Video Engine

## Overview

The **Artificial-Neural-Network-Based-Video-Engine** is a Java application that utilizes artificial neural networks to produce videos based on a text input. The video engine creates images by by processing text prompts entered in the app and utilizing OpenAI's API to bring them to life. Once the API returns the images, they're loaded into the engine, where users can customize them before finalizing their video. Not only that, the video engine allows you to add/edit audio and text. The engine gives you the ability to edit various aspects of text added such as position, color, font and size. Additionally, You have the ability to add multiple texts on top of the video at the same time. Finally, once the user completes their edits, the video engine integrates the audio and images to create the final video.  

## Features

- **Frame Interpolation**: Automatically generates intermediate frames between two existing frames to produce smoother video playback, particularly in lower frame-rate videos.
- **Resolution Enhancement**: Uses deep learning models to upscale low-resolution videos while maintaining sharpness and clarity, ideal for restoring old or compressed footage.
- **Noise Reduction**: Intelligent noise reduction powered by neural networks helps clean up grainy or noisy video footage without sacrificing detail.
- **Video Editing**: Enables AI-based video segmentation, object tracking, and scene detection, making it easier to cut, merge, or apply effects to specific parts of a video.
- **Real-time Processing**: Processes video content in real-time or near real-time, depending on hardware capabilities, thanks to the efficiency of the ANN models.
- **Customizable Neural Models**: Allows users to integrate their custom-trained neural networks to achieve specific video editing effects, making the engine highly adaptable for different use cases.

## Technologies Used

- **Programming Language**: Java and JavaFX
- **API Usage**: OpenAI API was used to generate the images based on the text input
- **Video Processing Library**: FFmpegFrameRecorder for Java to handle video input/output, frame capture, and manipulation
- **Multithreading**: Utilizes Java’s multithreading capabilities for efficient video frame processing
- **File Formats Supported**: MP4

## Usage

1. **Video Input**: Load a video file using the engine's command-line interface or GUI.
2. **Apply Enhancements**: Choose the neural network-based enhancement options such as frame interpolation, resolution upscaling, or noise reduction.
3. **Custom Neural Networks**: If you have a custom-trained neural network model, you can load it into the engine and apply its specific video editing capabilities.
4. **Process and Export**: The processed video can be exported in the desired format, with options to control encoding settings such as bitrate and frame rate.

## Future Improvements

- **GPU Acceleration**: Further optimizations for NVIDIA CUDA and other GPU frameworks to speed up video processing.
- **Deepfake Detection**: Integrate AI models to detect deepfakes and manipulated video content.
- **Motion Tracking**: Enhance the video editing toolset with more advanced motion tracking features.
- **Live Video Editing**: Enable real-time video editing during live streams or video calls.

## Contribution

Contributions are welcome! Feel free to fork this repository, submit a pull request, or raise issues for improvements. The project is open to enhancements in both the neural network models and video processing capabilities.
