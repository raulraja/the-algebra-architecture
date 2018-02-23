package nlpbuddy.runtime.presentation

import arrow.Kind
import arrow.effects.MonadSuspendSyntax
import arrow.typeclasses.bindingCatch
import nlpbuddy.algebras.ui.Presentation
import nlpbuddy.algebras.usecase.FetchTagsUseCase
import nlpbuddy.text
import javax.inject.Inject

class ConsolePresentation<F> @Inject constructor(
        val useCase: FetchTagsUseCase<F>,
        val s: MonadSuspendSyntax<F>
) : Presentation<F>, MonadSuspendSyntax<F> by s {
    override fun onUserRequestedTags(paragraphs: List<String>): Kind<F, Unit> =
            monadSuspend().bindingCatch {
                val tagged = useCase.fetchTagsInParagraphs(paragraphs).bind()
                tagged.map {
                    println("""
                        |$text
                        |
                        |${it.tags.take(10).joinToString(
                            prefix = "[",
                            separator = ", ",
                            postfix = " ... ]",
                            transform = { it.value })}""".trimMargin())
                }
                Unit
            }
}