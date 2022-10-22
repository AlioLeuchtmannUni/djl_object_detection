package djl.start.object_detection;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DjlObjectDetectionApplication {

    public static void main(String[] args) {

        SpringApplication.run(DjlObjectDetectionApplication.class, args);

        ImageDetect imageDetectTest = new ImageDetect();
        try {
            String res = imageDetectTest.diagnose();
            System.out.println(res);
        }catch (Exception e){
            System.out.println("Fehler aufgetreten:");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

}
