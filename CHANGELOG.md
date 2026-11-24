# Changelog

## 1.1.2
### Changed
- The original mod file is now downloaded automatically on first start (needs ArchaicLib 1.42.0+): sources and mirrors are listed in `config/archaiclib/originals/` and can be edited there. If the download fails, a banner on the main menu links to the manual download.

## 1.1.1
### Changed
- Performance: uses ArchaicLib 1.40.0 FastRandom instead of java.util.Random, plus the 1.40.0 cached block-state / allocation-free legacy world calls. Requires ArchaicLib 1.40.0+.

## 1.1.0
First release of the 1.12.2 port (Cleanroom Loader). Based on Villager's Nose 1.3c for 1.7.10 by StarSheep.

- Shear noses, wear them, plant them and grow new villagers (profession is kept).
- Ships no original assets: textures and music load from the original jar in `archaic_originals/`.
- Optional Helpful Villagers support: noses grow helpful villagers, sheared ones keep job and inventory.
