# Boostify 1.0.0

Boostify is a **client-side Fabric performance mod for Minecraft Java 26.2**.
It focuses on safe/balanced gains: less unnecessary render work, fewer far particles,
adaptive render-distance pressure and a configurable idle FPS cap.

## Features

- Adaptive performance pressure (0-3) based on current FPS
- Far-particle reduction while preserving particles close to the player
- Distance culling for non-player entities
- Distance culling for non-global block entities
- Temporary adaptive render-distance reduction (up to 2 chunks by default)
- Restores the player's original render distance when disabled/exiting
- 15/30 FPS inactive-window limiter
- Optional HUD: FPS, frametime, RAM, entity/chunk counts and saved render work
- `/boostify`, `/boostify reload`, `/boostify stats`, `/boostify performance`
- Optional Mod Menu integration
- Compatibility detection for Sodium, Lithium, FerriteCore, ImmediatelyFast and EntityCulling
- If EntityCulling is installed, Boostify's entity-distance hook backs off in compatibility mode

## Requirements

- Minecraft Java Edition 26.2
- Java 25
- Fabric Loader 0.19.3+
- Fabric API 0.156.0+26.2

Mod Menu is optional.

## Build

The included GitHub Actions workflow builds the mod automatically on GitHub.

Locally, with Java 25 and Gradle 9.5.1 installed:

```bash
gradle build
```

The normal mod JAR is written to `build/libs/boostify-1.0.0.jar`.
Do **not** upload the `-sources.jar` to Modrinth as the primary download.

## Config

Config file: `.minecraft/config/boostify.json`

Open settings with `/boostify` or through Mod Menu.

## Notes

Boostify does not use raw OpenGL calls. That is intentional for Minecraft 26.2's rendering-backend transition.
The adaptive chunk feature changes only the client's render-distance option temporarily; it does not touch simulation distance or server settings.

## License

MIT
