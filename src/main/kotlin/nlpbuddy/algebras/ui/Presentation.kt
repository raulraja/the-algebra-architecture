package nlpbuddy.algebras.ui

import arrow.Kind
import arrow.TC
import arrow.typeclass

@typeclass
interface Presentation<F> : TC {
    fun onUserRequestedTags(paragraphs: List<String>): Kind<F, Unit>
}