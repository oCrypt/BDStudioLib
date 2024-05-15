# Installation
Visit the BDStudioLib [releases](https://github.com/oCrypt/BDStudioLib/releases) page and download the version specific to your Minecraft server version. This is a library, NOT a standalone server plugin.

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
  <version>v1.2</version>
</dependency>
```

For additional information visit https://jitpack.io/#oCrypt/BDStudioLib.

# Usage

### Loading a model from a bdstudio file:
```java
File modelFile = new File("path"); // ensure that file ends with .bdstudio

try {
  DisplayModelSchematic model = DisplayModelSchematic.fromBDStudioFile(modelFile);
} catch (IOException e) {
  // handle exception
}
```

### Spawning & Manipulation:
To spawn the schematic at a given location, simply call the DisplayModelSchematic#spawn(location);
DisplayModelSchematic#collection() will allow you to view all the BDComponents of the model.
Additionally, you will also be able to access all the Matrix4f transformations and modify them freely.
