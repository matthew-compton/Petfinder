package com.ambergleam.petfinder.service;

import android.util.Log;

import com.ambergleam.petfinder.BuildConfig;
import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.model.SearchResponse;
import com.ambergleam.petfinder.model.SearchResponseLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import rx.Observable;

public class PetfinderService {

    private static final String TAG = PetfinderService.class.getSimpleName();

    public static final String PETFINDER_API_KEY = BuildConfig.PETFINDER_API_KEY;

    public static final String ENDPOINT = "http://api.petfinder.com";

    private static final String FUNCTION_SEARCH = "getRandom";
    private static final String FUNCTION_SEARCH_BY_ID = "get";
    private static final String FUNCTION_SEARCH_WITH_LOCATION = "find";

    private static final String OUTPUT = "full";
    private static final String FORMAT = "json";
    private static final String COUNT = "10";

    private final PetfinderServiceInterface mServiceInterface;
    private PetfinderPreference mPetfinderPreference;

    public static PetfinderService newInstance() {
        Gson gson = new GsonBuilder().create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient())
                .setConverter(new CustomGsonConverter(gson))
                .build();
        return new PetfinderService(restAdapter.create(PetfinderServiceInterface.class));
    }

    private PetfinderService(PetfinderServiceInterface serviceInterface) {
        mServiceInterface = serviceInterface;
    }

    public void setPetfinderPreference(PetfinderPreference petfinderPreference) {
        mPetfinderPreference = petfinderPreference;
    }

    public Observable<SearchResponse> search() {
        return mServiceInterface.search(
                FUNCTION_SEARCH,
                PETFINDER_API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString()
        );
    }

    public Observable<SearchResponse> searchById(String id) {
        return mServiceInterface.searchById(
                FUNCTION_SEARCH_BY_ID,
                PETFINDER_API_KEY,
                FORMAT,
                id
        );
    }

    public Observable<SearchResponseLocation> searchWithLocation(int offset) {
        return mServiceInterface.searchWithLocation(
                FUNCTION_SEARCH_WITH_LOCATION,
                PETFINDER_API_KEY,
                OUTPUT,
                FORMAT,
                COUNT,
                mPetfinderPreference.getAnimalEnum().toUrlFormatString(),
                mPetfinderPreference.getSizeEnum().toUrlFormatString(),
                mPetfinderPreference.getLocationUrlFormatString(),
                offset
        );
    }

    public int getCount() {
        return Integer.valueOf(COUNT);
    }

    private static class CustomGsonConverter extends GsonConverter {

        private static final String JSON_START_PRE = "\"pet\":{";
        private static final String JSON_START_POST = "\"pet\":[{";
        private static final String JSON_END_PRE = "}},\"@xmlns:xsi\":\"http://www.w3.org/2001/XMLSchema-instance\",\"header\":{";
        private static final String JSON_END_POST = "}}],\"header\":{";

        public CustomGsonConverter(Gson gson) {
            super(gson);
        }

        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            String json = toString(body);
            json = json.replace(JSON_START_PRE, JSON_START_POST);
            json = json.replace(JSON_END_PRE, JSON_END_POST);
            body = new JsonTypedInput(json.getBytes(Charset.forName(HTTP.UTF_8)));
            return super.fromBody(body, type);
        }

        private String toString(TypedInput body) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                br = new BufferedReader(new InputStreamReader(body.in()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not read JSON from byte array.", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not read JSON from byte array.", e);
                    }
                }
            }
            return sb.toString();
        }

    }

    private static class JsonTypedInput implements TypedInput {

        private static final String mMimeType = "application/json; charset=UTF-8";
        private final byte[] mBytes;

        public JsonTypedInput(byte[] bytes) {
            this.mBytes = bytes;
        }

        @Override
        public String mimeType() {
            return mMimeType;
        }

        @Override
        public long length() {
            return mBytes.length;
        }

        @Override
        public InputStream in() throws IOException {
            return new ByteArrayInputStream(mBytes);
        }

    }

}