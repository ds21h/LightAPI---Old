/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jb.licht.gegevens.Aktie;
import jb.licht.gegevens.Gegevens;
import jb.licht.gegevens.Huidig;
import jb.licht.klassen.Schakelaar;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Aktie")
public class AktieR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AktieR
     */
    public AktieR() {
    }

    /**
     * PUT method for updating or creating an instance of AktieR
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String putXml(String pParams) {
        Parameters lParameters;
        JSONObject lAntwoord;
        int lTeller;
        Gegevens lGegevens;
        String lResult;
        int lAantGoed;
        String lOmschr;

        lParameters = new Parameters(pParams);
        lAntwoord = new JSONObject();
        if (lParameters.xAantalPar() == 0) {
            lAntwoord.put("resultaat", "NOK");
            lAntwoord.put("omschrijving", "Geen parameters gevonden");
            return lAntwoord.toString();
        }
        if (!lParameters.xGoed()) {
            lAntwoord.put("resultaat", "NOK");
            lAntwoord.put("omschrijving", "Formaatfout in parameters");
            return lAntwoord.toString();
        }
        
        lGegevens = new Gegevens();
        lAantGoed = 0;
        for (lTeller = 0; lTeller < lParameters.xAantalPar(); lTeller++) {
            lResult = sExecAktie(lParameters.xKey(lTeller), lParameters.xWaarde(lTeller), lGegevens);
            if (lResult.equals("OK")) {
                lAantGoed++;
            }
        }
        if (lAantGoed==0){
            lResult = "NOK";
            lOmschr = "Aktie(s) niet uitgevoerd";
        } else {
            if (lAantGoed==lParameters.xAantalPar()){
                lResult = "OK";
                lOmschr = "Aktie(s) uitgevoerd";
            } else {
                lResult = "xOK";
                lOmschr = "Aktie(s) gedeeltelijk uitgevoerd";
            }
        }
        lAntwoord.put("resultaat", lResult);
        lAntwoord.put("omschrijving", lOmschr);
        return lAntwoord.toString();
    }

    private String sExecAktie(String pPar, String pWaarde, Gegevens pGegevens) {
        Aktie lAktie;
        String lResult;

        switch (pPar) {
            case "zetaan": {
                lResult = sTestSchakelaar(pGegevens, pWaarde);
                if (lResult.equals("OK")) {
                    lAktie = new Aktie(Aktie.cAktieZetAan, pWaarde);
                    pGegevens.xNieuweAktie(lAktie);
                }
                break;
            }
            case "zetuit": {
                lResult = sTestSchakelaar(pGegevens, pWaarde);
                if (lResult.equals("OK")) {
                    lAktie = new Aktie(Aktie.cAktieZetUit, pWaarde);
                    pGegevens.xNieuweAktie(lAktie);
                }
                break;
            }
            case "zetlichtuit": {
                lResult = sTestLichtUit(pGegevens, pWaarde);
                if (lResult.equals("OK")) {
                    lAktie = new Aktie(Aktie.cAktieZetLichtUit, pWaarde);
                    pGegevens.xNieuweAktie(lAktie);
                }
                break;
            }
            case "zetallesaan": {
                lAktie = new Aktie(Aktie.cAktieZetAllesAan);
                pGegevens.xNieuweAktie(lAktie);
                lResult = "OK";
                break;
            }
            case "zetallesuit": {
                lAktie = new Aktie(Aktie.cAktieZetAllesUit);
                pGegevens.xNieuweAktie(lAktie);
                lResult = "OK";
                break;
            }
            default: {
                lResult = "NOK";
                break;
            }
        }
        return lResult;
    }

    private String sTestSchakelaar(Gegevens pGegevens, String pId) {
        Schakelaar lSchakelaar;
        String lResult;

        lSchakelaar = pGegevens.xSchakelaar(pId);
        if (lSchakelaar.xNaam().equals(pId)) {
            if (lSchakelaar.xAktief()) {
                lResult = "OK";
            } else {
                lResult = "NOK";
            }
        } else {
            lResult = "NOK";
        }
        return lResult;
    }

    private String sTestLichtUit(Gegevens pGegevens, String pLichtUit) {
        String lResult;
        ZonedDateTime lLichtUit;
        Huidig lHuidig;

        try {
            lLichtUit = ZonedDateTime.parse(pLichtUit, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            lHuidig = pGegevens.xHuidig();
            if (lLichtUit.isAfter(lHuidig.xBijwerken())) {
                lResult = "NOK";
            } else {
                if (lLichtUit.isBefore(lHuidig.xZonsOndergang())) {
                    lResult = "NOK";
                } else {
                    lResult = "OK";
                }
            }
        } catch (DateTimeParseException e) {
            lResult = "NOK";
        }

        return lResult;
    }
}
