package nlpbuddy.runtime

import arrow.dagger.effects.instances.ArrowEffectsInstances
import arrow.dagger.effects.instances.DaggerMonadSuspendSyntaxInstance
import arrow.dagger.instances.ArrowInstances
import arrow.dagger.instances.DaggerApplicativeErrorSyntaxInstance
import arrow.dagger.instances.DaggerApplicativeSyntaxInstance
import arrow.dagger.instances.DaggerMonadSyntaxInstance
import arrow.effects.ForIO
import arrow.effects.MonadSuspendSyntax
import arrow.typeclasses.ApplicativeErrorSyntax
import arrow.typeclasses.ApplicativeSyntax
import arrow.typeclasses.MonadSyntax
import dagger.Component
import dagger.Module
import dagger.Provides
import nlpbuddy.algebras.datasource.NlpDataSource
import nlpbuddy.algebras.services.Config
import nlpbuddy.algebras.services.DefaultTagService
import nlpbuddy.algebras.services.DefaultTagService_Factory
import nlpbuddy.algebras.services.TagService
import nlpbuddy.algebras.ui.Presentation
import nlpbuddy.algebras.usecase.DefaultFetchTagsUseCase
import nlpbuddy.algebras.usecase.FetchTagsUseCase
import nlpbuddy.runtime.datasource.TextRazorNlpDataSource
import nlpbuddy.runtime.presentation.ConsolePresentation
import nlpbuddy.runtime.services.SystemEnvConfig
import javax.inject.Singleton

@Module
abstract class LocalInstances<F> {
    @Provides fun applicativeSyntax(ev: DaggerApplicativeSyntaxInstance<F>): ApplicativeSyntax<F> = ev
    @Provides fun applicativeErrorSyntax(ev: DaggerApplicativeErrorSyntaxInstance<F, Throwable>): ApplicativeErrorSyntax<F, Throwable> = ev
    @Provides fun monadSyntax(ev: DaggerMonadSyntaxInstance<F>): MonadSyntax<F> = ev
    @Provides fun monadSuspendSyntax(ev: DaggerMonadSuspendSyntaxInstance<F>): MonadSuspendSyntax<F> = ev
    @Provides fun dataSource(ev: TextRazorNlpDataSource<F>): NlpDataSource<F> = ev
    @Provides fun config(ev: SystemEnvConfig<F>): Config<F> = ev
    @Provides fun tagService(ev: DefaultTagService<F>): TagService<F> = ev
    @Provides fun presentation(ev: ConsolePresentation<F>): Presentation<F> = ev
    @Provides fun fetchTagsUseCase(ev: DefaultFetchTagsUseCase<F>): FetchTagsUseCase<F> = ev
}

@Module
class IOLocalInstances : LocalInstances<ForIO>()

@Singleton
@Component(modules = [
    ArrowInstances::class,
    ArrowEffectsInstances::class,
    IOLocalInstances::class]
)
abstract class World {
    abstract fun presentation(): Presentation<ForIO>
    companion object {
        private val instances: World = DaggerWorld.create()
        fun edge(): Presentation<ForIO> = instances.presentation()
    }
}