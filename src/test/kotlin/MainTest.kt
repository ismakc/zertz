import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object MainTest : Spek({
    describe("The Main") {
        it("should say hello World") {
            assertEquals("Hello, world!", getHelloString())
        }
    }
})
