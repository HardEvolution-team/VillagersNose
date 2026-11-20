# Villager's Nose (1.12.2 port layer)

Shear the noses off villagers, wear them, plant them and grow new villagers. NOT SERIOUS!
For **Minecraft 1.12.2 on Cleanroom Loader**. Requires [ArchaicLib](https://github.com/ElectrifiedDed/ArchaicLib) 1.8.0+.

> **This is not a re-upload.** The idea comes from the original *Villager's Nose* by **StarSheep** (Minecraft 1.7.10). This project contains **none of the original code or assets** (textures, sound, language files). It works as a "layer" on top of the original mod: for the textures and the music to appear you must provide the original mod yourself.

## Original mod (required for textures and music)

**Original: [Villager's Nose on CurseForge](https://www.curseforge.com/minecraft/mc-mods/villagers-nose)** — the 1.7.10 version, file `villagersnose_1.7.10_1.3c.jar`.

1. Download the original 1.7.10 jar from the link above.
2. Put it in the folder `<minecraft folder>/archaic_originals/` (create it if it does not exist). **Not** in `mods/` — a 1.7.10 jar cannot be loaded as a mod in 1.12.2.
3. Start the game. ArchaicLib reads the textures and the music straight from that jar; nothing is extracted or copied.

If the jar is missing, the game shows a window on the main menu with the link and the exact folder; the mod still works, but the nose and SuperNose textures and the SuperNose music are missing.

## Installation

1. Minecraft 1.12.2 + [Cleanroom Loader](https://github.com/CleanroomMC/Cleanroom) (Java 21+).
2. Put `archaiclib-1.8.0.jar` and `villagersnose-1.1.0.jar` into `mods/`.
3. Put the original jar into `archaic_originals/` (see above).

## Features

- Shear a villager to take its nose; the noseless villager refuses to trade until the nose grows back.
- Wear the nose as a helmet (it twitches near emeralds), or plant it on farmland to grow a new villager of the same profession.
- Craft the **SuperNose 9000** and enjoy the visions.
- Optional compatibility with Helpful Villagers (1.12.2 reimplementation): grown and restored villagers keep their type.
- Config via Forge `@Config`.

## Building

Needs a sibling folder `../ArchaicLib` built beforehand.

```
cd ../ArchaicLib && ./gradlew build
cd ../VillagersNose && ./gradlew build
```

## License

HardEvolution Restricted Source-Available License (HE-RSAL) 1.1 — see [LICENSE.en.md](LICENSE.en.md) and [LICENSE.ru.md](LICENSE.ru.md) (the Russian version prevails).
The original Villager's Nose belongs to its author, StarSheep (CC BY-NC-ND 4.0); this project neither contains nor modifies it.

Author: **electrifiedded** / [HardEvolution](https://github.com/HardEvolution-team) · [Discord](https://discord.gg/92JTPmfGSx)
