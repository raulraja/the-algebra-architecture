package nlpbuddy.algebras.usecase

import arrow.Kind
import arrow.TC
import arrow.core.identity
import arrow.data.k
import arrow.typeclass
import arrow.typeclasses.MonadSyntax
import nlpbuddy.algebras.services.AnalysisRequest
import nlpbuddy.algebras.services.Tag
import nlpbuddy.algebras.services.TagService
import javax.inject.Inject

data class TaggedParagraph(val text: String, val tags: List<Tag>)

@typeclass interface FetchTagsUseCase<F>: TC {
    fun fetchTagsInParagraphs(paragraphs: List<String>): Kind<F, List<TaggedParagraph>>
}

class DefaultFetchTagsUseCase<F> @Inject constructor(
        val tagService: TagService<F>,
        val s: MonadSyntax<F>
) : FetchTagsUseCase<F>, MonadSyntax<F> by s {

    override fun fetchTagsInParagraphs(paragraphs: List<String>): Kind<F, List<TaggedParagraph>> {
        return paragraphs.k()
                .map { tagService.tag(AnalysisRequest(it)) }
                .traverse(::identity, monad())
                .map { tagGroups ->
                    paragraphs.mapIndexed { n, text -> TaggedParagraph(text, tagGroups[n])}
                }
    }

}