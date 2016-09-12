package rs.pupin.custompolyline2;

import java.io.File;

/**
 * Created by Larissa on 09.09.2016.
 */
abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}