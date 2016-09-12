package rs.pupin.custompolyline2;

import android.os.Environment;

import java.io.File;

/**
 * Created by Larissa on 12.09.2016.
 */
public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

    @Override
    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }
}

