package mutithreading.simulations_and_multithreading.semaphores.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

enum Downloader { // enum is thread safe
    INSTANCE;
    // Semaphore(int permits, boolean fair)
    private Semaphore semaphore = new Semaphore(3, true);
    public void downloadData() {
        try {
            semaphore.acquire();
            download();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private void download() {
        System.out.println("Downloading data ... ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class App {
    public static void main(String[] args) {
        // Executors.newCachedThreadPool()
        // before starting a job it going to check whether there are any threads that finished the job, reuse them
        ExecutorService executorService = Executors.newCachedThreadPool(); // dynamically reuse threads

        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Downloader.INSTANCE.downloadData();
                }
            });
        }
        executorService.shutdown();

    }
}
