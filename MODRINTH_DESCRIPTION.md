# Boostify ⚡

**Boostify** is a lightweight client-side Fabric performance mod for Minecraft **26.2**.
It is designed around a safe/balanced preset: improve FPS and frametime consistency without turning Minecraft into a completely different-looking game.

### What Boostify optimizes

- Reduces unnecessary distant particle work
- Culls distant non-player entities and block entities
- Adapts its own optimization strength when FPS drops
- Can temporarily reduce render distance by a small amount under heavy load
- Limits background Minecraft to 30 or 15 FPS
- Includes an optional FPS / frametime / RAM / entity / chunk overlay
- Automatically detects common performance mods and avoids one obvious duplicate culling path

### Commands

`/boostify` – open settings  
`/boostify stats` – show current performance stats  
`/boostify reload` – reload config  
`/boostify performance` – apply the Performance preset

### Compatibility

Designed to run alongside mods such as **Sodium**, **Lithium**, **FerriteCore** and **ImmediatelyFast**. Mod Menu is optional.

### Requirements

Minecraft 26.2 • Fabric Loader 0.19.3+ • Fabric API • Java 25
