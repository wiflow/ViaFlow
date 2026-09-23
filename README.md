# ViaFlow [![CodeFactor](https://www.codefactor.io/repository/github/wiflow/viaflow/badge)](https://www.codefactor.io/repository/github/wiflow/viaflow)
A LabyMod 4 addon that lets you join servers running other Minecraft versions, using ViaVersion, ViaBackwards and ViaRewind.

## Features

- Asks each server for its version when you join and connects as that version, or natively when the server accepts your client
- Pick a version yourself in the server list, in the addon settings or with `/viaflow <version>`
- Every release from 1.8 up to the newest version ViaVersion supports
- Runs on every Minecraft version LabyMod supports, from 1.8.9 to 26.3
- ViaVersion only starts once a server needs another version, so playing natively costs nothing
- A HUD widget shows the server version while you play on a translated connection

## Usage

Join the server. With the Server Version set to Auto, ViaFlow pings the server first and connects as the version it reports. If a proxy lists your version but its servers turn you away, ViaFlow joins again as the newest version the proxy runs. When those servers run ViaVersion, they report their own version, and ViaFlow joins as that from the next join on. ViaFlow remembers the version each server was joined as, also across restarts, so later joins skip the check.

To force a version, pick it in the Server Version dropdown below the server list buttons before joining. The version resets to Auto every time the game starts.

## Commands

- `/viaflow` shows your client version and the version you connect as
- `/viaflow <version>` connects as that version from the next join, for example `/viaflow 1.8.9`
- `/viaflow auto` connects as the version each server reports again
- `/viaflow native` connects natively
- `/viaflow list` lists every version

## Known limitations

- The server list pings servers with your client version, so servers on other versions show up as incompatible there. Joining them works.
- The first join to a proxy whose servers don't accept your version takes two tries, since the proxy answers pings for every version it supports. After that, ViaFlow remembers the version that worked.
- With a 1.21.4 or newer client, pick block does nothing on servers running 1.21.3 or older.
- With a 1.16.5 or older client on a 1.17 or newer server, blocks below y 0 and above y 255 are not shown.
- With a client older than 1.20.3 on a 1.20.3 or newer server, scoreboard lines that carry their own text show without it.
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
