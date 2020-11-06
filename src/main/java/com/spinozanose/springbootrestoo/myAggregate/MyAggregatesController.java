package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;
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
     * Notice that *all* of the parameters are passed to the service. Don't try to make
     * the controller or service smart about the parameters of a search. That is a form of
     * validation that should be done in the search service itself!
     *
     * Notice that we have decided that the return is a list of json objects, and that since
     * they are already known to be valid, if maybe incomplete, the clumps of JSON can be
     * passed back, unexamined, to the caller.
     *
     * An alternative implementation could pass back a list of URIs, if that makes more sense.
     *
     * @param searchParams
     * @return List of MyAggregates (as JSON)
     * @throws InvalidSearchParametersException
     */
    @GetMapping
    public List<String> search(@RequestParam Map<String,String> allRequestParams) throws InvalidSearchParametersException {
        return service.search(allRequestParams);
    }
}
