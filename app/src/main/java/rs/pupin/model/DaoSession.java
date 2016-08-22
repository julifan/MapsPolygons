package rs.pupin.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import rs.pupin.model.Layer;
import rs.pupin.model.Polyline;
import rs.pupin.model.Polygon;
import rs.pupin.model.PointOfInterest;
import rs.pupin.model.GroundOverlay;

import rs.pupin.model.LayerDao;
import rs.pupin.model.PolylineDao;
import rs.pupin.model.PolygonDao;
import rs.pupin.model.PointOfInterestDao;
import rs.pupin.model.GroundOverlayDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig layerDaoConfig;
    private final DaoConfig polylineDaoConfig;
    private final DaoConfig polygonDaoConfig;
    private final DaoConfig pointOfInterestDaoConfig;
    private final DaoConfig groundOverlayDaoConfig;

    private final LayerDao layerDao;
    private final PolylineDao polylineDao;
    private final PolygonDao polygonDao;
    private final PointOfInterestDao pointOfInterestDao;
    private final GroundOverlayDao groundOverlayDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        layerDaoConfig = daoConfigMap.get(LayerDao.class).clone();
        layerDaoConfig.initIdentityScope(type);

        polylineDaoConfig = daoConfigMap.get(PolylineDao.class).clone();
        polylineDaoConfig.initIdentityScope(type);

        polygonDaoConfig = daoConfigMap.get(PolygonDao.class).clone();
        polygonDaoConfig.initIdentityScope(type);

        pointOfInterestDaoConfig = daoConfigMap.get(PointOfInterestDao.class).clone();
        pointOfInterestDaoConfig.initIdentityScope(type);

        groundOverlayDaoConfig = daoConfigMap.get(GroundOverlayDao.class).clone();
        groundOverlayDaoConfig.initIdentityScope(type);

        layerDao = new LayerDao(layerDaoConfig, this);
        polylineDao = new PolylineDao(polylineDaoConfig, this);
        polygonDao = new PolygonDao(polygonDaoConfig, this);
        pointOfInterestDao = new PointOfInterestDao(pointOfInterestDaoConfig, this);
        groundOverlayDao = new GroundOverlayDao(groundOverlayDaoConfig, this);

        registerDao(Layer.class, layerDao);
        registerDao(Polyline.class, polylineDao);
        registerDao(Polygon.class, polygonDao);
        registerDao(PointOfInterest.class, pointOfInterestDao);
        registerDao(GroundOverlay.class, groundOverlayDao);
    }
    
    public void clear() {
        layerDaoConfig.getIdentityScope().clear();
        polylineDaoConfig.getIdentityScope().clear();
        polygonDaoConfig.getIdentityScope().clear();
        pointOfInterestDaoConfig.getIdentityScope().clear();
        groundOverlayDaoConfig.getIdentityScope().clear();
    }

    public LayerDao getLayerDao() {
        return layerDao;
    }

    public PolylineDao getPolylineDao() {
        return polylineDao;
    }

    public PolygonDao getPolygonDao() {
        return polygonDao;
    }

    public PointOfInterestDao getPointOfInterestDao() {
        return pointOfInterestDao;
    }

    public GroundOverlayDao getGroundOverlayDao() {
        return groundOverlayDao;
    }

}
