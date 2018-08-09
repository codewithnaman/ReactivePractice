import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class OperatorTest {

    @Test
    public void operatorMap() {
        Flux.range(0, 9).map(s -> s * 10).log().subscribe();
        /*
            Transform the events and provide to subscriber. Like the streams API.
         */
    }

    @Test
    public void operatorFlatMap() {
        Flux.range(0, 9).flatMap(s -> Flux.range(s * 10, 2)).log().subscribe();
        /*
            Above one is synchronous operation which return a single element, If we want to transform a value into
            multiple than we use flatmap as shown in above example the flatmap produces two output for each
            input.
        */
    }

    @Test
    public void operatorFlatMapMany() {
        Mono.just(3).flatMapMany(s -> Flux.range(1, s)).log().subscribe();
        /*
            Takes a mono perform operation and generate flux events.
         */
    }

    @Test
    public void operatorConcat() throws InterruptedException {
        Flux<Integer> zeroToFour = Flux.range(0,5).delayElements(Duration.ofMillis(200));
        Flux<Integer> fiveToTen = Flux.range(5,8).delayElements(Duration.ofMillis(400));
        Flux.concat(zeroToFour,fiveToTen).log().subscribe();
        //zeroToFour.concatWith(fiveToTen).subscribe(System.out::println);
        Thread.sleep(10000);
        /*
            The concat operation combines in sequential way. While merge interwaves the values.

            [ INFO] (main) onSubscribe(FluxConcatArray.ConcatArraySubscriber)
            [ INFO] (main) request(unbounded)
            [ INFO] (parallel-1) onNext(0)
            [ INFO] (parallel-2) onNext(1)
            [ INFO] (parallel-3) onNext(2)
            [ INFO] (parallel-4) onNext(3)
            [ INFO] (parallel-5) onNext(4)
            [ INFO] (parallel-6) onNext(5)
            [ INFO] (parallel-7) onNext(6)
            [ INFO] (parallel-8) onNext(7)
            [ INFO] (parallel-1) onNext(8)
            [ INFO] (parallel-2) onNext(9)
            [ INFO] (parallel-3) onNext(10)
            [ INFO] (parallel-4) onNext(11)
            [ INFO] (parallel-5) onNext(12)
            [ INFO] (parallel-5) onComplete()
        */
    }

    @Test
    public void operatorMerge() throws InterruptedException {
        Flux<Integer> zeroToFour = Flux.range(0, 5).delayElements(Duration.ofMillis(200));
        Flux<Integer> fiveToTen = Flux.range(5, 8).delayElements(Duration.ofMillis(400));
        Flux.merge(zeroToFour, fiveToTen).log().subscribe();
        //zeroToFour.mergeWith(fiveToTen).subscribe(System.out::println);
        Thread.sleep(10000);
        /*
            [ INFO] (main) onSubscribe(FluxFlatMap.FlatMapMain)
            [ INFO] (main) request(unbounded)
            [ INFO] (parallel-1) onNext(0)
            [ INFO] (parallel-2) onNext(5)
            [ INFO] (parallel-3) onNext(1)
            [ INFO] (parallel-5) onNext(2)
            [ INFO] (parallel-4) onNext(6)
            [ INFO] (parallel-6) onNext(3)
            [ INFO] (parallel-8) onNext(4)
            [ INFO] (parallel-7) onNext(7)
            [ INFO] (parallel-1) onNext(8)
            [ INFO] (parallel-2) onNext(9)
            [ INFO] (parallel-3) onNext(10)
            [ INFO] (parallel-4) onNext(11)
            [ INFO] (parallel-5) onNext(12)
            [ INFO] (parallel-5) onComplete()
        * */
    }

    @Test
    public void operatorZip(){
        Flux<Integer> zeroToFour = Flux.range(0, 5);
        Flux<Integer> fiveToTen = Flux.range(5, 8);
        Flux.zip(zeroToFour,fiveToTen,(item1,item2) -> item1+","+item2).subscribe(System.out::println);
        //zeroToFour.zipWith(fiveToTen).log().subscribe();
        /*
            0,5
            1,6
            2,7
            3,8
            4,9
        */
    }

}
