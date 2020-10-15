package com.spinozanose.springbootrestoo.myAggregate;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/myaggregates")
class MyAggregatesController {

    @Autowired
    private MyAggregateService service;

    /**
     * @param allRequestParams
     * @return List of Ids
     *
     * Notice that *all* of the parameters are passed to the service. Don't try to
     * make the controller smart about the parameters of a search. That is a form
     * of validation and should be done in the service!
     */
    @GetMapping
    public List<String> search(@RequestParam Map<String,String> allRequestParams) {
        return service.search(allRequestParams);
    }
}
