package com.orange.neptunev2.backend_registry.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.orange.neptunev2.backend_registry.entity.Backend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Toukeat Tatsi J
 */
public class PageResponseFactory {

    private PageResponseFactory() {

    }

    public static <T> ResponseEntity<JSONObject> createResponseEntity(HttpStatus status, HttpHeaders headers, Page<T> page, HashMap<String, Object> filters, List<String> options) throws JSONException, JsonProcessingException {
        return createResponseEntity(status, headers, page, filters, options);
    }

    public static <T> ResponseEntity<JSONObject> createResponseEntity(HttpStatus status, HttpHeaders headers, Page<T> page, HashMap<String, Object> filters, List<String> options, ObjectWriter itemObjectWriter,String response) throws JSONException, JsonProcessingException {
        JSONObject jsonResponseObject = createResponseJsonBody(page, filters, options, itemObjectWriter);
        return ResponseEntity.status(status).headers(headers).contentType(MediaType.APPLICATION_JSON).body(jsonResponseObject);
    }

    public static <T> JSONObject createResponseJsonBody(Page<T> page) throws JSONException, JsonProcessingException {
        return createResponseJsonBody(page, null, null, null);
    }

    public static <T> JSONObject createResponseJsonBody(Page<T> page, HashMap<String, Object> filters, List<String> options) throws JSONException, JsonProcessingException {
        return createResponseJsonBody(page, filters, options, null);
    }

    public static <T> JSONObject createResponseJsonBody(Page<T> page, HashMap<String, Object> filters, List<String> options, ObjectWriter objectWriter) throws JSONException, JsonProcessingException {

        if (filters == null) {
            filters = new HashMap<>();
        }
        if (options == null) {
            options = new ArrayList<>();
        }
        if (objectWriter == null) {
            objectWriter = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")).writer();
        }

        JSONObject jsonResponseObject = new JSONObject();
        jsonResponseObject.put("page", page.getTotalPages());
        jsonResponseObject.put("itemPerPage", page.getSize());
        jsonResponseObject.put("filters", new JSONObject(objectWriter.writeValueAsString(filters)));
        jsonResponseObject.put("fullyItems", page.getTotalElements());
        jsonResponseObject.put("pageNumber", page.getNumber());
        jsonResponseObject.put("itemOffset",0);
        jsonResponseObject.put("options", new JSONArray(objectWriter.writeValueAsString(options)));
        List<T> items = page.getContent();
        String jsonString = objectWriter.writeValueAsString(items);
        return jsonResponseObject;
    }

    private static <T> JSONObject AddKeyToResponse(JSONObject jsonObject, Page<T> page, HashMap<String, Object> filters, List<String> options, ObjectWriter objectWriter) throws JSONException, JsonProcessingException {

        if (filters == null) {
            filters = new HashMap<>();
        }
        if (options == null) {
            options = new ArrayList<>();
        }
        if (objectWriter == null) {
            objectWriter = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")).writer();
        }

        JSONObject jsonResponse=new JSONObject();
        jsonResponse=jsonObject;
        jsonResponse.put("page", page.getTotalPages());
        jsonResponse.put("itemPerPage", page.getSize());
        jsonResponse.put("filters", new JSONObject(objectWriter.writeValueAsString(filters)));
        jsonResponse.put("fullyItems", page.getTotalElements());
        jsonResponse.put("pageNumber", page.getNumber());
        jsonResponse.put("itemOffset",0);
        jsonResponse.put("options", new JSONArray(objectWriter.writeValueAsString(options)));
        List<T> items = page.getContent();
        String jsonString = objectWriter.writeValueAsString(items);
        jsonResponse.put("items", new JSONArray(jsonString));

        return jsonResponse;

    }



    private static <T> JSONArray AddKeyToResponseArray(JSONArray jsonArray, Page<T> page, HashMap<String, Object> filters, List<String> options, ObjectWriter objectWriter) throws JSONException, JsonProcessingException {

        if (filters == null) {
            filters = new HashMap<>();
        }
        if (options == null) {
            options = new ArrayList<>();
        }
        if (objectWriter == null) {
            objectWriter = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")).writer();
        }


        JSONObject jsonResponse=new JSONObject();
        JSONArray jsonResponseArray=new JSONArray();

        jsonResponse.put("items",jsonArray);

        jsonResponse.put("page", page.getTotalPages());
        jsonResponse.put("itemPerPage", page.getSize());
        jsonResponse.put("filters", new JSONObject(objectWriter.writeValueAsString(filters)));
        jsonResponse.put("fullyItems", page.getTotalElements());
        jsonResponse.put("pageNumber", page.getNumber());
        jsonResponse.put("itemOffset",0);
        List<T> items = page.getContent();
        String jsonString = objectWriter.writeValueAsString(items);
        //jsonResponse.put("items", new JSONArray(jsonString));

        return jsonResponseArray.put(jsonResponse);

    }

    public static <T> JSONArray createResponseJsonBodyArray(JSONArray jsonArray,Page<T> page) throws JSONException, JsonProcessingException {
        return AddKeyToResponseArray(jsonArray, page, null, null,null);
    }

    public static <T> JSONObject createResponseJsonBodyJsonObject(JSONObject jsonObject,Page<T> page) throws JSONException, JsonProcessingException {
        return createResponseJsonBody(jsonObject, page, null, null,null);
    }

    public static <T> JSONObject createResponseJsonBody(JSONObject jsonObject, Page<T> groupOfBackend, HashMap<String, Object> filters, List<String> options, ObjectWriter objectWriter) throws JSONException, JsonProcessingException {


        if (filters == null) {
            filters = new HashMap<>();
        }
        if (options == null) {
            options = new ArrayList<>();
        }
        if (objectWriter == null) {
            objectWriter = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00")).writer();
        }


        JSONObject jsonResponse=jsonObject;


        jsonResponse.put("page", groupOfBackend.getTotalPages());
        jsonResponse.put("itemPerPage", groupOfBackend.getSize());
        jsonResponse.put("filters", new JSONObject(objectWriter.writeValueAsString(filters)));
        jsonResponse.put("fullyItems", groupOfBackend.getTotalElements());
        jsonResponse.put("pageNumber", groupOfBackend.getNumber());
        jsonResponse.put("itemOffset",0);
        jsonResponse.put("options", new JSONArray(objectWriter.writeValueAsString(options)));
        List<T> items = groupOfBackend.getContent();
        String jsonString = objectWriter.writeValueAsString(items);
        jsonResponse.put("items", items);

        return jsonResponse;
    }
}
