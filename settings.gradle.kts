pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9+"
}

rootProject.name = extra["mod.name"] as String

stonecutter.create(rootProject) {
    val fb = "fabric"; val fr = "forge"; val nf = "neoforge"
    fun match(version: String, vararg loaders: String) = loaders.forEach {
        var suffix = if (it == "forge" && sc.eval(version, "<=1.17.1")) ".arch" else ""
        version("$version-$it", version).buildscript = "scripts/$it$suffix.gradle.kts"
    }
    match("26.1.2", fb, fr, nf)
    match("1.21.1", fr, nf)
    match("1.17.1", fr)
    match("1.16.5", fb, fr)
}
