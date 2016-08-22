package rs.pupin.model;

import rs.pupin.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "POLYGON".
 */
public class Polygon {

    private Long id;
    private Double lat;
    private Double longit;
    private String markers;
    private Long map_layer_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PolygonDao myDao;

    private Layer layer;
    private Long layer__resolvedKey;


    public Polygon() {
    }

    public Polygon(Long id) {
        this.id = id;
    }

    public Polygon(Long id, Double lat, Double longit, String markers, Long map_layer_id) {
        this.id = id;
        this.lat = lat;
        this.longit = longit;
        this.markers = markers;
        this.map_layer_id = map_layer_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPolygonDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongit() {
        return longit;
    }

    public void setLongit(Double longit) {
        this.longit = longit;
    }

    public String getMarkers() {
        return markers;
    }

    public void setMarkers(String markers) {
        this.markers = markers;
    }

    public Long getMap_layer_id() {
        return map_layer_id;
    }

    public void setMap_layer_id(Long map_layer_id) {
        this.map_layer_id = map_layer_id;
    }

    /** To-one relationship, resolved on first access. */
    public Layer getLayer() {
        Long __key = this.map_layer_id;
        if (layer__resolvedKey == null || !layer__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LayerDao targetDao = daoSession.getLayerDao();
            Layer layerNew = targetDao.load(__key);
            synchronized (this) {
                layer = layerNew;
            	layer__resolvedKey = __key;
            }
        }
        return layer;
    }

    public void setLayer(Layer layer) {
        synchronized (this) {
            this.layer = layer;
            map_layer_id = layer == null ? null : layer.getId();
            layer__resolvedKey = map_layer_id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
