package rs.pupin.leasegreendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class LeaseDaoGenerator {
    /**
     * setting up the database
     *
     * @param args input
     * @throws Exception hopefully not
     */
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(17, "rs.pupin.model");
        //map layers
        Entity layer = schema.addEntity("Layer");
        layer.addIdProperty().primaryKey().autoincrement();
        layer.addStringProperty("name");

        //polyline table
        Entity polyline = schema.addEntity("Polyline");
        polyline.addIdProperty().primaryKey().autoincrement();
        polyline.addDoubleProperty("lat");
        polyline.addDoubleProperty("longit");
        polyline.addStringProperty("markers");
        //to-One-relation to layer
        Property layerIdProperty = polyline.addLongProperty("map_layer_id").getProperty();
        polyline.addToOne(layer, layerIdProperty);

        //polygon table
        Entity polygon = schema.addEntity("Polygon");
        polygon.addIdProperty().primaryKey().autoincrement();
        polygon.addDoubleProperty("lat");
        polygon.addDoubleProperty("longit");
        polygon.addStringProperty("markers");
        //to-One-relation to layer
        Property layerIdProperty2 = polygon.addLongProperty("map_layer_id").getProperty();
        polygon.addToOne(layer, layerIdProperty2);

        //point of interest table
        Entity pointOfInterest = schema.addEntity("PointOfInterest");
        pointOfInterest.addIdProperty().primaryKey().autoincrement();
        pointOfInterest.addStringProperty("comment");
        pointOfInterest.addDoubleProperty("lat");
        pointOfInterest.addDoubleProperty("longit");
        //to-One-relation to layer
        Property layerIdProperty3 = pointOfInterest.addLongProperty("map_layer_id").getProperty();
        pointOfInterest.addToOne(layer, layerIdProperty3);

        Entity groundOverlay = schema.addEntity("GroundOverlay");
        groundOverlay.addIdProperty().primaryKey().autoincrement();
        groundOverlay.addStringProperty("path");
        groundOverlay.addDoubleProperty("lat");
        groundOverlay.addDoubleProperty("longit");
        groundOverlay.addFloatProperty("width");
        groundOverlay.addFloatProperty("height");
        groundOverlay.addFloatProperty("rotation");
        //to-One-relation to layer
        Property layerIdProperty4 = groundOverlay.addLongProperty("map_layer_id").getProperty();
        groundOverlay.addToOne(layer, layerIdProperty4);

        new DaoGenerator().generateAll(schema, "C:\\Users\\Larissa\\AndroidStudioProjects\\CustomPolyline2\\app\\src\\main\\java");
    }
}