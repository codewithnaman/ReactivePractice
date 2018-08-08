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
}
