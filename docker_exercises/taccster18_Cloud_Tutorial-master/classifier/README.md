# TensorFlow Image Classifier

This directory includes a "self-contained" image classifier application based on the TensorFlow library.

## Build the Image

To build the image use the Dockerfile in this directory with a `docker build` command like so:

```
$ docker build -t taccsciapps/classify_image .

```

## Run the App

To run the app from the built image, set an environment variable, `$URL`, to a publicly available URL to an image to classify.
The `data` directory within this directory contains test data.

For example,

```
$ docker run -it --rm -e URL=https://raw.githubusercontent.com/TACC/taccster18_Cloud_Tutorial/master/classifier/data/dog.jpeg  taccsciapps/classify_image
```