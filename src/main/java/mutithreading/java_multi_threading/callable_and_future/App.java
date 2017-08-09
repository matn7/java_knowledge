package mutithreading.java_multi_threading.callable_and_future;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Mati on 22.07.2017.
 */
public class App {

    // Return value from multithreading
    // Callable - parametrized class in <> bracket. Between bracket put the return type
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Random random = new Random();
                int duration = random.nextInt(4000);
                if (duration > 2000) {
                    throw new IOException("Sleeping for too long");
                }
                System.out.println("Starting...");
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Finished");
                return duration;
            }
        });

        executorService.shutdown();
        //future.isCancelled();
        //future.isDone();

        try {
            // get method return result when it appears or throw exception id timeout
            System.out.println("Result is: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            IOException ex = (IOException)e.getCause();
            System.out.println(ex.getMessage());
        }
    }

}
