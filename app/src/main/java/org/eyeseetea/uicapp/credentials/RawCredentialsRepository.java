package org.eyeseetea.uicapp.credentials;


import android.content.Context;
import android.support.annotation.NonNull;

import org.eyeseetea.uicapp.R;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawCredentialsRepository implements CredentialsRepository {

    public static final String JSON_KEY_GIT_HUB_BOT_TOKEN = "gitHubBotToken";

    @NonNull
    private final Context context;

    @NonNull
    private String gitHubBotToken = "";

    public RawCredentialsRepository(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String getGitHubBotToken() {
        if (gitHubBotToken.isEmpty()) {
            readDataFromJson();
        }

        return gitHubBotToken;
    }

    private void readDataFromJson() {
        try {
            JSONObject jsonObject;
            InputStream inputStream = context.getResources().openRawResource(R.raw.config);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line)
                        .append("\n");
            }
            jsonObject = new JSONObject(sb.toString());
            gitHubBotToken = jsonObject.getString(JSON_KEY_GIT_HUB_BOT_TOKEN);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
