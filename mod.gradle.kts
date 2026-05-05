import com.bizcub.multiloader.MultiLoader
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import me.modmuss50.mpp.ModPublishExtension

val stonecutter = project.extensions.getByType(StonecutterBuildExtension::class.java)

project.extensions.configure<MultiLoader>("multiloader") {
    project.afterEvaluate {
        stonecutter.let { sc ->
//            sc.constants["is_cloth_config_available"] = isClothConfigAvailable
        }
    }

//    addRepository("https://maven.shedaniel.me")
//    addDependency("api", "me.shedaniel.cloth:cloth-config-${mod.loader}:${getDep("cloth-config")?.split("+")?.first()}")

    if (isFabric) {
//        addRepository("https://maven.terraformersmc.com/releases")

        addDependency("implementation", "net.fabricmc:fabric-loader:${getDep("fabric")}")
//        addDependency("api", "com.terraformersmc:modmenu:${getDep("modmenu")}")
    }

    if (isNeoForge) {
        addRepository("https://maven.neoforged.net/releases")
    }

    project.extensions.configure<ModPublishExtension>("publishMods") {
        modrinth {
//            if (isClothConfigAvailable) optional("cloth-config")
//            if (isFabric) optional("modmenu")
        }
        curseforge {
//            if (isClothConfigAvailable) optional("cloth-config")
//            if (isFabric) optional("modmenu")
        }
    }
}
