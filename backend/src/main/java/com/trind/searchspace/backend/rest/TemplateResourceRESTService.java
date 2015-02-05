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

import com.trind.searchspace.backend.service.impl.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/template")
@Component
public class TemplateResourceRESTService {

    @Autowired
    TemplateService templateService;

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(String template) {
        templateService.save(template);
        Response.ResponseBuilder builder = Response.ok();
        return builder.build();
    }

    @GET
    @Path("template")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTemplate(@QueryParam(value = "templateName") String templateName) {
        Response.ResponseBuilder builder = Response.ok().entity(templateService.getById(templateName));
        return builder.build();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTemplates() {
        Response.ResponseBuilder builder = Response.ok().entity(templateService.getTemplates());
        return builder.build();
    }


}
