package djl.start.object_detection;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageDetect {


    public String diagnose() throws ModelException, TranslateException, IOException {
        Path imageFile = Paths.get("Images/testbild1.jpg");
        return predict(imageFile);
    }


    public String predict(Path imageFile) throws IOException, ModelException, TranslateException {
        Image img = ImageFactory.getInstance().fromFile(imageFile);

        // Sehr simple nutzung eines Vortrainierten Modells,
        // Großes Plus: intellij Autocomplete so kann man sich stets die Funktionen und Varianen der Objekte anschauen
        Criteria criteria = Criteria.builder()
                        .optApplication(Application.CV.OBJECT_DETECTION) // Art der Anwendung
                        .setTypes(Image.class, DetectedObjects.class) // Set Input und Output types
                        .optFilter("backbone", "resnet50") // Filter nach modellen mit backbone resnet50
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel model = ModelZoo.loadModel(criteria)) {

            try (Predictor predictor = model.newPredictor()) {

                DetectedObjects detection = (DetectedObjects) predictor.predict(img);
                return saveBoundingBoxImage(img, detection);
            }
        }
    }


    // Erstelle Bild mit gefundenen Objekten aus Ursprungsbild und Erkannten Objekten
    private String saveBoundingBoxImage(Image img, DetectedObjects detection) throws IOException {
        Path outputDir = Paths.get("src/main/resources");
        Files.createDirectories(outputDir);

        Image newImage = img.duplicate();
        newImage.drawBoundingBoxes(detection);

        Path imagePath = outputDir.resolve("detected.png");
        newImage.save(Files.newOutputStream(imagePath), "png");

        return "sucess";
    }

}
