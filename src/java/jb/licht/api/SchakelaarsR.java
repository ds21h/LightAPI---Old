/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import jb.licht.gegevens.Gegevens;
import jb.licht.klassen.Schakelaar;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("/Schakelaars")
public class SchakelaarsR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SchakelaarsR
     */
    public SchakelaarsR() {
    }

    /**
     * Retrieves representation of an instance of jb.licht.api.SchakelaarsR
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gegevens lGegevens;
        List<Schakelaar> lSchakelaars;
        JSONArray lSchakelaarsX;
        JSONObject lResultaat;
        int lTel;

        lGegevens = new Gegevens();
        lSchakelaars = lGegevens.xSchakelaars();
        lSchakelaarsX = new JSONArray();
        for (lTel=0; lTel< lSchakelaars.size(); lTel++) {
            lSchakelaarsX.put(lSchakelaars.get(lTel).xSchakelaar());            
        }
        lResultaat = new JSONObject();
        lResultaat.put("schakelaars", lSchakelaarsX);
        lGegevens.xAfsluit();
        return lResultaat.toString();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public SchakelaarR getSchakelaarR(@PathParam("id") String id) {
        return SchakelaarR.getInstance(id);
    }
}
