plugins {
    id("me.modmuss50.mod-publish-plugin")
    id("dev.kikugie.fletching-table")
    id("com.bizcub.multiloader")
}

multiloader {
    val isClothConfigAvailable = !(isForge && scp > "1.21.3")

    sc.constants["is_cloth_config_available"] = isClothConfigAvailable

    sc.replacements {
        string(scp >= "26.2") {
            replace(".setScreen(", ".gui.setScreen(")
        }
        string(scp >= "26.1", "!graphics") {
            replace("GuiGraphics", "GuiGraphicsExtractor")
        }
        string(scp >= "1.21.11" && !isForge, "auto_config") {
            replace("AutoConfig", "AutoConfigClient")
        }
        string(scp >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }
        string(scp >= "1.19.4") {
            replace("CreateWorldScreen.SelectedGameMode", "WorldCreationUiState.SelectedGameMode")
        }
    }

    setMREnvironment(mrEnvs.clientOnly)
    setCFEnvironment(cfEnvs.client)

    addDependency(
        dependency = "me.shedaniel.cloth:cloth-config-${mod.loader}:${getDep("cloth-config").split("+").first()}",
        configuration = if (isClothConfigAvailable) "implementation" else "compileOnly",
        repository = "maven.shedaniel.me",
        excludedModules = listOf("net.fabricmc.fabric-api"),
        isPublishDepEnabled = isClothConfigAvailable,
        publishProjectId = "cloth-config"
    )

    if (isFabric) {
        addDependency(
            dependency = "net.fabricmc:fabric-loader:${getDep("fabric")}"
        )
        addDependency(
            dependency = "net.fabricmc.fabric-api:fabric-api:${getDep("fabric-api")}",
            isPublishDepEnabled = true,
            isPublishDepRequired = true
        )
        addDependency(
            dependency = "com.terraformersmc:modmenu:${getDep("modmenu")}",
            repository = "maven.terraformersmc.com/releases",
            excludedModules = listOf("eu.pb4"),
            isPublishDepEnabled = true
        )
    }
}
