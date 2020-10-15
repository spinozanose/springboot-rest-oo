package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.implementation.exceptions.ObjectNotFoundException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/myaggregate")
public class MyAggregateController {

    @Autowired
    private MyAggregateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<URI> create(@RequestBody String body) {
        final Map<String, Object> jsonObject;
        // Note: JSONObject class extends HashMap
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a JSON Object", e);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON Parse Error", e);
        }

        final MyAggregate myAggregate;
        try {
            myAggregate = service.create(jsonObject);
        } catch (InvalidDomainDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data", e);
        }
        final String id = (String) myAggregate.toMap().get("id");
        final URI location = URI.create("/v1/myaggregate/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> read( @PathVariable String id) {
        final MyAggregate myAggregate = service.read(id);
        if (myAggregate==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found");
        }
        return ResponseEntity.ok(myAggregate.toMap());
    }

    @PutMapping("/{id}")
    public void update(@RequestBody String body, @PathVariable String id) throws InvalidDomainDataException, ObjectNotFoundException {
        final Map<String, Object> data;
        try {
            data = (JSONObject) new JSONParser().parse(body);
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON is not an Object", e);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON Parse Error", e);
        }

        service.update(data);

    }

    @DeleteMapping("/{id}")
    public void delete(@RequestParam(value = "id") String id) {
        try {
            service.delete(id);
        } catch (ObjectNotFoundException e) {
            // Delete is idempotent, so this is fine.
        }
    }

}
