package org.eyeseetea.uicapp;


import android.app.Application;
import android.support.annotation.NonNull;

import com.github.stkent.bugshaker.BugShaker;
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType;
import com.github.stkent.bugshaker.github.GitHubConfiguration;

import org.eyeseetea.uicapp.credentials.CredentialsRepository;
import org.eyeseetea.uicapp.credentials.RawCredentialsRepository;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String getGitHubBotToken = getGitHubBotToken();


        BugShaker.get(this)
                .setEmailAddresses("someone@example.com")
                .setLoggingEnabled(BuildConfig.DEBUG)
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setGitHubInfo(new GitHubConfiguration(
                        "eyeseetea/bugshaker-android",
                        getGitHubBotToken,
                        "eyeseeteabottest/snapshots",
                        "master"))
                .assemble()
                .start();
    }

    @NonNull
    private String getGitHubBotToken() {
        CredentialsRepository credentials = new RawCredentialsRepository(this);
        return credentials.getGitHubBotToken();
    }
}
