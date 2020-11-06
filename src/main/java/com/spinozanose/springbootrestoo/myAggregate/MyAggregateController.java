package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
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

/**
 * This is the main implementation of the Springboot functionality and is implemented in the
 * standard Springboot way. However, the transactional logic for the operations is in the
 * MyAggregateService.
 *
 * Notice that all of the RESTful interface concerns are managed here and do not bleed into the
 * rest of MyAggregate. For example, converting application exceptions to the appropriate RESTful
 * responses is left as much as possible to the Springboot framework: it is why we have it.
 */
@RestController
@RequestMapping("/v1/myaggregate")
public class MyAggregateController {

    @Autowired
    private MyAggregateService service;

    /**
     * We throw exceptions that are handled through the Spring ControllerAdvice functionality.
     *
     * @param body
     * @return ResponseEntity<URI>
     * @throws InvalidDomainDataException
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<URI> create(@RequestBody String body) throws InvalidDomainDataException {
        final Map<String, Object> jsonObject;
        // Note: JSONObject class extends HashMap
        try {
            jsonObject = (JSONObject) new JSONParser().parse(body);
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a JSON Object", e);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON Parse Error", e);
        }
        //
        final MyAggregate myAggregate = service.create(jsonObject);
        //
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
