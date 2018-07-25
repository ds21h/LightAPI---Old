/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jb.licht.gegevens.Aktie;
import jb.licht.gegevens.Gegevens;
import jb.licht.klassen.Instelling;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Instelling")
public class InstellingR {

    private static final int ResultOK = 0;
    private static final int ResultGeenWijziging = 1;
    private static final int ResultDeelOK = 2;
    private static final int ResultFout = 9;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of InstellingR
     */
    public InstellingR() {
    }

    /**
     * Retrieves representation of an instance of jb.licht.api.InstellingR
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gegevens lGegevens;
        Instelling lInstelling;
        JSONObject lInstellingJ;

        lGegevens = new Gegevens();
        lInstelling = lGegevens.xInstelling();
        lGegevens.xAfsluit();
        lInstellingJ = lInstelling.xInstelling();
        return lInstellingJ.toString();
    }

    /**
     * PUT method for updating or creating an instance of InstellingR
     *
     * @param pVraag representation for the resource
     * @return 
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJSON(String pVraag) {
        JSONObject lVraag;
        JSONObject lDeelVraag;
        JSONObject lAntwoord;
        String lResultaat;
        String lOmschrijving;
        Instelling lInstelling;
        JSONObject lInstellingJ;
        Gegevens lGegevens;
        Aktie lAktie;
        double lLengte;
        int lResult;
        boolean lGewijzigd;
        int lAantGoed = 0;
        int lAantFout = 0;

        try {
            lVraag = new JSONObject(pVraag);
        } catch (JSONException pExc) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        lGegevens = new Gegevens();
        lInstelling = lGegevens.xInstelling();
        lGewijzigd = false;
        try {
            lDeelVraag = lVraag.getJSONObject(Instelling.cLokatie);
            lResult = sVerwerkLokatie(lDeelVraag, lInstelling);
            switch (lResult) {
                case ResultOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    break;
                case ResultDeelOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    lAantFout++;
                    break;
                case ResultFout:
                    lAantFout++;
                    break;
            }
        } catch (JSONException pExc) {
        }
        try {
            lDeelVraag = lVraag.getJSONObject(Instelling.cSensor);
            lResult = sVerwerkSensor(lDeelVraag, lInstelling);
            switch (lResult) {
                case ResultOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    break;
                case ResultDeelOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    lAantFout++;
                    break;
                case ResultFout:
                    lAantFout++;
                    break;
            }
        } catch (JSONException pExc) {
        }
        try {
            lDeelVraag = lVraag.getJSONObject(Instelling.cLichtUit);
            lResult = sVerwerkLichtUit(lDeelVraag, lInstelling);
            switch (lResult) {
                case ResultOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    break;
                case ResultDeelOK:
                    lGewijzigd = true;
                    lAantGoed++;
                    lAantFout++;
                    break;
                case ResultFout:
                    lAantFout++;
                    break;
            }
        } catch (JSONException pExc) {
        }

        if (lGewijzigd) {
            lGegevens.xWijzigInstelling(lInstelling);
            lAktie = new Aktie(Aktie.cAktieRefresh);
            lGegevens.xNieuweAktie(lAktie);
            lInstelling = lGegevens.xInstelling();
        }
        lGegevens.xAfsluit();
        lInstellingJ = lInstelling.xInstelling();

        if (lAantGoed == 0) {
            if (lAantFout == 0) {
                lResultaat = "OK";
                lOmschrijving = "Niets gewijzigd";
            } else {
                lResultaat = "NOK";
                lOmschrijving = "Fout";
            }
        } else {
            if (lAantFout == 0) {
                lResultaat = "OK";
                lOmschrijving = "Gewijzigd";
            } else {
                lResultaat = "XOK";
                lOmschrijving = "Ged. gewijzigd";
            }
        }
        lAntwoord = new JSONObject();
        lAntwoord.put("resultaat", lResultaat);
        lAntwoord.put("omschrijving", lOmschrijving);
        lAntwoord.put(Instelling.cInstelling, lInstellingJ);
        return Response.ok(lAntwoord.toString()).build();
    }

    private int sVerwerkLokatie(JSONObject pLokatie, Instelling pInstelling) {
        double lLengte;
        double lBreedte;
        int lResult;
        int lAantGoed = 0;
        int lAantFout = 0;

        try {
            lLengte = pLokatie.getDouble(Instelling.cLengte);
            lResult = pInstelling.xLengte(lLengte);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lBreedte = pLokatie.getDouble(Instelling.cBreedte);
            lResult = pInstelling.xBreedte(lBreedte);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }

        if (lAantGoed == 0) {
            if (lAantFout == 0) {
                lResult = ResultGeenWijziging;
            } else {
                lResult = ResultFout;
            }
        } else {
            if (lAantFout == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultDeelOK;
            }
        }
        return lResult;
    }

    private int sVerwerkSensor(JSONObject pSensor, Instelling pInstelling) {
        int lResult;
        int lWaarde;
        int lAantGoed = 0;
        int lAantFout = 0;

        try {
            lWaarde = pSensor.getInt(Instelling.cGrens);
            lResult = pInstelling.xSensorGrens(lWaarde);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lWaarde = pSensor.getInt(Instelling.cDrempel);
            lResult = pInstelling.xSensorDrempel(lWaarde);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lWaarde = pSensor.getInt(Instelling.cMax);
            lResult = pInstelling.xMaxSensor(lWaarde);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lWaarde = pSensor.getInt(Instelling.cInterval);
            lResult = pInstelling.xPeriodeSec(lWaarde);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lWaarde = pSensor.getInt(Instelling.cHerhaling);
            lResult = pInstelling.xPeriodeDonker(lWaarde);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }

        if (lAantGoed == 0) {
            if (lAantFout == 0) {
                lResult = ResultGeenWijziging;
            } else {
                lResult = ResultFout;
            }
        } else {
            if (lAantFout == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultDeelOK;
            }
        }
        return lResult;
    }

    private int sVerwerkLichtUit(JSONObject pLichtUit, Instelling pInstelling) {
        int lResult;
        String lTijdStip;
        int lPeriode;
        int lAantGoed = 0;
        int lAantFout = 0;

        try {
            lTijdStip = pLichtUit.getString(Instelling.cTijdStip);
            lResult = pInstelling.xLichtUit(lTijdStip);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lPeriode = pLichtUit.getInt(Instelling.cPeriode);
            lResult = pInstelling.xUitTijd(lPeriode);
            if (lResult == Instelling.cResultOK) {
                lAantGoed++;
            } else {
                lAantFout++;
            }
        } catch (JSONException pExc) {
        }

        if (lAantGoed == 0) {
            if (lAantFout == 0) {
                lResult = ResultGeenWijziging;
            } else {
                lResult = ResultFout;
            }
        } else {
            if (lAantFout == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultDeelOK;
            }
        }
        return lResult;
    }
}
