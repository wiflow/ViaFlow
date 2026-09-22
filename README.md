# ViaFlow [![CodeFactor](https://www.codefactor.io/repository/github/wiflow/viaflow/badge)](https://www.codefactor.io/repository/github/wiflow/viaflow)
A LabyMod 4 addon that lets you join servers running other Minecraft versions, using ViaVersion, ViaBackwards and ViaRewind.

## Features

- Pick the server version in the server list, in the addon settings or with `/viaflow <version>`
- Every release from 1.8 up to the newest version ViaVersion supports
- Runs on every Minecraft version LabyMod supports, from 1.8.9 to 26.3
- ViaVersion only starts once you pick another version, so playing natively costs nothing
- A HUD widget shows the server version while you play on a translated connection

## Usage

1. Open the server list and pick a version in the Server Version dropdown below the buttons.
2. Join the server.

The version resets to native every time the game starts.

## Commands

- `/viaflow` shows your client version and the version you connect as
- `/viaflow <version>` connects as that version from the next join, for example `/viaflow 1.8.9`
- `/viaflow native` connects natively again
- `/viaflow list` lists every version

## Known limitations

- The server list pings servers with your client version, so servers on other versions show up as incompatible there. Joining them works.
- With a 1.21.4 or newer client, pick block does nothing on servers running 1.21.3 or older.
- The first start after installing needs an internet connection: LabyMod downloads ViaVersion, ViaBackwards and ViaRewind from repo.viaversion.com.

## Building

```bash
./gradlew build createReleaseJar
```

The addon is the jar ending in `-release.jar` in `build/libs/`.

## Credits

- [ViaVersion](https://github.com/ViaVersion/ViaVersion), [ViaBackwards](https://github.com/ViaVersion/ViaBackwards) and [ViaRewind](https://github.com/ViaVersion/ViaRewind) do the protocol translation
- [ViaFabricPlus](https://github.com/ViaVersion/ViaFabricPlus) showed how to run it on the client
- [LabyMod](https://labymod.net/)

## License

GPL-3.0, like ViaVersion, ViaBackwards and ViaRewind. See [LICENSE](LICENSE).
