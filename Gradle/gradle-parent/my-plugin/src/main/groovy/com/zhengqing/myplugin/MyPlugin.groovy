package com.zhengqing.myplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task("myPlugin") {
            doLast {
                println("hi myPlugin ...")
            }
        }
    }
}