import org.junit.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    public void createMono(){
        Mono.just("A").log().subscribe();
        //Mono.just("A") - creates and event, nothing happens with it until and unless no one subscribe it
        //Mono.just("A").log()  - enables the logging if any logging framework is enabled otherwise use console for logging.
        //Mono.just("A").log().subscribe() - this will subscribe the method and will consume our input and you can see calls in console
        /*  [ INFO] (main) | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
            [ INFO] (main) | request(unbounded)
            [ INFO] (main) | onNext(A)
            [ INFO] (main) | onComplete()*/
    }
}
