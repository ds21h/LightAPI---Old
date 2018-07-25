/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.licht.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan
 */
public class Parameters {

    private final String mParam;
    private List<String> mParameters;
    private final boolean mGoed;

    public Parameters(String pParam) {
        mParam = pParam;
        sSplitsParam();
        mGoed = sTestParams();
    }

    public boolean xGoed() {
        return mGoed;
    }

    public String xKey(int pIndex) {
        String lParam;
        int lIs;

        if (pIndex < mParameters.size()) {
            lParam = mParameters.get(pIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return "";
            } else {
                return lParam.substring(0, lIs);
            }
        } else {
            return "";
        }
    }

    public String xWaarde(int pIndex) {
        String lParam;
        int lIs;

        if (pIndex < mParameters.size()) {
            lParam = mParameters.get(pIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return "";
            } else {
                return lParam.substring(lIs + 1);
            }
        } else {
            return "";
        }
    }

    public int xAantalPar() {
        return mParameters.size();
    }

    private void sSplitsParam() {
        String lInput;
        boolean lKlaar;
        int lAmp;
        String lWaarde;

        lInput = mParam;
        lKlaar = false;
        mParameters = new ArrayList<>();
        while (!lKlaar) {
            lAmp = lInput.indexOf("&");
            if (lAmp == -1) {
                lWaarde = lInput;
                lKlaar = true;
            } else {
                lWaarde = lInput.substring(0, lAmp);
                if (lInput.length() > lAmp + 1) {
                    lInput = lInput.substring(lAmp + 1);
                } else {
                    lKlaar = true;
                }
            }
            if (!lWaarde.equals("")) {
                mParameters.add(lWaarde);
            }
        }
    }

    private boolean sTestParams() {
        int lIndex;
        String lParam;
        int lIs;
        int lSp;

        for (lIndex = 0; lIndex < mParameters.size(); lIndex++) {
            lParam = mParameters.get(lIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return false;
            }
            lSp = lParam.indexOf(" ");
            if (lSp != -1) {
                return false;
            }
        }
        return true;
    }
}
