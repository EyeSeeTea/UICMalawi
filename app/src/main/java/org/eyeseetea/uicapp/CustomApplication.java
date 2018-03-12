package org.eyeseetea.uicapp;


import android.app.Application;

import com.github.stkent.bugshaker.BugShaker;
import com.github.stkent.bugshaker.flow.dialog.AlertDialogType;
import com.github.stkent.bugshaker.github.GitHubConfiguration;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BugShaker.get(this)
                .setEmailAddresses("someone@example.com")
                .setLoggingEnabled(BuildConfig.DEBUG)
                .setAlertDialogType(AlertDialogType.APP_COMPAT)
                .setGitHubInfo(new GitHubConfiguration(
                        "eyeseetea/uicapp",
                        BuildConfig.GIT_HUB_BOT_TOKEN,
                        "eyeseeteabottest/snapshots",
                        "master"))
                .assemble()
                .start();
    }

}
