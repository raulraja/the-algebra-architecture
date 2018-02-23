package nlpbuddy.algebras.services

import arrow.Kind
import arrow.TC
import arrow.typeclass

data class NlpApiKey(val value: String)

@typeclass interface Config<F> : TC {
    fun nlpApiKey(): Kind<F, NlpApiKey>
}