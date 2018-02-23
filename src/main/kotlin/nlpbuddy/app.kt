package nlpbuddy

import arrow.effects.fix
import nlpbuddy.runtime.World

val text = """
    |GSC, formerly known as Girl Scout Cookies, is an OG Kush and Durban Poison hybrid cross whose reputation grew too
    |large to stay within the borders of its California homeland. With a sweet and earthy aroma, GSC launches you to
    |euphoria’s top floor where full-body relaxation meets a time-bending cerebral space. A little goes a long way with
    |this hybrid, whose THC heights have won GSC numerous Cannabis Cup awards. Patients needing a strong dose of relief,
    |however, may look to GSC for severe pain, nausea, and appetite loss.
    |There are several different phenotypes of the GSC strain including Thin Mint and Platinum Cookies,
    |which exhibit some variation in appearance and effect. Typically, however, GSC expresses its beauty in twisting
    |green calyxes wrapped in purple leaves and fiery orange hairs. Patients and consumers looking to cultivate this
    |cannabis staple themselves should wait 9 to 10 weeks for their indoor plants to finish flowering.
    """.trimMargin()

object NLPBuddyApp {
    @JvmStatic
    fun main (args: Array<String>) {
        val program = World.edge().onUserRequestedTags(listOf(text))
        program.fix().unsafeRunSync()
    }
}