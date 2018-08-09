import org.junit.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    public void createMono() {
        Mono.just("A").log().subscribe();
        //Mono.just("A") - creates and event, nothing happens with it until and unless no one subscribe it
        //Mono.just("A").log()  - enables the logging if any logging framework is enabled otherwise use console for logging.
        //Mono.just("A").log().subscribe() - this will subscribe the method and will consume our input and you can see calls in console
        /*  [ INFO] (main) | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
            [ INFO] (main) | request(unbounded)
            [ INFO] (main) | onNext(A)
            [ INFO] (main) | onComplete()*/
    }

    @Test
    public void createMonoWithCustomConsumer() {
        Mono.just("A").log().subscribe(s ->
                System.out.println(s + " is pushed to client")
        );
        /* After each event onNext is called which uses consumer to perform custom task.
          [ INFO] (main) | onNext(A)
           A is pushed to client
          [ INFO] (main) | onComplete()
         */
    }

    @Test
    public void createMonoWithMethodCalls(){
        Mono.just("A").log().doOnSubscribe(
                subscription -> System.out.println("On subscribe Called "+subscription.toString())
        ).doOnSuccess(
                s -> System.out.println("On success called "+ s.toString())
        ).doOnError(
                s -> System.out.println("On error called "+s.toString())
        ).doOnRequest(
                s -> System.out.println("On request called "+s)
        ).subscribe( System.out::println);
        /*
            [ INFO] (main) | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
            On subscribe Called reactor.core.publisher.FluxPeekFuseable$PeekFuseableConditionalSubscriber@d7b1517
            On request called 9223372036854775807
            [ INFO] (main) | request(unbounded)
            [ INFO] (main) | onNext(A)
            On success called A
            [ INFO] (main) | onComplete()
        */
    }

    @Test
    public void createEmptyMono(){
        Mono.empty().log().subscribe(System.out::println);
    }

    @Test
    public void createEmptyMonoWithSubscriberConstructor(){
        Mono.empty().log().subscribe(
                System.out::println,
                e -> System.out.println("Error occured while consuming"),
                () -> System.out.println("Consumer has been completed"),
                s -> System.out.println("Subscription consumer called"+s.toString())
        );
        /* Consumer complete called since there is no element to consumer if there is element then after consumption
           onComplete will be called.
        [DEBUG] (main) Using Console logging
        [ INFO] (main) onSubscribe([Fuseable] Operators.EmptySubscription)
        Subscription consumer calledreactor.core.publisher.FluxPeek$PeekSubscriber@11531931
        [ INFO] (main) onComplete()
        Consumer has been completed
        */
    }

    @Test
    public void createErrorMonoWithSubscriberConstructor(){
        Mono.error(new RuntimeException()).subscribe(
                System.out::println,
                e -> System.out.println("Error occured while consuming"),
                () -> System.out.println("Consumer has been completed"),
                s -> System.out.println("Subscription consumer called"+s.toString())
        );
        /*  Error is thrown and subscription is cancelled so on complete is not called because
            subscription is cancelled before the consumption of all elements.
        [DEBUG] (main) Using Console logging
        Subscription consumer calledreactor.core.publisher.Operators$EmptySubscription@357246de
        Error occured while consuming
        */
    }

    @Test
    public void MonoErrorLog(){
        Mono.error(new Exception()).log().subscribe();
    }

    @Test
    public void doOnErrorMono(){
        Mono.error(new Exception())
                .doOnError(e -> System.out.println("On error "+e)).log().subscribe();
        /* When an exception occured than doOnError function can be called. But it will not resume
           the flow after exception handling, It still cancels the subscription.
            [DEBUG] (main) Using Console logging
            [ INFO] (main) onSubscribe(FluxPeek.PeekSubscriber)
            [ INFO] (main) request(unbounded)
            On error java.lang.Exception
            [ERROR] (main) onError(java.lang.Exception)
            [ERROR] (main)  - java.lang.Exception
            java.lang.Exception
                at MonoTest.doOnErrorMono(MonoTest.java:98)
	*/
    }

    @Test
    public void OnErrorResume(){
        Mono.error(new Exception()).onErrorResume(s-> {
            System.out.println("Error occured returning B as output");
            return Mono.just("B");}).log().subscribe();
        /*  Exception is thrown which is logged and a default element will be returned. On which onNext is
        called and subscription continues and cancelled when completed.
        [DEBUG] (main) Using Console logging
        [ INFO] (main) onSubscribe(FluxOnErrorResume.ResumeSubscriber)
        [ INFO] (main) request(unbounded)
        Error occured returning B as output
        [ INFO] (main) onNext(B)
        [ INFO] (main) onComplete()
        */
    }

    @Test
    public void onErrorReturnMono(){
        Mono.error(new Exception()).onErrorReturn("B").log().subscribe();
        /*
        When an exception occured it directly return the element without logging and resume
        publisher and subscriber.
        [DEBUG] (main) Using Console logging
        [ INFO] (main) onSubscribe(FluxOnErrorResume.ResumeSubscriber)
        [ INFO] (main) request(unbounded)
        [ INFO] (main) onNext(B)
        [ INFO] (main) onComplete()
         */
    }

    


}
