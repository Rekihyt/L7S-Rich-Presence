import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity

import java.io.File
import java.io.IOException
import java.time.Instant
import kotlin.system.exitProcess

/**
 * Demo for L7S discord rich presence
 */
class RichPresence {
    private val core: Core = initPresence()

    @Throws(IOException::class)
    fun initPresence(): Core {
        val osName = System.getProperty("os.name").lowercase();
        val sdkPath = "game-sdk/lib/x86_64/discord_game_sdk." + if (osName.contains("windows"))
            "dll"
        else if (osName.contains("linux")) {
            "so"
        } else if (osName.contains("mac"))
            "dylib"
        else exitProcess(1)

        Core.init(File(sdkPath))
        CreateParams().use { params ->
            params.clientID = 698611073133051974L
            // TODO: add discord not required flag
            params.flags = CreateParams.getDefaultFlags()
            Core(params).use { core ->
                Activity().use { activity ->
                    activity.details = "Running an example"
                    activity.state = "and having fun"

                    // Setting a start time causes an "elapsed" field to appear
                    activity.timestamps().start = Instant.now()

                    // We are in a party with 10 out of 100 people.
//                    activity.party().size().setMaxSize(100)
//                    activity.party().size().setCurrentSize(10)

                    // Make a "cool" image show up
                    activity.assets().largeImage = "images/logo.png"
                    activity.assets().smallImage = "images/frigate.png"

                    // Setting a join secret and a party ID causes an "Ask to Join" button to appear
                    // activity.party().setID("Party!")
                    // activity.secrets().setJoinSecret("Join!")

                    // Finally, update the current activity to our activity
                    core.activityManager().updateActivity(activity)
                }
                return core
            }
        }
    }

    fun runLoop() {
        // Run callbacks forever
        while (true) {
            core.runCallbacks()
            try {
                // Sleep a bit to save CPU
                Thread.sleep(16)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }
}