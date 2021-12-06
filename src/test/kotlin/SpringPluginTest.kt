import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.implementation
import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpringPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("interop.spring")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("interop.base"))
    }

    @Test
    fun `includes kotlin spring plugin`() {
        assertNotNull(project.plugins.findPlugin("org.jetbrains.kotlin.plugin.spring"))
    }

    @Test
    fun `includes spring bom`() {
        // There's not an easy way to get to the dependencyManagement details, so we're going to add a dependency for
        // spring-context and verify the version
        project.dependencies.implementation("org.springframework:spring-context")

        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertHasJars("spring-context-5.3.10")
    }
}
