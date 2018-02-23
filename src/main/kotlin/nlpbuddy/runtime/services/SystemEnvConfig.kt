package nlpbuddy.runtime.services

import arrow.Kind
import arrow.typeclasses.ApplicativeErrorSyntax
import nlpbuddy.algebras.services.Config
import nlpbuddy.algebras.services.NlpApiKey
import javax.inject.Inject

const val NLP_SERVICE_KEY_ENV_VAR = "NLP_SERVICE_KEY"

class SystemEnvConfig<F> @Inject constructor(
        val s: ApplicativeErrorSyntax<F, Throwable>
) : Config<F>, ApplicativeErrorSyntax<F, Throwable> by s {

    val key = System.getenv(NLP_SERVICE_KEY_ENV_VAR)

    override fun nlpApiKey(): Kind<F, NlpApiKey> =
        if (key.isBlank()) IllegalStateException("No env var value provided for $NLP_SERVICE_KEY_ENV_VAR").raiseError()
        else NlpApiKey(key).pure()



}