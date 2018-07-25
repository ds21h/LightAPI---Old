/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import jb.licht.gegevens.Gegevens;
import jb.licht.gegevens.Huidig;
import jb.licht.klassen.Schakelaar;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Overzicht")
public class OverzichtR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OverzichtR
     */
    public OverzichtR() {
    }

    /**
     * Retrieves representation of an instance of jb.licht.api.OverzichtR
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gegevens lGegevens;
        List<Schakelaar> lSchakelaars;
        Huidig lHuidig;
//        OverzichtJ lOverzichtJ;
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
        lHuidig = lGegevens.xHuidig();
        lResultaat.put("huidigTijdstip", ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResultaat.put("zonsOndergang", lHuidig.xZonsOndergang().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResultaat.put("lichtUit", lHuidig.xLichtUit().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResultaat.put("fase", lHuidig.xFase());
        lResultaat.put("lichtmeting", lHuidig.xLichtMeting());
        lResultaat.put("schakelaars", lSchakelaarsX);
//        lOverzichtJ = new OverzichtJ(lHuidig, lSchakelaars);
        lGegevens.xAfsluit();
        return lResultaat.toString();
    }
}
