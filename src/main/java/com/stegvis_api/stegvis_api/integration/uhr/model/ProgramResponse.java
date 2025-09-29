package com.stegvis_api.stegvis_api.integration.uhr.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramResponse {

    private int antalObjekt;
    private List<Program> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Program {
        private String senasteUrval;
        private String uppkomsttillfälleHeader;
        private String tillfälleId;
        private String tillfälle;
        private String terminId;
        private String termin;
        private String år;
        private String antagningsomgångId;
        private String antagningsomgångKategori;
        private String lärosäteId;
        private String lärosäte;
        private String anmälningsalternativId;
        private String anmälningskod;
        private String anmälningsalternativ;
        private String anmälningsalternativEngelska;
        private String utbildningstyp;
        private String utbildningId;
        private String utbildningsnyckel;
        private String programId;
        private String programinriktning;
        private String studietakt;
        private String startperiod;
        private String undervisningsform;
        private String studietid;
        private String studieort;
        private String examen;
        private String ämnesgrupp;
        private String nyckelord;

        private Sok sok;
        private Urval urval1;
        private Urval urval2;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sok {
        private int sökande;
        private int förstahandsökande;
        private int kvinnligaSökande;
        private int manligaSökande;
        private int kvinnligaFörstahandsökande;
        private int manligaFörstahandsökande;
        private int sökande24ÅrEllerYngre;
        private int sökande25Till34År;
        private int sökande35ÅrEllerÄldre;
        private int förstahandsökande24ÅrEllerYngre;
        private int förstahandsökande25Till34År;
        private int förstahandsökande35ÅrEllerÄldre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Urval {
        private List<Urvalsgrupp> urvalsgrupper;
        private int antagna;
        private int reserver;
        private int kvinnligaAntagna;
        private int manligaAntagna;
        private int kvinnligaReserver;
        private int manligaReserver;
        private int antagna24ÅrEllerYngre;
        private int antagna25Till34År;
        private int antagna35ÅrEllerÄldre;
        private int reserver24ÅrEllerYngre;
        private int reserver25Till34År;
        private int reserver35ÅrEllerÄldre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Urvalsgrupp {
        private String urvalsgruppId;
        private String lägstaAntagnaPoäng;
        private int antagna;
        private int reserver;
    }
}