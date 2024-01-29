# Installation
Visit the BDStudioLib [releases](https://github.com/oCrypt/BDStudioLib/releases) page and download the version specific to your Minecraft server version.
(NOTE: Currently only a 1.20.4 build is provided.)

# Usage

### Loading a model from a bdstudio file:
```java
File modelFile = new File("path"); // ensure that file ends with .bdstudio

try {
  DisplayModel model = DisplayModel.fromBDStudioFile(modelFile);
} catch (IOException e) {
  // handle exception
}
```

### Spawning & Manipulation:
To spawn the model at a given location, simply call the DisplayModel#spawn(location);
DisplayModel#collection() will allow you to view all the components of the model and also allow for some freedom of modification
