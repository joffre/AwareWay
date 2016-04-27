package com.pji.de.awareway.utilitaires;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pji.de.awareway.MainActivity;
import com.pji.de.awareway.R;
import com.pji.de.awareway.bean.AAUser;
import com.pji.de.awareway.webbridge.AABridge;

/**
 * Created by Geoffrey on 27/04/2016.
 */
public class UserManager {

    AAUser user = null;
    Uri profilImageURL;

    private boolean updated = false;

    public UserManager(){
    }

    public void setUser(AAUser user, Uri profilURL){
        this.user = user;
        this.profilImageURL = profilURL;
        updated = false;
    }

    public boolean isAuthentified(){
        return user != null && user.getEmail() != null && !user.getEmail().isEmpty();
    }

    public boolean isImaged(){
        return this.profilImageURL != null;
    }

    public Uri getProfilImageURL(){
        return this.profilImageURL;
    }

    public AAUser getUser(){
        return this.user;
    }

    public boolean isAlreadyUpdated(){
        return updated;
    }

    public void notifyUpdate(){
        this.updated = true;
    }
}
