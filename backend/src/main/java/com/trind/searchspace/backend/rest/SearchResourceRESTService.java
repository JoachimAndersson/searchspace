/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trind.searchspace.backend.rest;

import com.trind.searchspace.backend.factory.SearchServiceFactory;
import com.trind.searchspace.backend.model.Search;
import com.trind.searchspace.backend.model.query.targettype.QueryTargetType;
import com.trind.searchspace.backend.service.impl.AutoCompleteService;
import com.trind.searchspace.backend.service.impl.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Component
@Path("/search")
public class SearchResourceRESTService {

    @Autowired
    SourceService sourceService;

    @Autowired
    AutoCompleteService autoCompleteService;

    @Autowired
    SearchServiceFactory searchServiceFactory;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(Search search) {
        Response.ResponseBuilder builder = Response.ok().entity(searchServiceFactory.search(search));
        return builder.build();
    }

    @GET
    @Path("sources")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSources() {
        Response.ResponseBuilder builder = Response.ok().entity(sourceService.getAllSourcesWithoutSettings());
        return builder.build();
    }

    @PUT
    @Path("autocomplete/{field}/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<String> autoComplete(@PathParam("value") String value, @PathParam("field") String field, QueryTargetType queryTargetType
    ) {
        return autoCompleteService.autoCompleteQueryTarget(value, field, queryTargetType);
    }

    @PUT
    @Path("list/{field}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<String> autoComplete(@PathParam("field") String field, QueryTargetType queryTargetType) {
        return autoCompleteService.listQueryTargetFieldValues(field, queryTargetType);
    }

}
