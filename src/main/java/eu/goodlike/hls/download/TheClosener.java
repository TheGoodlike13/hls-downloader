package eu.goodlike.hls.download;

import com.google.inject.Inject;
import eu.goodlike.cmd.ProcessRunner;
import eu.goodlike.libraries.okhttp.HttpClients;
import okhttp3.OkHttpClient;

public final class TheClosener implements AutoCloseable {

    @Override
    public void close() throws Exception {
        HttpClients.close(okHttpClient);
        processRunner.close();
    }

    // CONSTRUCTORS

    @Inject TheClosener(OkHttpClient okHttpClient, ProcessRunner processRunner) {
        this.okHttpClient = okHttpClient;
        this.processRunner = processRunner;
    }

    // PRIVATE

    private final OkHttpClient okHttpClient;
    private final ProcessRunner processRunner;

}
