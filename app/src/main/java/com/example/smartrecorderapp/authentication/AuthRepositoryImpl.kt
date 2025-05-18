package com.example.smartrecorderapp.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

/*
Перешли к паттерну Repo -> ViewModel -> UI.
Этот подход позволяет оставить ViewModel лишь ответственность за изменение UI,
а также добавить коду масштабируемости - Repo можно использовать во многих ViewModel
 */

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository  {


    /*
    Перешли к функциям suspend, чтобы использовать await - безопасный вариант получения данных из
    асинхронных функций, этот метод использует suspendCancellableCoroutine*
        -----
        Отличие от suspendCoroutine
        в том, что обрабатывает случаи отмены корутины, например, пользователь, когда, выходит с экрана,
        где "жила" viewModel, где была запущена корутина (процесс связи с Firebase будет остановлен.
        В целом suspencCoroutine и suspendCancellableCoroutine используются для того, чтобы «подвесить»
        выполнение корутины, пока не произойдёт какое-то событие (например, ответ от сервера, результат
        из базы данных и т. д.
        -----

    и addOnCompleteListener, для подписки на Task*,
        -----
        Ассинхронный тип данных из Firebase, представляющий операцию, результат которой будет доступен
        в будущем. Именно этот тип используется как it в Listener-ах (onSuccessListener), то есть в колбеках,
        но колбеки менее предпочтительны, так как

        * плохо отлаживаются, читаются, дебагаются
        * нельзя использовать try-catch (в нормальном виде, так как в этом случае try-catch будет в каждом
        отдельном listener, а не в целом на результат, что создат нагромождение.
        * нет async, await
        * нельзя прервать

        Поэтому существует библиотека kotlinx-coroutines-play-services, она добавляет для Task функцию-расширение await(),
        чтобы использовать его в suspend-функциях
        -----

    внутри себя, оборачивая в асинхронную функцию
    работу с сетью, ожидающую результат
    когда корутина  чтобы приостановить корутину
    также используем Result - тип, улучшающий работу с try-catch, в нем мы
    храним результат при удачном завершении или ошибку, при появлении исключения
     */

    /*
    Обычный flow не может работать с асинхронными коллбэками:
    ----
        // Так не получится!
        flow {
            FirebaseAuth.getInstance().addAuthStateListener { auth ->
                emit(auth.currentUser) // Ошибка: emit нельзя вызывать из другого потока
            }
        }
    ----
    callbackFlow решает эту проблему через trySend.
    callbackFlow — это мост между:
        Коллбэк-ориентированными API (Firebase, Android SDK)
        Реактивными потоками Kotlin (Flow)

    Его структура всегда следует шаблону:
    1. Создать listener → 2. Зарегистрировать → 3. Отправить данные → 4. Очистить в awaitClose.
    Используйте его везде, где нужно преобразовать события в Flow.
     */

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySend( it.currentUser )
        }
        auth.addAuthStateListener( listener )
        awaitClose {
            auth.removeAuthStateListener( listener )
        }
    }

    override suspend fun logIn(request: AuthRequest): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword( request.email, request.password ).await()
            Result.success(authResult.user!!)
        }
        catch ( e: Exception ) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: AuthRequest): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword( request.email, request.password ).await()
            Result.success(authResult.user!!)
        }
        catch ( e: Exception ) {
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        auth.signOut()
    }
}