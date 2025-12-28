# ViaFlow

A LabyMod 4 addon that integrates ViaVersion protocol translation, allowing you to connect to Minecraft servers running any version from 1.8.9 to 1.21.x.

## Features

- **Cross-Version Connectivity**: Connect to servers running older or newer Minecraft versions
- **Version Selection**: Manually select target server version
- **F3 Debug Display**: Show protocol version information in debug screen
- **Full ViaVersion Suite**: Includes ViaVersion, ViaBackwards, ViaRewind, and ViaLegacy

## Supported Versions

### Client Versions (LabyMod)
- 1.8.9, 1.12.2, 1.16.5, 1.17.1, 1.18.2
- 1.19.x, 1.20.x, 1.21.x

### Server Versions (Connect To)
- 1.8.x through 1.21.x
- Legacy versions via ViaLegacy

## Installation

1. Download the latest ViaFlow release
2. Place the `.jar` file in your LabyMod addons folder
3. Restart LabyMod
4. Configure via LabyMod settings or `/viaflow` command

## Commands

- `/viaflow` - Show current version info
- `/viaflow list` - List all supported versions
- `/viaflow <version>` - Set target version (e.g., `/viaflow 1.8.9`)
- `/viaflow help` - Show command help

## Configuration

Access ViaFlow settings through LabyMod's addon settings:

- **Enable ViaFlow**: Toggle protocol translation
- **Target Version**: Select server version (Native uses client version)
- **Show Version in F3**: Display version info in debug screen
- **Verify Session (Old Versions)**: Authentication for pre-1.7 servers
- **BetaCraft Authentication**: Support for classic servers

## Building

```bash
./gradlew build
```

The compiled addon will be in `build/libs/`.

## Credits

- [ViaVersion](https://github.com/ViaVersion/ViaVersion) - Core protocol translation
- [ViaBackwards](https://github.com/ViaVersion/ViaBackwards) - Newer to older version support
- [ViaRewind](https://github.com/ViaVersion/ViaRewind) - 1.9+ to 1.8.x support
- [ViaLegacy](https://github.com/ViaVersion/ViaLegacy) - Legacy version support
- [ViaLoader](https://github.com/RaphiMC/ViaLoader) - ViaVersion integration library
- [ViaForge](https://github.com/ViaVersion/ViaForge) - Reference implementation
- [LabyMod](https://labymod.net/) - Client platform

## License

This project uses code and libraries from the ViaVersion project family.
See individual projects for their respective licenses.

## Author

WiFlow
