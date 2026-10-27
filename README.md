<div align="center">

<h1>Armor Indication</h1>
<hr>

**Client-side Fabric mod that marks netherite armor slots by enchantment in inventory screens**

![Minecraft](https://img.shields.io/badge/MINECRAFT-1.21.4-62B47A?style=flat-square&logo=minecraft&logoColor=white)
![Fabric](https://img.shields.io/badge/FABRIC_LOADER-0.16.10-DBD0B4?style=flat-square)
![Java](https://img.shields.io/badge/JAVA-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![License](https://img.shields.io/github/license/hhhhhffd/armor-indication-mod?style=flat-square)

[Build](#build) · [Install](#install) · [License](#license)

</div>

> [!NOTE]
> Archived — no longer maintained.

## What it is

Armor Indication replaces the icons of netherite armor pieces in container and inventory screens depending on their enchantments, for example telling apart pieces with and without Mending. It only changes how slots are drawn; game mechanics and server state are untouched. No release was published; build the JAR from source.

## How it works

A Mixin into `HandledScreen` redirects the `drawItem` calls made inside `drawSlot`. `NetheriteArmorEnchantmentUtil` resolves a variant (`SPECIAL`, `MENDING` or `NONE`) from the stack's enchantments, and `NetheriteArmorTextureManager` returns a replacement texture for it. If none applies, the vanilla call runs, so durability bars and stack counts stay vanilla. Textures for helmet, chestplate, leggings and boots (`_special` and `_special_mending`) are in `src/main/resources/assets/armor_indication/textures/item/`.

## Stack

| Part | Version |
| --- | --- |
| Minecraft | 1.21.4 |
| Fabric Loader | >= 0.16.10 |
| Fabric API | >= 0.119.4+1.21.4 |
| Java | 21 |
| Mod version | 1.0.0 |
| Code injection | SpongePowered Mixin |
| Build | Gradle, Fabric Loom 1.9.2 |

## Build

```bash
git clone https://github.com/hhhhhffd/armor-indication-mod.git
cd armor-indication-mod
./gradlew build
```

The JAR is written to `build/libs/`.

## Install

Put the JAR and Fabric API into the `mods` folder of your Minecraft 1.21.4 profile. The mod is client-side only and has no configuration.

## License

MIT, see [LICENSE](LICENSE).
