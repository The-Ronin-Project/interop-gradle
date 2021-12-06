import gradle.kotlin.dsl.accessors._3081ed7e6bb658519cc365c772992eb9.sourceSets
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KtorPluginTest {
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply("interop.ktor")
    }

    @Test
    fun `includes interop base plugin`() {
        assertNotNull(project.plugins.findPlugin("interop.base"))
    }

    @Test
    fun `includes Ktor dependencies`() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        // These names are slightly different because of how ktor resolves its dependencies to system specific types
        mainSourceSet.compileClasspath.assertHasJars(
            "ktor-client-cio-jvm-1.6.3",
            "ktor-client-core-jvm-1.6.3",
            "ktor-client-jackson-jvm-1.6.3",
            "ktor-auth-jwt-jvm-1.6.3"
        )

        val testSourceSet = project.sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME)
        testSourceSet.compileClasspath.assertHasJars("ktor-client-mock-jvm-1.6.3")
    }

    @Test
    fun `excludes legacy JUnit`() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        mainSourceSet.compileClasspath.assertNoJarsStartingWith("junit")
    }
}
