import com.bizcub.multiloader.MultiLoader
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import me.modmuss50.mpp.ModPublishExtension

apply(plugin = "dev.kikugie.fletching-table")

val stonecutter = project.extensions.getByType(StonecutterBuildExtension::class.java)

project.extensions.configure<MultiLoader>("multiloader") {
    access()

    project.afterEvaluate {
        stonecutter.let { sc ->
            sc.constants["is_cloth_config_available"] = isClothConfigAvailable

            sc.replacements {
                string(scp >= "26.1", "!graphics") {
                    replace("GuiGraphics", "GuiGraphicsExtractor")
                }
                string(scp >= "1.21.11" && !isForge, "auto_config") {
                    replace("AutoConfig", "AutoConfigClient")
                }
            }
        }
    }

    addRepositoryWithDependency("maven.shedaniel.me", "api", "me.shedaniel.cloth:cloth-config-${mod.loader}:${getDep("cloth-config")?.split("+")?.first()}")

    if (isFabric) {
        addDependency("implementation", "net.fabricmc:fabric-loader:${getDep("fabric")}")
        addDependency("implementation", "net.fabricmc.fabric-api:fabric-api:${getDep("fabric-api")}")
        addRepositoryWithDependency("maven.terraformersmc.com/releases", "api", "com.terraformersmc:modmenu:${getDep("modmenu")}")
    }

    if (isNeoForge) {
        addRepository("maven.neoforged.net/releases")
    }

    project.extensions.configure<ModPublishExtension>("publishMods") {
        modrinth {
            if (isClothConfigAvailable) optional("cloth-config")
            if (isFabric) requires("fabric-api")
            if (isFabric) optional("modmenu")
        }
        curseforge {
            if (isClothConfigAvailable) optional("cloth-config")
            if (isFabric) requires("fabric-api")
            if (isFabric) optional("modmenu")
        }
    }
}
