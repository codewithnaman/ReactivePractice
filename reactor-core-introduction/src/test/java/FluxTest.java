import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class FluxTest {

    //Error methods like doOnError,onErrorResume also applicable and work in same way as mono.

    private String[] array = {"A","B","C"};

    @Test
    public void createFluxWithJust(){
        Flux.just("A","B","C").log().subscribe();
        /*
        This publish the elements and subscriber call onNext method and consume the element.
        [DEBUG] (main) Using Console logging
        [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
        [ INFO] (main) | request(unbounded)
        [ INFO] (main) | onNext(A)
        [ INFO] (main) | onNext(B)
        [ INFO] (main) | onNext(C)
        [ INFO] (main) | onComplete()
        */
    }

    @Test
    public void createFluxWithList(){
        Flux.just(Arrays.asList(array)).log().subscribe();
        /* When we pass list it is consumed as an object not the each element single time.
           To do so we use the Iterable method shown in below test.
        [ INFO] (main) | onSubscribe([Fuseable] FluxJust.WeakScalarSubscription)
        [ INFO] (main) | request(unbounded)
        [ INFO] (main) | onNext([A, B, C])
        [ INFO] (main) | onComplete()
         */
    }

    @Test
    public void createFluxWithIterable(){
        Flux.fromIterable(Arrays.asList(array)).log().subscribe();
        /*Output same as test case createFluxWithJust()*/
    }

    @Test
    public void createFluxWithRange(){
        Flux.range(0,3).log().subscribe();
        /*  Publish range of numbers from the number provided to the count as second argument.
        [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
        [ INFO] (main) | request(unbounded)
        [ INFO] (main) | onNext(0)
        [ INFO] (main) | onNext(1)
        [ INFO] (main) | onNext(2)
        [ INFO] (main) | onComplete()
         */
    }

    @Test
    public void createFluxForTimeDuration() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1)).log().subscribe();
        Thread.sleep(5000);
        /*  This will generate the numbers in given duration.
            This flux never completes as this is continuous running and exit when program exit
            that's why onComplete method is not called.
            [ INFO] (main) onSubscribe(FluxInterval.IntervalRunnable)
            [ INFO] (main) request(unbounded)
            [ INFO] (parallel-1) onNext(0)
            [ INFO] (parallel-1) onNext(1)
            [ INFO] (parallel-1) onNext(2)
            [ INFO] (parallel-1) onNext(3)
            [ INFO] (parallel-1) onNext(4)
        */
    }

    @Test
    public void fluxWithRangeAndRestriction() throws InterruptedException{
        Flux.interval(Duration.ofSeconds(1)).take(2).log().subscribe();
        Thread.sleep(5000);
        /*  Take will limit the output and then onComplete will be called.
        [ INFO] (main) onSubscribe(FluxTake.TakeSubscriber)
        [ INFO] (main) request(unbounded)
        [ INFO] (parallel-1) onNext(0)
        [ INFO] (parallel-1) onNext(1)
        [ INFO] (parallel-1) onComplete()
        */
    }

    @Test
    public void fluxWithBackPressure(){
        Flux.range(0,9).log().subscribe(null,null,null,
                s-> s.request(3));
        /*
        Request implements the back pressure mechanism, then it consumes the only number of requested
        elements through subscription, But since all elements which publisher published have not
        consumed so onComplete method is not called. To consume all the elements published we use custom
        subscriber as shown in next test.
        [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
        [ INFO] (main) | request(3)
        [ INFO] (main) | onNext(0)
        [ INFO] (main) | onNext(1)
        [ INFO] (main) | onNext(2)
         */
    }

    @Test
    public void fluxWithSubscriber(){
        Flux.range(0,10).log().subscribe(new BaseSubscriber<Integer>() {
            int elementToProcess = 3;
            int counter = 0;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("On Subscribe called"+subscription);
                subscription.request(elementToProcess);
            }

            @Override
            protected void hookOnNext(Integer value) {
                counter++;
                if(counter==elementToProcess){
                    counter = 0;
                    Random random = new Random();
                    elementToProcess = random.ints(3,5).findFirst().getAsInt();
                    request(elementToProcess);
                }
            }
        });
        /*
            Backpressure mechanism for control number of events for consumption. As in below log
            shows request with different number. When request called then that number of events will
            be published and onNext called when number of events are consumed then request call for
            next events.

            [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
            On Subscribe calledreactor.core.publisher.FluxPeekFuseable$PeekFuseableSubscriber@4157f54e
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(0)
            [ INFO] (main) | onNext(1)
            [ INFO] (main) | onNext(2)
            [ INFO] (main) | request(4)
            [ INFO] (main) | onNext(3)
            [ INFO] (main) | onNext(4)
            [ INFO] (main) | onNext(5)
            [ INFO] (main) | onNext(6)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(7)
            [ INFO] (main) | onNext(8)
            [ INFO] (main) | onNext(9)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onComplete()
        */
    }

    @Test
    public void fluxWithLimit(){
        Flux.range(0,10).log().limitRate(3).subscribe();
        /*
            Flux with limitRate has same implementation as above custom subscriber.

            [ INFO] (main) | onSubscribe([Synchronous Fuseable] FluxRange.RangeSubscription)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(0)
            [ INFO] (main) | onNext(1)
            [ INFO] (main) | onNext(2)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(3)
            [ INFO] (main) | onNext(4)
            [ INFO] (main) | onNext(5)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(6)
            [ INFO] (main) | onNext(7)
            [ INFO] (main) | onNext(8)
            [ INFO] (main) | request(3)
            [ INFO] (main) | onNext(9)
            [ INFO] (main) | onComplete()
        */
    }

}
