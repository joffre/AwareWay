package com.pji.de.awareway.utilitaires;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.GoogleApiClient;
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
    private GoogleApiClient googleAPIClient = null;

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

    public void setGoogleAPIClient(GoogleApiClient googleAPIClient) {
        this.googleAPIClient = googleAPIClient;
    }

    public boolean googleAuthentified() {
        return this.googleAPIClient != null;
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.googleAPIClient;
    }

    public String getFormatedUserNameDetails(){
        String result = "";
        if(isAuthentified()){
            result = AAStringUtils.capitalizeFully(getUser().getFirstName() + " " + getUser().getLastName());
        }
        return result;
    }
}
