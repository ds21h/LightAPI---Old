/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import jb.licht.gegevens.Gegevens;
import jb.licht.klassen.Schakelaar;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
public class SchakelaarR {

    private String mId;

    /**
     * Creates a new instance of SchakelaarR
     */
    private SchakelaarR(String pId) {
        mId = pId;
    }

    /**
     * Get instance of the SchakelaarR
     */
    public static SchakelaarR getInstance(String pId) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of SchakelaarR class.
        return new SchakelaarR(pId);
    }

    /**
     * Retrieves representation of an instance of jb.licht.api.SchakelaarR
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Gegevens lGegevens;
        Schakelaar lSchakelaar;
        JSONObject lSchakelaarJ;

        lGegevens = new Gegevens();
        lSchakelaar = lGegevens.xSchakelaar(mId);
        lGegevens.xAfsluit();
        lSchakelaarJ = lSchakelaar.xSchakelaar();
        return lSchakelaarJ.toString();
    }

    /**
     * PUT method for updating or creating an instance of SchakelaarR
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String putJson(String pParams) {
        Parameters lParameters;
        JSONObject lAntwoord;
        JSONObject lSchakel;
        String lAktie;
        Gegevens lGegevens;
        Schakelaar lSchakelaar;
        String lResultaat;
        String lOmschrijving;

        lAntwoord = new JSONObject();
        if (mId.equals("")) {
            lAntwoord.put("resultaat", "NOK");
            lAntwoord.put("omschrijving", "Geen ID opgegeven");
            return lAntwoord.toString();
        }
        lParameters = new Parameters(pParams);
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
        if (!lParameters.xKey(0).equals("aktie")) {
            lAntwoord.put("resultaat", "NOK");
            lAntwoord.put("omschrijving", "Geen aktie aangegeven");
            return lAntwoord.toString();
        }
        lAktie = lParameters.xWaarde(0);
        lGegevens = new Gegevens();
        switch (lAktie) {
            case "wijzig":
                lSchakelaar = lGegevens.xSchakelaar(mId);
                if (lSchakelaar.xNaam().equals(mId)) {
                    if (lParameters.xAantalPar() > 1) {
                        lResultaat = sWijzigSchakelaar(lSchakelaar, lParameters);
                        if (lResultaat.equals("NOK")) {
                            lOmschrijving = "Wijziging niet uitgevoerd";
                        } else {
                            lGegevens.xWijzigSchakelaar(lSchakelaar);
                            if (lResultaat.equals("OK")) {
                                lOmschrijving = "Wijziging uitgevoerd";
                            } else {
                                lOmschrijving = "Wijzing gedeeltelijk uitgevoerd";
                            }
                        }
                        lSchakel = lSchakelaar.xSchakelaar();

                        lAntwoord.put("resultaat", lResultaat);
                        lAntwoord.put("omschrijving", lOmschrijving);
                        lAntwoord.put("schakelaar", lSchakel);
                    } else {
                        lAntwoord.put("resultaat", "NOK");
                        lAntwoord.put("omschrijving", "Niets te wijzigen");
                    }
                } else {
                    lAntwoord.put("resultaat", "NOK");
                    lAntwoord.put("omschrijving", "Schakelaar bestaat niet");
                }
                break;
            case "nieuw":
                lSchakelaar = lGegevens.xSchakelaar(mId);
                if (lSchakelaar.xNaam().equals(mId)) {
                    lAntwoord.put("resultaat", "NOK");
                    lAntwoord.put("omschrijving", "Schakelaar bestaat al");
                } else {
                    lSchakelaar.xNaam(mId);
                    lResultaat = sWijzigSchakelaar(lSchakelaar, lParameters);
                    if (lResultaat.equals("NOK")) {
                        lOmschrijving = "Schakelaar niet opgevoerd";
                    } else {
                        lGegevens.xNieuweSchakelaar(lSchakelaar);
                        if (lResultaat.equals("OK")) {
                            lOmschrijving = "Schakelaar opgevoerd";
                        } else {
                            lOmschrijving = "Schakelaar gedeeltelijk opgevoerd";
                        }
                    }
                    lSchakel = lSchakelaar.xSchakelaar();

                    lAntwoord.put("resultaat", lResultaat);
                    lAntwoord.put("omschrijving", lOmschrijving);
                    lAntwoord.put("schakelaar", lSchakel);
                }
                break;
            case "verwijder":
                lSchakelaar = lGegevens.xSchakelaar(mId);
                if (lSchakelaar.xNaam().equals(mId)) {
                    if (lParameters.xAantalPar() > 1) {
                        lAntwoord.put("resultaat", "NOK");
                        lAntwoord.put("omschrijving", "Te veel parameters");
                    } else {
                        lGegevens.xVerwijderSchakelaar(mId);
                        lAntwoord.put("resultaat", "OK");
                        lAntwoord.put("omschrijving", "Schakelaar verwijderd");
                    }
                } else {
                    lAntwoord.put("resultaat", "NOK");
                    lAntwoord.put("omschrijving", "Schakelaar bestaat niet");
                }
                break;
            default:
                lAntwoord.put("resultaat", "NOK");
                lAntwoord.put("omschrijving", "Onbekende aktie");
                break;
        }
        return lAntwoord.toString();
    }

    private String sWijzigSchakelaar(Schakelaar pSchakelaar, Parameters pParameters) {
        int lTeller;
        int lAantGoed;
        int lAantFout;
        int lResult;
        int lVolgNummer;
        int lPauze;

        lAantGoed = 0;
        lAantFout = 0;
        for (lTeller = 1; lTeller < pParameters.xAantalPar(); lTeller++) {
            switch (pParameters.xKey(lTeller)) {
                case "volgnummer":
                    try {
                        lVolgNummer = Integer.parseInt(pParameters.xWaarde(lTeller));
                        lResult = pSchakelaar.xVolgNummer(lVolgNummer);
                        if (lResult == 0) {
                            lAantGoed++;
                        } else {
                            lAantFout++;
                        }
                    } catch (NumberFormatException pExc) {
                        lAantFout++;
                    }
                    break;
                case "type":
                    lResult = pSchakelaar.xType(pParameters.xWaarde(lTeller));
                    if (lResult == 0) {
                        lAantGoed++;
                    } else {
                        lAantFout++;
                    }
                    break;
                case "groep":
                    lResult = pSchakelaar.xGroep(pParameters.xWaarde(lTeller));
                    if (lResult == 0) {
                        lAantGoed++;
                    } else {
                        lAantFout++;
                    }
                    break;
                case "punt":
                    lResult = pSchakelaar.xPunt(pParameters.xWaarde(lTeller));
                    if (lResult == 0) {
                        lAantGoed++;
                    } else {
                        lAantFout++;
                    }
                    break;
                case "ip":
                    lResult = pSchakelaar.xIP(pParameters.xWaarde(lTeller));
                    if (lResult == 0) {
                        lAantGoed++;
                    } else {
                        lAantFout++;
                    }
                    break;
                case "aktief":
                    if (pParameters.xWaarde(lTeller).equals("true")) {
                        pSchakelaar.xAktief(true);
                        lAantGoed++;
                    } else {
                        if (pParameters.xWaarde(lTeller).equals("false")) {
                            pSchakelaar.xAktief(false);
                            lAantGoed++;
                        } else {
                            lAantFout++;
                        }
                    }
                    break;
                case "pauze":
                    try {
                        lPauze = Integer.parseInt(pParameters.xWaarde(lTeller));
                        lResult = pSchakelaar.xPauze(lPauze);
                        if (lResult == 0) {
                            lAantGoed++;
                        } else {
                            lAantFout++;
                        }
                    } catch (NumberFormatException pExc) {
                        lAantFout++;
                    }
                    break;
                default:
                    lAantFout++;
                    break;
            }
        }

        lResult = pSchakelaar.xTestSchakelaar();
        if (lResult == 0) {
            if (lAantFout == 0) {
                lAantGoed++;
            }
        } else {
            lAantFout++;
        }

        if (lAantGoed == 0) {
            return "NOK";
        } else {
            if (lAantFout == 0) {
                return "OK";
            } else {
                return "xOK";
            }
        }
    }
}
