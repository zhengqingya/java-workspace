package com.zhengqing.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildSrcPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task("taskBuildSrc") {
            doLast {
                println("hi buildSrc ...")
            }
        }
    }
}