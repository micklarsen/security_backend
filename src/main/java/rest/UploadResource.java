package rest;


import webscraper.TagCounter;
import webscraper.TagDTO;
import webscraper.Tester;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.util.List;

@Path("/upload")
public class UploadResource {

    @Context
    private UriInfo context;


    @Path("sequential")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTagsSequental() {
        long startTime = System.nanoTime();
        List<TagCounter> dataFetched = Tester.runSequental();
        long endTime = System.nanoTime() - startTime;
        return TagDTO.getTagsAsJson("Sequental fetching", dataFetched, endTime);
    }
}