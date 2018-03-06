package org.eyeseetea.uicapp.credentials;


import android.support.annotation.NonNull;

public interface CredentialsRepository {

    @NonNull
    String getGitHubBotToken();
}
