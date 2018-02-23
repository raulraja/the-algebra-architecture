package nlpbuddy.algebras.datasource

import arrow.Kind
import arrow.TC
import arrow.typeclass

data class Category(val value: String)
data class Entity(val value: String)
data class Topic(val value: String)

data class AnalysisResponse(
        val categories: List<Category>,
        val entities: List<Entity>,
        val topics: List<Topic>
)

@typeclass interface NlpDataSource<F> : TC {
    fun analyze(text: String): Kind<F, AnalysisResponse>
}