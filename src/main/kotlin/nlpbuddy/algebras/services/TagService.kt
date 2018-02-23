package nlpbuddy.algebras.services

import arrow.Kind
import arrow.TC
import arrow.data.ListK
import arrow.data.applicative
import arrow.data.fix
import arrow.data.k
import arrow.syntax.applicative.map
import arrow.typeclass
import arrow.typeclasses.MonadSyntax
import nlpbuddy.algebras.datasource.NlpDataSource
import javax.inject.Inject

data class AnalysisRequest(val text: String)
data class Tag(val value: String)

@typeclass
interface TagService<F> : TC {

    fun tag(request: AnalysisRequest): Kind<F, ListK<Tag>>

}

class DefaultTagService<F> @Inject constructor(
        val dataSource: NlpDataSource<F>,
        val s: MonadSyntax<F>
) : TagService<F>, MonadSyntax<F> by s {

    override fun tag(request: AnalysisRequest): Kind<F, ListK<Tag>> =
            dataSource.analyze(request.text).map {
                with(it) {
                    ListK.applicative().map(categories.k(), entities.k(), topics.k(), { (category, entity, topic) ->
                        listOf(Tag(category.value), Tag(entity.value), Tag(topic.value))
                    }).fix().flatten().distinct().k()
                }
            }

}