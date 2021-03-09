package com.orange.neptunev2.backend_registry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.*;
import com.orange.neptunev2.backend_registry.entity.Backend;
import com.orange.neptunev2.backend_registry.entity.BackendUpdateDto;
import com.orange.neptunev2.backend_registry.entity.Credential;
import com.orange.neptunev2.backend_registry.exceptions.BackendNotFoundException;
import com.orange.neptunev2.backend_registry.repository.BackendRepository;
import com.orange.neptunev2.backend_registry.responses.PageResponseFactory;
import com.orange.neptunev2.backend_registry.serialization.GroupOfSerialization;
import lombok.var;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequestMapping(name = "app_backends_", path = "/v1/rest/backends")
public class BackendController {

	@Autowired
	private BackendRepository repository;

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(name = "create")
	@ResponseBody
	public Backend create(@Valid @RequestBody Backend backend) throws JsonProcessingException, JSONException {

		Backend savedBackend = new Backend();
		ObjectMapper mapper = new ObjectMapper();
		String jsonParameter = mapper.writeValueAsString(backend);

		JSONObject jsonbject = new JSONObject(jsonParameter);
		JSONArray jsonArray = jsonbject.names();

		ArrayList<String> listParameter = new ArrayList<String>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				listParameter.add(jsonArray.getString(i));
			}
		}

		if (listParameter.contains("name") && listParameter.contains("host")) {
			savedBackend = repository.save(backend);
		} else if (listParameter.contains("name") && listParameter.contains("host") && listParameter.contains("credential")) {
			Credential credential = new Credential();
			credential.setType(backend.getCredential().getType());
			credential.setValue(backend.getCredential().getValue());
			backend.setCredential(credential);
			savedBackend = repository.save(backend);
		}
		return savedBackend;
	}


	@GetMapping(name = "read", path = "/{id}")
	@ResponseBody
	public Backend read(@PathVariable String id) {

		Optional<Backend> backend = repository.findById(UUID.fromString(id));
		if (!backend.isPresent())
			throw new BackendNotFoundException();
		return backend.get();

	}

	@RequestMapping(name = "update", path = "/{id}", method = {RequestMethod.PUT})
	@ResponseBody
	public Backend update(@Valid @RequestBody BackendUpdateDto backendUpdateDto, @PathVariable("id") String id) {

		Optional<Backend> backendOptional=repository.findById(UUID.fromString(id));

		if (!backendOptional.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found"
			);
		}

		Backend backend=backendOptional.get();

		if(backendUpdateDto.getName() !=null){
			backend.setName(backendUpdateDto.getName());
		}

		if(backendUpdateDto.getHost() !=null){
			backend.setHost(backendUpdateDto.getHost());
		}

		if(backendUpdateDto.getPort() !=null){
			backend.setPort(backendUpdateDto.getPort());
		}

		if(backendUpdateDto.isEnabled() != null){
			backend.setEnabled(backendUpdateDto.isEnabled());
		}

		if(backendUpdateDto.getScheme() !=null){
			backend.setScheme(backendUpdateDto.getScheme());
		}

		if(backendUpdateDto.getCredential() != null){
			backend.setCredential(backendUpdateDto.getCredential());
		}
		repository.save(backend);
		return backend;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(name = "read", path = "/{id}")
	public void delete(@PathVariable String id) {
		Backend backend = repository.findById(UUID.fromString(id))
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		repository.delete(backend);
	}


	/*@GetMapping(name = "list",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> retrieveBackends(@PageableDefault Pageable pageable, @RequestParam(value = "details", required = false) String details) throws JsonProcessingException, JSONException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String response;
		Page<Backend> groupOfBackend=repository.findAll(pageable);
		if (details!=null){
			response = mapper.writerWithView(GroupOfSerialization.Details.class).writeValueAsString(groupOfBackend);
		}else{
			response = mapper.writerWithView(GroupOfSerialization.Summary.class).writeValueAsString(groupOfBackend);
			System.out.println(converter(response));
		}
		return new ResponseEntity<String>(response,HttpStatus.OK);
	}*/

	@GetMapping(name = "list",produces = MediaType.APPLICATION_JSON_VALUE)
	//public String retrieveBackends(@PageableDefault Pageable pageable, @RequestParam(value = "details", required = false) String details) throws JsonProcessingException, JSONException {
	public ResponseEntity<JSONArray> retrieveBackends(@PageableDefault Pageable pageable, @RequestParam(value = "details", required = false) String details) throws JsonProcessingException, JSONException {
	//public ResponseEntity<JSONObject> retrieveBackends(@PageableDefault Pageable pageable, @RequestParam(value = "details", required = false) String details) throws JsonProcessingException, JSONException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String response;
		Page<Backend> groupOfBackend=repository.findAll(pageable);
		if (details!=null){
			response = mapper.writerWithView(GroupOfSerialization.Details.class).writeValueAsString(groupOfBackend);
		}else{
			response = mapper.writerWithView(GroupOfSerialization.Summary.class).writeValueAsString(groupOfBackend);

		}
		JSONObject jsonObject=converter(response);
		//JsonObject jsonObject=convertUsingFromJson(response);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(converterJsonArray(response));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		//JSONObject jsonObjectResponse= PageResponseFactory.createResponseJsonBodyJsonObject(jsonObject,groupOfBackend);
		JSONArray jsonObjectResponse= PageResponseFactory.createResponseJsonBodyArray(converterJsonArray(response),groupOfBackend);

		return new ResponseEntity<JSONArray>(converterJsonArray(response),HttpStatus.OK);
		//return new ResponseEntity<String>(getJsonObject(response),HttpStatus.OK);

	}


	private JSONObject converter(String response) throws JSONException {
		JSONObject jsonObj = new JSONObject(response);
		return jsonObj ;
	}

	private JSONArray converterJsonArray(String response) throws JSONException {
		JSONObject jsonObj = new JSONObject(response);
		return jsonObj.getJSONArray("content");
	}

	private JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray) throws JSONException {
		JSONObject jsonObject=new JSONObject();
		for (int n =0;n < jsonArray.length();n++){
			jsonObject=jsonArray.getJSONObject(n);
		}
		return jsonObject;
	}

	private String getJsonObject(String response){
		Gson gson=new Gson();
		JsonElement json = gson.fromJson(response, JsonElement.class);
		String jsonInString = gson.toJson(json);
		return jsonInString;
	}



}
