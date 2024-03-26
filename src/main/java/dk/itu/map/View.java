// // package dk.itu.map;

// import dk.itu.map.structures.Way;
// import javafx.geometry.Point2D;

// // import javafx.fxml.FXMLLoader;
// // import javafx.geometry.Point2D;

// // import javafx.stage.Stage;
// // import javafx.scene.Parent;
// // import javafx.scene.Scene;
// // import javafx.scene.paint.Color;
// // import javafx.scene.canvas.Canvas;
// // import javafx.scene.transform.Affine;
// // import javafx.scene.canvas.GraphicsContext;
// // import javafx.scene.control.Button;
// // import javafx.scene.transform.NonInvertibleTransformException;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;

// public class View {
//     Canvas canvas = new Canvas(640, 480);
//     GraphicsContext gc = canvas.getGraphicsContext2D();

//     Affine trans = new Affine();
//     private float zoomLevel;
//     private final float startZoom;



// //     Affine trans = new Affine();

//     public View(){
//         startZoom = 0;
//     }

//     public View(Model model, Stage primaryStage) {
//         this.model = model;

// //     public View(ModelOld model, Stage primaryStage) {
// //         this.model = model;



//         //pan(-0.56*model.chunkHandler.minlon, model.chunkHandler.maxlat);
//         trans.prependTranslation(-0.56*model.chunkHandler.minlon, model.chunkHandler.maxlat); //Calling the code of pan, to prevent redraw before zoom has been run
//         //This is done to avoid getheight and getwidth from canvas, returning way to big values
//         zoom(0, 0, canvas.getHeight() / (model.chunkHandler.maxlat - model.chunkHandler.minlat));


//         startZoom = getZoomDistance();

//         redraw();

//     }

//     private float getZoomDistance(){
//         Point2D p1 = convertTo2DPoint(0,0);
//         Point2D p2 = convertTo2DPoint(0,100);
//         return (float) p1.distance(p2);
//     }

//     private int getDetailLevel(){
//         if(zoomLevel > 50) return 4;
//         if(zoomLevel > 30) return 3;
//         if(zoomLevel > 20) return 2;
//         if(zoomLevel > 15) return 1;
//         return 0;
//     }

// //             primaryStage.setTitle("MapIT");
// //             primaryStage.setScene(scene);
// //             primaryStage.show();

// //             canvas = (Canvas) scene.lookup("#canvas");
// //             gc = canvas.getGraphicsContext2D();

//         updateZoomLevel();
//         updateChunks();

//         int count = 0;
//         long start = System.nanoTime();
//         for(int i = getDetailLevel(); i <= 4; i++){
//             Map<Integer, List<Way>> chunkLayer = model.chunkLayers.get(i);
//             for (int chunk : chunkLayer.keySet()) {
//                 for(int j = 0; j < chunkLayer.get(chunk).size(); j++){
//                     chunkLayer.get(chunk).get(j).draw(gc);
//                     count++;
//                 }
//             }
//         }
//         long end = System.nanoTime();

//         System.out.println("Number of ways drawn: " + count);
//     }

//     private Point2D getUpperLeftCorner() {
//         return convertTo2DPoint(0, 0);
//     }

//     private Point2D getLowerRightCorner() {
//         return convertTo2DPoint(canvas.getWidth(), canvas.getHeight());
//     }

//     private void updateChunks() {

//         Point2D upperLeftCorner = getUpperLeftCorner();
//         Point2D lowerRightCorner = getLowerRightCorner();

//         int upperLeftChunk = model.chunkHandler.pointToChunkIndex(upperLeftCorner);
//         int lowerRightChunk = model.chunkHandler.pointToChunkIndex(lowerRightCorner);

//         Set<Integer> chunks = getSmallestRect(upperLeftChunk, lowerRightChunk, model.chunkHandler.chunkColumnAmount, model.chunkHandler.chunkRowAmount);

//         model.updateChunks(chunks, getDetailLevel());

//     }

//     public Set<Integer> getSmallestRect(int chunk1, int chunk2, int columAmount, int rowAmount){
//         Set<Integer> chunks = new HashSet<>();

//         int a = Math.min(chunk1, chunk2);
//         int b = Math.max(chunk1, chunk2);

//         int height = b/columAmount - a/columAmount;
//         int width = a%columAmount-b%columAmount;

//         for(int i = 0; i <= height; i++) {
//             int c = a + columAmount*i;
//             for(int j = 0; j <= width; j++) {
//                 chunks.add(c-j);
//             }
//         }

//         return chunks;
//     }

// //             pan(-0.56*model.minlon, model.maxlat);
// //             zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
// //             redraw();
// //         } catch (IOException e) {
// //             e.printStackTrace();
// //         }
// //     }

//     void zoom(double dx, double dy, double factor) {
//         trans.prependTranslation(-dx, -dy);
//         trans.prependScale(factor, factor);
//         trans.prependTranslation(dx, dy);
//         redraw();
//     }

//     public void updateZoomLevel(){
//         float newZoom = getZoomDistance();
//         zoomLevel = (newZoom/startZoom) * 100;

//     }



//     public Point2D convertTo2DPoint(double lastX, double lastY) {
//         try {
//             return trans.inverseTransform(lastX, lastY);
//         } catch (NonInvertibleTransformException e) {
//             throw new RuntimeException(e);
//         }
//     }
// }
