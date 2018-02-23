package nlpbuddy.runtime.datasource

import arrow.Kind
import arrow.core.identity
import arrow.effects.MonadSuspendSyntax
import arrow.typeclasses.bindingCatch
import com.textrazor.TextRazor
import com.textrazor.annotations.AnalyzedText
import nlpbuddy.algebras.datasource.*
import nlpbuddy.algebras.services.Config
import java.util.*
import javax.inject.Inject

class TextRazorNlpDataSource<F> @Inject constructor(
        val config : Config<F>,
        val s: MonadSuspendSyntax<F>
) : NlpDataSource<F>, MonadSuspendSyntax<F> by s {
    override fun analyze(text: String): Kind<F, AnalysisResponse> =
            monadSuspend().bindingCatch {
                val analyzedText = runClient(text).bind()
                val response = toAnalysisResponse(analyzedText).bind()
                response
            }

    private fun runClient(text: String): Kind<F, AnalyzedText> =
            monadSuspend().bindingCatch {
                val key = config.nlpApiKey().bind()
                monadSuspend().catch ({
                    TextRazor(key.value)
                            .classified(TextRazorClassifier.NewsCodes)
                            .extract(TextRazorExtractor.Entities)
                            .extract(TextRazorExtractor.Topics)
                            .analyze(text)
                }, ::identity).bind()
            }

    private fun toAnalysisResponse(analyzedText: AnalyzedText): Kind<F, AnalysisResponse> =
            with(analyzedText.response) {
                AnalysisResponse(
                        categories.map { Category(it.label) },
                        entities.map { Entity(it.entityId) },
                        topics.map { Topic(it.label) }
                ).pure()
            }
}

sealed class TextRazorExtractor(val value: String) {
    object Entities : TextRazorExtractor("entities")
    object Topics : TextRazorExtractor("topics")
}

sealed class TextRazorClassifier(val value: String) {
    object NewsCodes : TextRazorClassifier("textrazor_newscodes")
}

fun TextRazor.extract(extractor: TextRazorExtractor): TextRazor {
    addExtractor(extractor.value)
    return this
}

fun TextRazor.classified(classifier: TextRazorClassifier): TextRazor {
    classifiers = Arrays.asList(classifier.value)
    return this
}