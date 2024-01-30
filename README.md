# Installation
Visit the BDStudioLib [releases](https://github.com/oCrypt/BDStudioLib/releases) page and download the version specific to your Minecraft server version. This is a library, NOT a standalone server plugin.
(NOTE: Currently only a 1.20.4 build is provided.)

## Adding BDStudioLib as a dependency to your plugin:

### Maven:

Repository:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Dependency:
```xml
<dependency>
  <groupId>com.github.oCrypt</groupId>
  <artifactId>BDStudioLib</artifactId>
  <version>1.1</version>
</dependency>
```

For additional information visit https://jitpack.io/#oCrypt/BDStudioLib.

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
