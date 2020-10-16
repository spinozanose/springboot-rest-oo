package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidSearchParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * Notice that *all* of the parameters are passed to the service. Don't try to make
     * the controller or service smart about the parameters of a search. That is a form of
     * validation that should be done in the search service!
     */
    @GetMapping
    public List<String> search(@RequestParam Map<String,String> allRequestParams) throws InvalidSearchParametersException {
        return service.search(allRequestParams);
    }
}
