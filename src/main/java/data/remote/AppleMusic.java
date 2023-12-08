package data.remote;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLRequest;
import cafsoft.foundation.URLSession;
import domain.model.Result;
import domain.model.Root;

public class AppleMusic {
    private URLComponents components = null;

    public AppleMusic(){
        components = new URLComponents();
        components.setScheme("https");
        components.setHost("itunes.apple.com");
        components.setPath("/search");
    }

    public interface ErrorCodeCompletionHandler {
        void run (int errorCode, String text);
    }

    public interface RootCompletionHandler {
        void run (Root root);
    }

    public interface CompletionHandler {
        void run();
    }

    public void requestSongByTerm(String searchTerm, int limit,
                                  RootCompletionHandler rootCompletion,
                                  ErrorCodeCompletionHandler errorCodeCompletion){

        components.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("media", "music"),
                new URLQueryItem("entity", "song"),
                new URLQueryItem("limit", String.valueOf(limit)),
                new URLQueryItem("term", searchTerm)
        });

        var url = components.getURL();
        var session = URLSession.getShared();
        var task = session.dataTask(url, (data, response, error) -> {
            if (error != null){
                errorCodeCompletion.run(-1, "");
                return;
            }

            if (response instanceof HTTPURLResponse){
                var httpResponse = (HTTPURLResponse) response;

                var text = (data != null) ? data.toText() : "";
                var root = new Gson().fromJson(text, Root.class);

                if (httpResponse.getStatusCode() == 200){
                    if (rootCompletion != null){
                        rootCompletion.run(root);
                    }
                }else{
                    if(errorCodeCompletion != null){
                        errorCodeCompletion.run(httpResponse.getStatusCode(), text);
                    }
                }
            }

        });

        task.resume();
    }

    public void downloadArtworks(Root root, String basePath){

        if(root == null){
            return;
        }

        for (Result result : root.getResults()){
            var fullFilename = basePath + "/" + result.getLocalArtworkFilename();
            System.out.println("fullFilename " + fullFilename);
            if (!new File(fullFilename).exists()) {

                var components = new URLComponents(result.getArtworkUrl100());

                var request = new URLRequest(components.getURL());

                var session = URLSession.getShared();

                var task = session.downloadTask(request, (localUrl, response, error) -> {

                    if (error != null){
                        return;
                    }

                    if(response instanceof HTTPURLResponse){
                        var httpResponse = (HTTPURLResponse) response;

                        if(httpResponse.getStatusCode() == 200){
                            var file = new File(localUrl.getFile());

                            file.renameTo(new File(fullFilename));
                        }
                    }

                });

                task.resume();
            }else{
                //System.out.println("Entramos aquí3" + root.getResults());
            }
        }
    }

    public void downloadPreviewTrack(Result result, String basePath, CompletionHandler completion,
                                     ErrorCodeCompletionHandler errorCodeCompletionHandler){

        var fullFilename = basePath + "/" + result.getLocalPreviewFilename();
        URLComponents components1 = new URLComponents(result.getPreviewUrl());

        var request = new URLRequest(components1.getURL());
        var session = URLSession.getShared();

        var task = session.downloadTask(request, (localUrl, response, error) -> {

            if (error != null){
                errorCodeCompletionHandler.run(-1, "");
                return;
            }

            if (response instanceof HTTPURLResponse){
                var httpResponse = (HTTPURLResponse) response;

                if(httpResponse.getStatusCode() == 200) {
                    var file = new File(localUrl.getFile());

                    file.renameTo(new File(fullFilename));
                    if (completion != null){
                        completion.run();

                    }else{
                        if (errorCodeCompletionHandler != null){
                            errorCodeCompletionHandler.run(httpResponse.getStatusCode(), null);
                        }
                    }
                }
            }
        });

        task.resume();
    }

}
