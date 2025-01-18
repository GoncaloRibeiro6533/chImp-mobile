package pt.isel.chimp.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import pt.isel.chimp.ChImpTestApplication

@Suppress("unused")
class ChImpInstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ChImpTestApplication::class.java.name, context)
    }
}